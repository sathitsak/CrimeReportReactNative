import React from 'react';
import { Root } from 'native-base';
import { createStackNavigator, createDrawerNavigator } from 'react-navigation';
import DrawerPage from './drawer';

import Home from './home/';
import AddCrime from './addCrime';
import Settings from './settings';
import About from './about';

const Drawer = createDrawerNavigator(
    {
        Home: { screen: Home },
        Settings: { screen: Settings },
        About: { screen: About }
    },
    {
        initialRouteName: 'Home',
        contentOptions: {
            activeTintColor: '#e91e63' // TODO - Change color
        },
        contentComponent: props => <DrawerPage {...props} />
    }
);

const Stack = createStackNavigator(
    {
        Drawer: { screen: Drawer },
        Home: { screen: Home },
        AddCrime: { screen: AddCrime },
        Settings: { screen: Settings },
        About: { screen: About }
    },
    {
        initialRouteName: 'Drawer',
        headerMode: 'none'
    }
);

export default () =>
    <Root>
        <Stack />
    </Root>;
