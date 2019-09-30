import React, { Component } from 'react';
import { Container, Text, Header, Left, Body, Right, Button, Icon, 
    Title, Content, Footer } from 'native-base';

class About extends Component {
    render() {
        return (
            <Container>
                <Header>
                    <Left>
                        <Button 
                            transparent 
                            onPress={() => this.props.navigation.openDrawer()} 
                        >
                            <Icon name='menu' />   
                        </Button>
                    </Left>
                    <Body>
                        <Title>About</Title>
                    </Body>
                    <Right />
                </Header>
                <Content padder>
                    <Text>Contents goes here</Text>
                </Content>   
                <Footer>
                     {/* Footer goes here */}
                </Footer>                
            </Container>
        );
    }
}

export default About;
