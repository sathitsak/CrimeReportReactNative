const express = require('express');
const app = express();
const path = require('path');
const bodyParser = require('body-parser');
const session = require('express-session');
const flash = require('connect-flash');

// set static folder
app.use(express.static(__dirname + '/public/'));

/*initialise session
* ::TO DO::
* create https server and set cookie to secure
*/

app.use(session({
    secret: 's3cr3t',
    resave: false,
    saveUninitialized: true,
    cookie: { secure: false }
}))

// initialise flash for future flash messages
app.use(flash());

// set views folder and templating to ejs
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// set body parser to global
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())


const index = require('./routes/index.js');
app.use('/', index);

// listen on port 3000 or environment config port
app.set('port', (process.env.PORT || 3000))
app.listen(app.get('port'), function(){
    console.log('express serving at port ' + app.get('port'));
});

// export for testing purposes
module.exports = app;