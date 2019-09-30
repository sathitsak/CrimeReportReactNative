const express = require('express');
const router = express.Router();
const LEAcontroller = require("../controllers/LEAController");
const reportController = require('../controllers/reportController.js');
const chiefController = require('../controllers/chiefController.js');
const officerController = require('../controllers/officerController.js');

router.get('/', (req, res) => {
    res.redirect('/login');
});

router.get('/login', LEAcontroller.login);

router.post('/login', LEAcontroller.getLEA);

router.get('/dashboard', reportController.allReports);

router.get('/dashboard/:id', reportController.getReport);

router.get('/logout', LEAcontroller.logout);

router.post('/dashboard/:id/assign', chiefController.assign);

router.post('/dashboard/:id/resolve', officerController.resolve);

router.post('/archive/:id/reopen', LEAcontroller.reopen);

router.get('/archive', reportController.archived);

router.get('/archive/:id', reportController.getReport);

module.exports = router;