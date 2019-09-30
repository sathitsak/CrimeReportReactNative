import React, { Component } from 'react';
import { View } from 'react-native';
import axios from 'axios';
import { Container, Text, Header, Left, Body, Right, Button, Icon, 
    Title, Content, Footer, H3, FooterTab, Form,Item,Input } from 'native-base';
import styles from './styles';

class Signin extends Component {
     constructor(props) {
         super(props);
         this.state = {
             FIRSTNAME: 'null',
             LASTNAME: 'null',
             PHONE: 'null'           
         };
         
     }
     onSubmitButtonPress() {

        const {  FIRSTNAME, LASTNAME,PHONE  } = this.state;
        axios({
                method: 'post',
                url: 'https://6jpu65jou0.execute-api.ap-southeast-2.amazonaws.com/test/auth/signin',
                data: {
                   
                    "firstName": FIRSTNAME,
                    "lastName": LASTNAME,
                    "phone": PHONE
                   
                }
            }).then(function (response) {
                console.log(response);
            })
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

export default Signin;
