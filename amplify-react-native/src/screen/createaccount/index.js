import React, { Component } from 'react';
import { View } from 'react-native';
import axios from 'axios';
import { Container, Text, Header, Left, Body, Right, Button, Icon, 
    Title, Content, Footer, H3, FooterTab, Form,Item,Input } from 'native-base';
import styles from './styles';

class Createaccount extends Component {
     constructor(props) {
         super(props);
         this.state = {
             ID: 'null',
             PASSWORD: 'null',
             FIRSTNAME: 'null',
             LASTNAME: 'null',
             PHONE: 'null'
            
         };
         
     }
     onSubmitButtonPress() {

        const { ID, PASSWORD, FIRSTNAME, LASTNAME,PHONE } = this.state;
        axios({
                method: 'post',
                url: 'https://j5d06mrage.execute-api.ap-southeast-2.amazonaws.com/test/auth/signup',
                data: {
                    "userName": ID,
                    "password": PASSWORD,
                    "firstName": FIRSTNAME,
                    "lastName": LASTNAME,
                    "phone": PHONE
                   
                }
        }).then(this.props.navigation.navigate('Home'))
            .catch(function (error) {
                console.log(error);
            });
     }
    render() {
        return (
            <Container>
                <Header />
                  <Content>
                     <Form>
                      <Item>
                      <Input 
                      placeholder="Username"
                      value={this.setState.ID}
                        onChangeText={ID => this.setState({ ID })}
                       />
                     </Item>
                     <Item>
                       <Input placeholder="Password" 
                       value={this.setState.PASSWORD}
                        onChangeText={PASSWORD => this.setState({ PASSWORD })}
                       />
                      </Item>
                     < Item >
                       <Input 
                      placeholder="First name"
                      value={this.setState.FIRSTNAME}
                        onChangeText={FIRSTNAME => this.setState({ FIRSTNAME })}
                       />
                       </Item >
                       < Item >
                        <Input 
                      placeholder="Last name"
                      value={this.setState.LASTNAME}
                        onChangeText={LASTNAME => this.setState({ LASTNAME })}
                       />
                       </Item>
                       < Item >
                        <Input 
                      placeholder="Phone number"
                      value={this.setState.PHONE}
                        onChangeText={PHONE => this.setState({ PHONE })}
                       />
                       </Item>
                       <Button
                        style={styles.submitButton}
                        onPress={this.onSubmitButtonPress.bind(this)}>
                        <Text>Submit</Text>
                    </Button>
                     </Form>
                    </Content>  
                  
                
            </Container>
        );
    }
}

export default Createaccount;
