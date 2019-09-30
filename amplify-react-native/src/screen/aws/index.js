import React from 'react';
import { Linking, Button, StyleSheet, Text, View } from 'react-native';
import Analytics from '@aws-amplify/analytics';
import {withAuthenticator } from 'aws-amplify-react-native';
import awsconfig from '../../../aws-exports';
import Amplify, {
  Storage
} from 'aws-amplify';
import { ImagePicker, Permissions } from 'expo';
import mime from 'mime-types';

// retrieve temporary AWS credentials and sign requests
Amplify.configure(awsconfig);
// send analytics events to Amazon Pinpoint
Analytics.configure(awsconfig);

class Aws extends React.Component {
    constructor(props) {
      super(props);
      this.handleAnalyticsClick = this.handleAnalyticsClick.bind(this);
      this.state = {resultHtml: <Text></Text>, eventsSent: 0,image: null};
    }
    handleAnalyticsClick() {
      Analytics.record('AWS Amplify Tutorial Event')
        .then( (evt) => {
            const url = 'https://console.aws.amazon.com/pinpoint/home/?region=us-east-1#/apps/'+awsconfig.aws_mobile_analytics_app_id+'/analytics/events';
            let result = (
              <View>
                <Text>Event Submitted.</Text>
                <Text>Events sent: {++this.state.eventsSent}</Text>
                <Text style={styles.link} onPress={() => Linking.openURL(url)}>
                  View Events on the Amazon Pinpoint Console
                </Text>
              </View>
            );
            this.setState({
                'resultHtml': result
            });
        });
    };

     handleS3Click() {
    Storage.put('test2.txt', 'hello')
      .then(result => console.log(result))
      .catch(err => console.log(err));
     };
     _pickImage = async () => {
   const {
     status: cameraRollPerm
   } = await Permissions.askAsync(Permissions.CAMERA_ROLL);

   if (cameraRollPerm === 'granted') {
     let pickerResult = await ImagePicker.launchImageLibraryAsync({
       allowsEditing: true,
       aspect: [4, 3],
     });
     this._handleImagePicked(pickerResult);
   }
  };

  _handleImagePicked = async (pickerResult) => {
    const imageName = pickerResult.uri.replace(/^.*[\\\/]/, '');
    const fileType = mime.lookup(pickerResult.uri);
    const access = {
      level: "public",
      contentType: 'image/jpeg'
    };
    const imageData = await fetch(pickerResult.uri)
    const blobData = await imageData.blob()

    try {
      await Storage.put(imageName, blobData, access)
    } catch (err) {
      console.log('error: ', err)
    }
  }
    render() {
       let { image } = this.state;
      return (
        <View style={styles.container}>
           {/* <PhotoPicker onPick = {data => console.log(data)}/> */}
          <Text>Welcome to your React Native App with Amplify!</Text>
          <Button title="Generate Analytics Event" onPress={this.handleAnalyticsClick} />
          {this.state.resultHtml}
          <Button title="Put" onPress={this. handleS3Click} />
          <Button
          title="Pick an image from camera roll"
          onPress={this._pickImage}
        />
        {image &&
          <Image source={{ uri: image }} style={{ width: 200, height: 200 }} />}
                     
        </View>
      );
    }
    
}


const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  link: {
    color: 'blue'
  }
});

export default withAuthenticator(Aws);

