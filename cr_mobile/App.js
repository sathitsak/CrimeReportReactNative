import React, { Component } from 'react';
import { StyleProvider } from 'native-base';
import Main from './src/screen/Main';
import getTheme from './src/native-base-theme/components';
import variables from './src/native-base-theme/variables/commonColor';

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { loading: true };
  }

  async componentWillMount() {
    await Expo.Font.loadAsync({
      'Roboto': require('native-base/Fonts/Roboto.ttf'),
      'Roboto_medium': require('native-base/Fonts/Roboto_medium.ttf'),
    });
    this.setState({ loading: false });
  }
  
  render() {
    if (this.state.loading) {
      return <Expo.AppLoading />;}
    return(
	<StyleProvider style={getTheme(variables)}>
        <Main />
      </StyleProvider>);
  }
}

