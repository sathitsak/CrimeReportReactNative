import React, { Component } from 'react';
import {
    Container, Item, Header, Left, Body, Right, Button, Icon,
    Title, Content, Form, Picker, Label, Input, DatePicker, Textarea, Text
} from 'native-base';
import styles from './styles';
import { Platform } from 'react-native';
import { Constants, Location, Permissions } from 'expo';

class AddCrime extends Component {
    constructor(props) {
        super(props);
        this.state = {
            crimeType: 1,
            address: '',
            description: '',
            chosenDate: new Date(),
            location: [],
            longitude: 0,
            latitude: 0,
            errorMessage: 'null',
            response: ''
        };
        this.setDate = this.setDate.bind(this);
    }
    onSubmitButtonPress() {

        const { address, chosenDate, description, crimeType, latitude, longitude, response } = this.state;

        //console.log('Address:', address, 'Description:', description, chosenDate, 'crimeType:', crimeType, 'latitude', latitude, 'longitude', longitude);
       
        fetch('https://0vzctaj85l.execute-api.ap-southeast-2.amazonaws.com/test/case/report', {
            method: 'POST',
           
            body: JSON.stringify({
                "type":crimeType,
                "address": address,
                "date": chosenDate,
                "description": description,
                "longitude": parseInt(longitude),
                "latitude": parseInt(latitude)
            }),
        }).then(response => response.json())
        .then(data => this.setState({ response: data }));
        console.log(response);
        
       
     


    }

    onCrimeTypeValueChange(value) {
        this.setState({
            crimeType: value
        });
    }
    setDate(newDate) {
        
        var  str = newDate.toString().substr(4, 4);
        var intDate;
        
        if (str.match('Jan')){
            intDate = newDate.toString().substring(7, 10)+'01'+newDate.toString().substring(11, 15);
            
        }else if (str.match('Feb')){
            intDate = newDate.toString().substring(7, 10)+'02'+newDate.toString().substring(11, 15);
        }else if (str.match('Mar')){
            intDate = newDate.toString().substring(7, 10)+'03'+newDate.toString().substring(11, 15);
        }else if (str.match('Apr')){
            intDate = newDate.toString().substring(7, 10)+'04'+newDate.toString().substring(11, 15);
        }else if (str.match('May')){
            intDate = newDate.toString().substring(7, 10)+'05'+newDate.toString().substring(11, 15);
        }else if (str.match('Jun')){
            intDate = newDate.toString().substring(7, 10)+'06'+newDate.toString().substring(11, 15);
        }else if (str.match('Jul')){
            intDate = newDate.toString().substring(7, 10)+'07'+newDate.toString().substring(11, 15);
        }else if (str.match('Aug')){
            intDate = newDate.toString().substring(7, 10)+'08'+newDate.toString().substring(11, 15);
        }else if (str.match('Sep')){
            intDate = newDate.toString().substring(7, 10)+'09'+newDate.toString().substring(11, 15);
        }else if (str.match('Oct')){
            intDate = newDate.toString().substring(7, 10)+'10'+newDate.toString().substring(11, 15);
        }else if (str.match('Nov')){
            intDate = newDate.toString().substring(7, 10)+'11'+newDate.toString().substring(11, 15);
        }else if (str.match('Dec')){
            intDate = newDate.toString().substring(7, 10)+'12'+newDate.toString().substring(11, 15);
        }
        this.setState({ chosenDate: intDate });

        
    }

    _getLocationAsync = async () => {
        let { status } = await Permissions.askAsync(Permissions.LOCATION);
        if (status !== 'granted') {
            this.setState({
                errorMessage: 'Permission to access location was denied',
            });
        }

        let location = await Location.getCurrentPositionAsync({});
        this.setState({
            location,
            longitude: location.coords.longitude,
            latitude: location.coords.latitude
        });


    };
    

    render() {
        return (
            <Container>
                <Header>
                    <Left>
                        <Button
                            transparent
                            onPress={() => this.props.navigation.goBack()}
                        >
                            <Icon name='arrow-back' />
                        </Button>
                    </Left>
                    <Body>
                        <Title>Report Crime</Title>
                    </Body>
                    <Right />
                </Header>
                <Content
                    padder style={styles.content}
                >
                    <Form>
                        <Label>Type:</Label>
                        <Picker
                            mode='dropdown' iosHeader='Select Type'
                            iosIcon={<Icon name='ios-arrow-down-outline' />}
                            selectedValue={this.state.crimeType}
                            onValueChange = {
                                this.onCrimeTypeValueChange.bind(this)
                            }
                        >
                            <Picker.Item label='Robbery' value='1' />
                            <Picker.Item label='Murder' value='2' />
                            <Picker.Item label='Assault' value='3' />
                            <Picker.Item label='Theft' value='4' />
                            <Picker.Item label='Rape' value='5' />
                            <Picker.Item label='Election Fraud' value='6' />
                        </Picker>



                        <Label>Address:</Label>
                        <Item regular>
                            <Input
                                value={this.setState.address}
                                onChangeText={address => this.setState({ address })}
                                placeholder="input crime scene location here:" />
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
                                onDateChange={this.setDate} />
                        </Item>

                        <Label>Description:</Label>
                        <Textarea
                            rowSpan={5} bordered placeholder="input crime desciption here"
                            value={this.setState.description}
                            onChangeText={description => this.setState({ description })}
                        />

                        <Label>Evidence:</Label>
                        <Button
                            small
                            style={styles.label}
                        >
                            <Text>Upload</Text>
                        </Button>
                        <Button
                            small
                            style={styles.label}
                            onPress={this._getLocationAsync.bind(this)}>
                            <Text> Use my location </Text>
                            
                        </Button>
                       
                    </Form>

                    <Button
                        style={styles.submitButton}
                        onPress={this.onSubmitButtonPress.bind(this)}>
                        <Text>Submit</Text>
                    </Button>

                </Content>
            </Container>
        );
    }
}

export default AddCrime;
