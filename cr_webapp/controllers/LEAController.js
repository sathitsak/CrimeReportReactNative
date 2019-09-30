// Controller for all priveledges available to all LEA users

const express = require('express');
const axios = require('axios');
const url = require('../models/credentials.json').url;


// render login page
let login = function(req, res){
    res.render('login', {
        title : 'login',
        status : 200
    });
}

// post to authentication API, recieve and store token in session, redirect to dashboard interface
let getLEA = function(req, res){
    let credentials =  { 
        userName : req.body.username,
        password : req.body.password
        }
    // make post request and save token
    axios.post(`${url}test/auth/signin`,
    credentials)
    .then( response => {
        role = response.data.role;
        let token = response.data.accessToken
        req.session.accessToken = token;
        req.session.role = role;
        res.redirect("/dashboard");
    })
    .catch(err => {
        console.log(err);
        let status = err.status;
        res.status(status).render("login", {
            error : err.response.statusMessage,
            title : "Something went wrong!",
            status : status
        });
    })
}



// Destroy session and redirect to logout
let logout = function(req, res){
    let token = req.session.accessToken;
    let credentials = { access_token : token };
    axios({ method: 'POST', url: `${url}test/auth/signout`,
    headers: credentials})
    .then(response => {
        req.session.destroy();
        res.redirect('/login');
    })
    .catch(err => {
        console.log(err);
        req.session.destroy();
        res.redirect('/login');
    })
}

// allows both chief and LEA officer to reopen resolved cases
let reopen = function(req, res){
    let token = req.session.accessToken;
    let credentials = { access_token : token };
    let reportid = parseInt(req.params.id);

    axios.all([axios({ method: 'PUT', url: `${url}test/case/reopen`,
    params : { id : reportid }, headers: credentials}), reportid])
    .then(axios.spread((response, id) => {
        // console.log(response);
        // res.flash('message', );
        res.redirect(`/dashboard/${id}`)
    }))
    .catch(err => {
        console.log(err);
    })
}

module.exports.login = login;
module.exports.getLEA = getLEA;
module.exports.logout = logout;
module.exports.reopen = reopen;
