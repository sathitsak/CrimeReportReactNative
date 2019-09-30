import React, { Component } from 'react';
import { View } from 'react-native';
import { Container, Text, Header, Left, Body, Right, Button, Icon, 
    Title, Content, Footer, H3, FooterTab } from 'native-base';
import styles from './styles';

class Home extends Component {
    render() {
        return (
            <Container>
                <Header>
                   
                </Header>
                <Content padder>
                    <Text>Default page</Text>            
                </Content>   
                <Footer style={styles.footer}>
                <FooterTab>
                     <Button 
                                               
                        onPress={() => this.props.navigation.navigate('AddCrime')}
                      >
                     <Text>Report crime</Text>
                     </Button>
                     <Button>
                     <Text>View crime</Text>
                     </Button>
                     <Button>
                     <Text>Crime stat</Text>
                     </Button>
                     <Button >
                     <Text>Account</Text>
                     </Button>


                     
                     </FooterTab>
                </Footer>             
                
            </Container>
        );
    }
}

export default Home;
