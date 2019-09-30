import React, { Component } from 'react';
import { Image } from 'react-native';
import { Container, Content, List, ListItem, Left, Right, Text, Icon } from 'native-base';
import styles from './styles';

const screens = [
    {
        name: 'Home',
        route: 'Home',
        icon: 'home',
        color: '#C5F442'
    },
    //TODO - Change 'Home' background color
    {
        name: 'Settings',
        route: 'Settings',
        icon: 'settings',
        color: '#C5F442'
    },
    {
        name: 'About',
        route: 'About',
        icon: 'person',
        color: '#C5F442'
    }
];

// TODO assign an asset to these images
const drawerCover = require('../../../assets/drawer-cover.png');
const drawerImage = require('../../../assets/Logo.png');

class DrawerPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            shadowOffsetWidth: 1,
            shadowRadius: 4
        };
    }

    render() {
        return (
            <Container>
                <Content
                    bounces={false}
                    style={styles.content}
                >
                <Image source={drawerCover} style={styles.drawerCover} />
                <Image square source={drawerImage} style={styles.drawerImage} />

                <List
                    dataArray={screens}
                    renderRow={data =>
                        <ListItem
                            button
                            noBorder
                            onPress={() => this.props.navigation.navigate(data.route)}
                        >
                            <Left>
                                <Icon
                                    active
                                    name={data.icon}
                                    style={styles.itemIcon}
                                />
                                <Text style={styles.text}>{data.name}</Text>
                            </Left>                            
                        </ListItem>
                    }
                />                
                </Content>
            </Container>
        );
    }
}

export default DrawerPage;
