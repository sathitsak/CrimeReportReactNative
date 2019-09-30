// Controller that holds all methods that deal with retrieving reports

const axios = require('axios');
const url = require('../models/credentials.json').url;

// Gets all reports that are assigned to that officer, or all reports assigned to a post for
// a chief
// req: access_token, LEA role.
// res: dashboard filled with reports
let allReports = function (req, res) {

    let token = req.session.accessToken;
    let credentials = { access_token: token };
    let header = { headers: credentials };
    // console.log(req.session);
    axios.all([
        axios.get(`${url}test/case/report/all`,
            header),
        axios.get(`${url}test/crimetype/all`)
    ])
        .then(axios.spread((reports, crimes) => {
            let role = req.session.role;
            let reportdata = reports.data.cases;
            let crimedata = crimes.data;

            // Append the name of crime to the integer value in the report
            req.session.crimes = JSON.stringify(crimedata);
            // console.log(reports);
            for (i = 0; i < reportdata.length; i++) {
                reportdata[i].date = new Date(reportdata[i].date);
                if (reportdata[i].case_report.type) {
                    reportdata[i].case_report.type = crimedata[reportdata[i].case_report.type - 1].type;
                }
            }

            // On success
            res.status(200).render('dashboard',
                {
                    reports: reportdata,
                    title: "Dashboard",
                    status: 200,
                    role: role
                });

        }))
        // On fail
        .catch(err => {
            console.log(err);
            let error = err.response.message;
            let status = err.response.status;
            res.render('dashboard', {
                error: error,
                title: "Something went wrong!",
                status: status
            });
        });
}


// Gets report by ID and displays all relevant information about the case
// req: access_token, report id, LEA role
// res: a list of all open cases assigned to officer or chief's post
let getReport = function (req, res) {

    let token = req.session.accessToken;
    let credentials = { access_token: token };
    const header = { headers: credentials };
    let id = req.params.id;

    // get report by id
    axios.get(`${url}test/case/report?id=${id}`, header)
        .then(response => {
            let crimes = JSON.parse(req.session.crimes);
            let report = response.data;
            id = report.case_report.id;
            // console.log(report);
           
            // if report has crime type append crime name to integer value in report
            if (report.case_report.type) {
                report.case_report.type = crimes[report.case_report.type - 1].type;
            }
            
            // if report is already assigned retrieve to whom it is assigned
            if (report.is_assigned && req.session.role == 3) {
                return axios.all([axios.get(`${url}test/case/chief/assign?id=${id}`, header),
                    report, id]);

            } else {

                // if report is not assigned get all assignable officers under chief's post
                return axios.all([axios.get(`${url}test/case/chief/lea`, header), report, id])
            }
        })
        // On success
        .then(axios.spread((assignee, report, id) => {
            let role = req.session.role;
            // console.log(assignee.data)


            // if report is assigned render whom it is assigned to
            if (report.is_assigned > 0) {

                assignee = assignee.data.userName;
                res.render('report', {
                    report: report,
                    title: `report #${id}`,
                    assignee: assignee,
                    status: 200,
                    role: role
                });
            // if report is not assigned render all assignable officers
            } else {
                assignee = assignee.data;

                res.render('report', {
                    report: report,
                    title: `report #${id}`,
                    assignee: assignee,
                    status: 200,
                    role: role
                });
            }


        }))
        // On failure render error message
        .catch(err => {

            console.log(err);
            let error = err.response.message;
            let status = err.response.status;
            res.render('dashboard', {
                error: error,
                title: "Something went wrong!",
                status: status
            });
        })
}

// retrieve all resolved cases assigned to officer or chief's post
// req: access_token, LEA role
// res: a list of all archieved/resolved cases
let archived = function (req, res) {
    let token = req.session.accessToken;
    let credentials = { access_token: token };
    let header = { headers: credentials };

    // get all archieved cases assigned to that officer or chief's post
    axios.get(`${url}test/case/archive`, header)
        // on success
        .then(archive => {
            let role = req.session.role;
            archive = archive.data.cases;
            // console.log(archive);
            res.render('dashboard', {
                reports: archive,
                title: "Archive",
                status: 200,
                role: role
            });
        })
        // on failure
        .catch(err => {
            console.log(err);
            let error = err.response.message;
            let status = err.response.status;
            res.render('dashboard', {
                error: error,
                title: "Something went wrong!",
                status: status
            })
        });
}

module.exports.archived = archived;
module.exports.allReports = allReports;
module.exports.getReport = getReport;
