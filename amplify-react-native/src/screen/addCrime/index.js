import React, { Component } from "react";
import axios from "axios";
import {
  Container,
  Item,
  Header,
  Left,
  Body,
  Right,
  Button,
  Icon,
  Title,
  Content,
  Form,
  Picker,
  Label,
  Input,
  DatePicker,
  Textarea,
  Text
} from "native-base";
import styles from "./styles";
import {
  Image,
  AsyncStorage
} from "react-native";
import { Location, Permissions, ImagePicker } from "expo";
import Analytics from "@aws-amplify/analytics";
import awsconfig from "../../../aws-exports";
import Amplify, { Storage } from "aws-amplify";
// retrieve temporary AWS credentials and sign requests
Amplify.configure(awsconfig);
// send analytics events to Amazon Pinpoint
Analytics.configure(awsconfig);

class AddCrime extends Component {
  constructor(props) {
    super(props);

    this.state = {
      crimeType: 1,
      address: "",
      description: "",
      chosenDate: new Date(),
      location: [],
      longitude: "-37.799083",
      latitude: "144.962883",
      errorMessage: "null",
      response: "",
      image: null,
      resultHtml: <Text />,
      eventsSent: 0,
      s3path: "null",
      storedValue: null,
      storedPASSWORD: null,
      otherTypeText: null
    };
    this.setDate = this.setDate.bind(this);
  }
  // send user credential to server to get accesstoken
  async componentDidMount() {
    let storedValue = await AsyncStorage.getItem("ID");
    let storedPASSWORD = await AsyncStorage.getItem("PASSWORD");

    console.log("Fetched data: ", storedValue);
    console.log("Fetched PASSWORD: ", storedPASSWORD);
    axios({
      method: "post",
      url:
        "https://j5d06mrage.execute-api.ap-southeast-2.amazonaws.com/test/auth/signin",
      data: {
        userName: storedValue,
        password: storedPASSWORD
      }
    })
      .then(
        function(response) {
          console.log("response :" + response.data.accessToken);
          this.setState({
            storedValue: response.data.accessToken
          });
        }.bind(this)
      )
      .catch(function() {}.bind(this));
  }
// if user select crime type "other" then render extra text box so user can input type desription
  renderButton() {
    const { crimeType } = this.state;

    if (crimeType == 6) {
      console.log("Other chosen");
      return (
        <Item regular>
          <Input
            value={this.setState.otherTypeText}
            onChangeText={otherTypeText => this.setState({ otherTypeText })}
            placeholder="input crime type:"
          />
        </Item>
      );
    } else {
      console.log("Other not chosen");
    }
  }
// submit button has two form the define type and other crime type.
  onSubmitButtonPress() {
    const {
      address,
      chosenDate,
      description,
      crimeType,
      latitude,
      longitude,
      s3path,
      storedValue,
      otherTypeText
    } = this.state;

    if (crimeType == 6) {
      axios({
        headers: {
          access_token: storedValue
        },
        method: "post",
        url:
          "https://j5d06mrage.execute-api.ap-southeast-2.amazonaws.com/test/case/report",
        data: {
          case_report: {
            address: address,
            description: description,
            date: parseInt(chosenDate),
            longitude: longitude,
            latitude: latitude
          },
          case_other: {
            crime: otherTypeText
          },
          case_evidence: [
            {
              evidence: s3path
            }
          ]
        }
      })
        .then(function(response) {
          console.log(response);
        })
        .catch(function(error) {
          console.log(error);
        });
    } else {
      axios({
        headers: {
          access_token: storedValue
        },
        method: "post",
        url:
          "https://j5d06mrage.execute-api.ap-southeast-2.amazonaws.com/test/case/report",
        data: {
          case_report: {
            type: crimeType,
            address: address,
            description: description,
            date: parseInt(chosenDate),
            longitude: longitude,
            latitude: latitude
          },

          case_evidence: [
            {
              evidence: s3path
            }
          ]
        }
      })
        .then(function(response) {
          console.log(response);
        })
        .catch(function(error) {
          console.log(error);
        });
    }
  }
//setstate of crimetype to be the same tht user pick
  onCrimeTypeValueChange(value) {
    this.setState({
      crimeType: value
    });
  }
  // change date from UI to number only such as Oct 18 2018 to 18102018
  setDate(newDate) {
    var str = newDate.toString().substr(4, 4);
    var intDate;

    if (str.match("Jan")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "01" +
        newDate.toString().substring(11, 15);
    } else if (str.match("Feb")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "02" +
        newDate.toString().substring(11, 15);
    } else if (str.match("Mar")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "03" +
        newDate.toString().substring(11, 15);
    } else if (str.match("Apr")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "04" +
        newDate.toString().substring(11, 15);
    } else if (str.match("May")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "05" +
        newDate.toString().substring(11, 15);
    } else if (str.match("Jun")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "06" +
        newDate.toString().substring(11, 15);
    } else if (str.match("Jul")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "07" +
        newDate.toString().substring(11, 15);
    } else if (str.match("Aug")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "08" +
        newDate.toString().substring(11, 15);
    } else if (str.match("Sep")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "09" +
        newDate.toString().substring(11, 15);
    } else if (str.match("Oct")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "10" +
        newDate.toString().substring(11, 15);
    } else if (str.match("Nov")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "11" +
        newDate.toString().substring(11, 15);
    } else if (str.match("Dec")) {
      intDate =
        newDate.toString().substring(7, 10) +
        "12" +
        newDate.toString().substring(11, 15);
    }
    this.setState({ chosenDate: intDate });
  }
//get user current location from google map API
  _getLocationAsync = async () => {
    let { status } = await Permissions.askAsync(Permissions.LOCATION);
    if (status !== "granted") {
      this.setState({
        errorMessage: "Permission to access location was denied"
      });
    }

    let location = await Location.getCurrentPositionAsync({});
    this.setState({
      location,
      longitude: location.coords.longitude,
      latitude: location.coords.latitude
    });
    console.log(location.coords.longitude);
  };
//Call image picker to let user be able to pick the image
  _pickImage = async () => {
    const { status: cameraRollPerm } = await Permissions.askAsync(
      Permissions.CAMERA_ROLL
    );

    if (cameraRollPerm === "granted") {
      let pickerResult = await ImagePicker.launchImageLibraryAsync({
        allowsEditing: true,
        aspect: [4, 3]
      });
      this._handleImagePicked(pickerResult);
    }
  };
  //when image is picked upload it to s3 bucket
  _handleImagePicked = async pickerResult => {
    const imageName = pickerResult.uri.replace(/^.*[\\\/]/, "");
    const s3path =
      "https://sign67cb05fde0894aad961a9b532d5a7a8b.s3.ap-southeast-2.amazonaws.com/public/" +
      imageName;
    const access = {
      level: "public",
      contentType: "image/jpeg"
    };
    const imageData = await fetch(pickerResult.uri);
    const blobData = await imageData.blob();

    try {
      await Storage.put(imageName, blobData, access);
      console.log("====================");
      console.log(s3path);
      this.setState({ s3path: s3path });
    } catch (err) {
      console.log("error: ", err);
    }
  };
  render() {
    const { navigation } = this.props;
    const itemId = navigation.getParam("AccessToken ");
    let { image } = this.state;
    console.log("KILLME" + itemId);
    return (
      <Container>
        <Header>
          <Left>
            <Button transparent onPress={() => this.props.navigation.goBack()}>
              <Icon name="arrow-back" />
            </Button>
          </Left>
          <Body>
            <Title>Report Crime</Title>
          </Body>
          <Right />
        </Header>
        <Content padder style={styles.content}>
          <Form>
            <Label>Evidence:</Label>
            <Button small style={styles.label} onPress={this._pickImage}>
              <Item regular>
                {image && (
                  <Image
                    source={{ uri: image }}
                    style={{ width: 200, height: 200 }}
                  />
                )}
              </Item>
              <Text>Upload</Text>
            </Button>
            <Label>Type:</Label>
            <Picker
              mode="dropdown"
              iosHeader="Select Type"
              iosIcon={<Icon name="ios-arrow-down-outline" />}
              selectedValue={this.state.crimeType}
              onValueChange={this.onCrimeTypeValueChange.bind(this)}
            >
              <Picker.Item label="Murder" value="1" />
              <Picker.Item label="Assault" value="2" />
              <Picker.Item label="Theft" value="3" />
              <Picker.Item label="Rape" value="4" />
              <Picker.Item label="Election Fraud" value="5" />
              <Picker.Item label="Other" value="6" />
            </Picker>

            {this.renderButton()}

            <Label>Address:</Label>
            <Item regular>
              <Input
                value={this.setState.address}
                onChangeText={address => this.setState({ address })}
                placeholder="input crime scene location here:"
              />
            </Item>

            <Label>Witness date:</Label>
            <Item regular>
              <DatePicker
                locale={"en"}
                timeZoneOffsetInMinutes={undefined}
                modalTransparent={false}
                animationType={"fade"}
                androidMode={"default"}
                placeHolderText="Select date"
                textStyle={{ color: "green" }}
                placeHolderTextStyle={{ color: "#d3d3d3" }}
                onDateChange={this.setDate}
              />
            </Item>

            <Label>Description:</Label>
            <Textarea
              rowSpan={5}
              bordered
              placeholder="input crime desciption here"
              value={this.setState.description}
              onChangeText={description => this.setState({ description })}
            />

            <Button
              small
              style={styles.label}
              onPress={this._getLocationAsync.bind(this)}
            >
              <Text> Use my location </Text>
            </Button>
          </Form>

          <Button
            style={styles.submitButton}
            onPress={this.onSubmitButtonPress.bind(this)}
          >
            <Text>Submit</Text>
          </Button>
        </Content>
      </Container>
    );
  }
}

export default AddCrime;
