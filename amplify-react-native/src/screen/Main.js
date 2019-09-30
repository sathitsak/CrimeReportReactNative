import React from 'react';
import { Root } from 'native-base';
import { createStackNavigator, createDrawerNavigator } from 'react-navigation';
import DrawerPage from './drawer';

import Home from './home/';
import AddCrime from './addCrime';
import Account from './account';
import Settings from './settings';
import About from './about';
import Createaccount from './createaccount';
import Signin from './signin';
import Aws from './aws';
import CrimeStat from './crimestat';


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
        Account:{screen: Account },
        Createaccount:{screen: Createaccount},
        Signin:{screen: Signin},
        Settings: { screen: Settings },
        About: { screen: About },
        Aws: {screen: Aws},
        CrimeStat : {screen: CrimeStat}
        
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
