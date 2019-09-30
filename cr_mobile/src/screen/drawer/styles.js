const React = require('react-native');
const { Platform, Dimensions } = React;

const deviceHeight = Dimensions.get('window').height;
const deviceWidth = Dimensions.get('window').width;

export default {
    drawerCover: {
        alignSelf: 'stretch',
        height: deviceHeight / 3.5,
        width: null,
        position: 'relative',
        marginBottom: 10
      },
      drawerImage: {
        position: 'absolute',
        left: Platform.OS === 'android' ? deviceWidth / 15 : deviceWidth / 14,
        top: Platform.OS === 'android' ? deviceHeight / 10 : deviceHeight / 8,
        width: 230,
        height: 75,
        resizeMode: 'cover'
      },
    content: {
        flex: 1,
        backgroundColor: '#fff', 
        top: -1
    },
    itemIcon: {
        color: '#777',
        fontSize: 26,
        width: 30
    },
    text: {
        fontWeight: Platform.OS === 'ios' ? '500' : '400',
        fontSize: 16,
        marginLeft: 20
    }
};
