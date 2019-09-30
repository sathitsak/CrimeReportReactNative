const expect = require('chai').expect;
const app = require('../app');
const request = require('supertest');

const userCredentialsChief1 = {
    username: 'chief1',
    password: '123456'
}

const userCredentialsOfficer1 = {
    username: 'lea1',
    password: '123456'
}

// this case must be in chief1's open cases (dashboard)
var openReport = 405;
// this case must be in chief1's closed cases (archive)
var closedReport = 406;

var authenticatedUser = request.agent(app);



describe('happy path tests for LEA chief', function (done) {

    before(function (done) {   
        this.timeout(20000);
        console.log("before: signing in chief with valid credentials")
        authenticatedUser
            .post('/login')
            .send(userCredentialsChief1)
            .end(function (err, response) {
                expect(response.statusCode).to.equal(302);
                expect('Location', '/dashboard');
                done();
            });
    });

    it('authenticated chief recieves all reports that are assigned', function (done) {
        this.timeout(10000);
        authenticatedUser.get('/dashboard')
            .expect(200, done);
            
    });
    
    it('authenticated chief can access report assigned to his post', function (done) {
        this.timeout(20000);
        authenticatedUser.get(`/dashboard/${openReport}`)
            .expect(200, done);
            
    });


    it('authenticated chief can access report assigned to his post and assign it to an LEA officer', function (done) {
        this.timeout(20000);
        authenticatedUser.post(`/dashboard/${openReport}/assign`).send({assignee : 'lea1'})
        .end(function(err, response) {
            expect(response.statusCode).to.equal(302);
            expect('Location', '/dashboard/310');
            done();
        })
            
    });

    it('authenticated chief can access archived reports', function (done) {
        this.timeout(20000);
        authenticatedUser.get('/archive')
            .expect(200, done);
            
    });

    it('authenticated chief can access reopen archived cases', function (done) {
        this.timeout(20000);
        authenticatedUser.post(`/archive/${closedReport}/reopen`)
            .expect(302, done);
            
    });

    it('logout of chief1 account', function (done) {
        this.timeout(20000);
        authenticatedUser.get('/logout')
            .expect(302, done);
            
    });
    
});



describe('happy path tests for LEA officer', function (done) {

    before(function (done) {   
        this.timeout(20000);
        console.log("before: signing in officer with valid credentials")
        authenticatedUser
            .post('/login')
            .send(userCredentialsOfficer1)
            .end(function (err, response) {
                expect(response.statusCode).to.equal(302);
                expect('Location', '/dashboard');
                done();
            });
    });

    it('authenticated officer recieves all reports that are assigned to him/her', function (done) {
        this.timeout(20000);
        authenticatedUser.get('/dashboard')
            .expect(200, done);
            
    });

    it('authenticated officer view report details and resolve report', function (done) {
        this.timeout(20000);
        authenticatedUser.post(`/dashboard/${openReport}/resolve`)
            .expect(302, done);
            
    });

    it('authenticated officer view archived report and reopen it', function (done) {
        this.timeout(20000);
        authenticatedUser.post(`/archive/${openReport}/reopen`)
            .expect(302, done);
            
    });

    it('logout of lea1 account', function (done) {
        this.timeout(20000);
        authenticatedUser.get('/logout')
            .expect(302, done);
            
    });
})