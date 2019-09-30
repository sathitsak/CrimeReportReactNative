import React, { Component } from "react";
import {
  Container,
  Text,
  Header,
  Button,
  Content} from "native-base";
import styles from "./styles";
//Account page
import { AsyncStorage } from "react-native";
//Fetch stored ID and password from persistance storage to check whether user is logged in or not
class Account extends Component {
  state = { loginState: true };
  async componentDidMount() {
    let storedID = await AsyncStorage.getItem("ID");
      let storedPASSWORD = await AsyncStorage.getItem("PASSWORD").then(this.setState({ storedID }))

    console.log("Fetched data: ", storedID);
    console.log("Fetched PASSWORD: ", storedPASSWORD);
     
  }
  //Function for user to log out
    _deleteData = async () => {
      try {
        await AsyncStorage.removeItem('ID');
        await AsyncStorage.removeItem('PASSWORD');
        await AsyncStorage.removeItem('@MySuperStore:key').then(this.props.navigation.navigate('Home'));
        return true;
      }
      catch (exception) {
        return false;
      }
    }
   // If user is already sign in show crimestat and logout button, otherwise show sign and create account button
  renderSignin() {
      const { storedID } = this.state;
    if (storedID == null) {
      //this.setState({ loginState: false});
     // console.log("login state" + loginState);
        return ( 
        <React.Fragment>
            <Button
                style={styles.button}
                onPress={() => this.props.navigation.navigate("Createaccount")}
            >
                <Text>create account</Text>
            </Button>

            <Button
                style={styles.button}
                onPress={() => this.props.navigation.navigate("Signin")}
            >
                <Text>sign in</Text>
            </Button>
        </React.Fragment>
        );
    } else {
        return <React.Fragment>
            <Button style={styles.button} onPress={() => this.props.navigation.navigate("CrimeStat")}>
              <Text>Crime stat</Text>
            </Button>
            <Button style={styles.button} onPress={this._deleteData.bind(this)}>
              <Text>Log out</Text>
            </Button>
          </React.Fragment>;
    }
  }
  render() {
    return (
      <Container>
        <Header />
        <Content>
            
              {this.renderSignin()}  
          
        </Content>
      </Container>
    );
  }
}

export default Account;
