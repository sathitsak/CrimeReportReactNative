import React, { Component } from "react";
import { SafeAreaView, AsyncStorage } from "react-native";
import { Location, Permissions } from "expo";
import Map from "./Map";
import axios from "axios";

const deltas = {
  latitudeDelta: 0.0922,
  longitudeDelta: 0.0421
};

class CrimeStat extends Component {
  state = {
    region: null,
    crimelocation: []
  };
  //Get location of the user when loading the screen
  componentWillMount() {
    this.getLocationAsync();
  }
  //Get location of the user

  getLocationAsync = async () => {
    let { status } = await Permissions.askAsync(Permissions.LOCATION);
    if (status !== "granted") {
      this.setState({
        errorMessage: "Permission to access location was denied"
      });
    }

    let location = await Location.getCurrentPositionAsync({});
    let storedValue = await AsyncStorage.getItem("accessToken");
    console.log("AccessToken in crime stat:" + storedValue);
    const region = {
      latitude: location.coords.latitude,
      longitude: location.coords.longitude,
      ...deltas
    };
    await this.setState({
      region
    });
    //Load location from server
    axios({
      headers: {
        access_token: storedValue
      },
      method: "get",
      url:
        "https://j5d06mrage.execute-api.ap-southeast-2.amazonaws.com/test/case/statistics",
      data: {}
    })
      .then(
        function(response) {
          console.log(response.data.locations);
          const place = response.data.locations.map(locations => {
            return {
              name: "crime",
              coords: locations
            };
          });
          console.log(place);
          const crimelocation = place;
          this.setState({ crimelocation });
        }.bind(this)
      )
      .catch(function(error) {
        console.log(error);
      });
  };

  render() {
    const { region, crimelocation } = this.state;
    return (
      <SafeAreaView style={styles.container}>
        <Map region={region} places={crimelocation} />
      </SafeAreaView>
    );
  }
}

const styles = {
  container: {
    width: "100%",
    height: "125%"
  }
};
export default CrimeStat;
