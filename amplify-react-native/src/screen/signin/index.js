import React, { Component } from "react";
import axios from "axios";
import {
  Container,
  Text,
  Header,
  Button,
  Content,
  Form,
  Item,
  Input
} from "native-base";
import styles from "./styles";
import { AsyncStorage } from "react-native";
class Signin extends Component {
  constructor(props) {
    super(props);
    this.state = {
      ID: "null",
      PASSWORD: "null",
      AccessToken: "",
      storedValue: null,
      LoginResult: null
    };
  }
  async componentDidMount() {
    let storedValue = await AsyncStorage.getItem("ID");
    let storedPASSWORD = await AsyncStorage.getItem("PASSWORD");

    console.log("Fetched data: ", storedValue);
    console.log("Fetched PASSWORD: ", storedPASSWORD);
  }
  _retrieveData = async () => {
    try {
      const value = await AsyncStorage.getItem("@MySuperStore:key");
      this.setState({
        storedValue: value
      });
      if (value !== null) {
        // We have data!!
        //console.log('VALUE'+value);
      }
    } catch (error) {
      // Error retrieving data
    }
  };
  _storeData = async () => {
    const { AccessToken, ID, PASSWORD } = this.state;
    //console.log('ACESSTOKEN' + AccessToken);
    try {
      await AsyncStorage.setItem("@MySuperStore:key", AccessToken);
      await AsyncStorage.setItem("ID", ID);
      await AsyncStorage.setItem("PASSWORD", PASSWORD);
    } catch (error) {
      // Error saving data
    }
  };
 
  onSubmitButtonPress() {
    const { ID, PASSWORD } = this.state;
    axios({
      method: "post",
      url:
        "https://j5d06mrage.execute-api.ap-southeast-2.amazonaws.com/test/auth/signin",
      data: {
        userName: ID,
        password: PASSWORD
      }
    })
      .then(
        function() {
          this._storeData().then(this.props.navigation.navigate("Home"));

          console.log("Login PASS");
          this.setState({
            LoginResult: "PASS"
          });
        }.bind(this)
      )
      .catch(
        function() {
          console.log("Login FAIL");
          this.setState({
            LoginResult: "FAIL"
          });
        }.bind(this)
      );

    //console.log(AccessToken);
  }
  render() {
    const { LoginResult } = this.state;
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
              <Input
                placeholder="Password"
                value={this.setState.PASSWORD}
                onChangeText={PASSWORD => this.setState({ PASSWORD })}
              />
            </Item>

            <Button
              style={styles.submitButton}
              onPress={this.onSubmitButtonPress.bind(this)}
            >
              <Text>Submit</Text>
            </Button>
          </Form>
          <Text>This is Signin Status, '{LoginResult}'</Text>
        </Content>
      </Container>
    );
  }
}

export default Signin;
