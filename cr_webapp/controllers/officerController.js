const axios = require('axios');
const url = require('../models/credentials.json').url;

// Allows officer to close cases
// req: access_token, id of report wished to be resolved
let resolve = function(req, res){
    let token = req.session.accessToken;
    let credentials = { access_token: token };
    let reportid = parseInt(req.params.id);

    axios.all([axios({ method: 'PUT', url: `${url}test/case/resolve`,
    params : { id : reportid }, headers: credentials}), reportid])
    // On success
    .then(axios.spread((response, id) => {
        // console.log(response);
        res.redirect("/dashboard")
    }))
    // On fail
    .catch(err => {
        res.status(err.status).render('report', {
            report: report,
            title: `report #${id}`,
            assignee: assignee,
            status: err.status,
            role: role
        });
    })
}

module.exports.resolve = resolve;

