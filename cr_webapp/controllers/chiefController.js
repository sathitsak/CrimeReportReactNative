// Controller for all LEA chief only privledges

const axios = require('axios');
const url = require('../models/credentials.json').url;

// Allows chief to assign case report to an LEA officer
// req: assignee (officer id), report id
// res: refresh report info page with assignee linked to report
let assign = function(req, res){
    let reportid = parseInt(req.params.id);
    let assignee = req.body.assignee;
    let token = req.session.accessToken;
    let credentials = { access_token : token };
    let body = { caseID : reportid, userName : assignee };
    
    // Post request assigning case to officer
    axios.all([axios({ method: 'POST', url: `${url}test/case/chief/assign`, 
    headers: credentials, data: body }), reportid])
    // On success
    .then(axios.spread((response, id) => {
        res.redirect(`/dashboard/${id}`);
    }))
    // On failure
    .catch(err => {
        res.status(err.status).render('report', {
            report: report,
            title: `report #${id}`,
            assignee: assignee,
            status: err.status,
            error: err.response.data.message,
            role: role
        });
    })
}

module.exports.assign = assign;


    