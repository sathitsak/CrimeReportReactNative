import React, { Component } from "react";
import { AsyncStorage } from "react-native";
import {
  Container,
  Text,
  Header,
  Left,
  Body,
  Right,
  Button,
  Title,
  Content,
  Footer,
  FooterTab,
  
} from "native-base";

import styles from "./styles";
import axios from "axios";
class Home extends Component {
  constructor() {
    super();
    this.state = {
      storedValue: null
    };
  }
  //Connect to server and ask for credential when load the screen
  async componentDidMount() {
    this.forceUpdate();
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
          AsyncStorage.setItem("accessToken", response.data.accessToken);
          console.log("response from home :" + response.data.accessToken);
          this.setState({
            storedValue: response.data.accessToken
          });
        }.bind(this)
      )
      .catch(function(error) {}.bind(this));
  }
  _storeData = async () => {
    console.log("HOME TOKEN");
    try {
      await AsyncStorage.setItem("@MySuperStore:key", "null");
      this.props.navigation.navigate("AddCrime");
    } catch (error) {
      // Error saving data
    }
  };

  render() {
    return (
      <Container>
        <Header>
          <Left />
          <Body>
            <Title>Crime Bilby</Title>
          </Body>
          <Right />
        </Header>

        <Content />

        <Footer style={styles.footer}>
          <FooterTab>
            <Button onPress={this._storeData.bind(this)}>
              <Text>Report Crime</Text>
            </Button>

            <Button onPress={() => this.props.navigation.navigate("Account")}>
              <Text>ACCOUNT</Text>
            </Button>
          </FooterTab>
        </Footer>
      </Container>
    );
  }
}

export default Home;
