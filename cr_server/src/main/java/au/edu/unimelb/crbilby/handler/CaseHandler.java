
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.handler;

import au.edu.unimelb.crbilby.helper.CaseHelper;
import au.edu.unimelb.crbilby.helper.ProfileHelper;
import au.edu.unimelb.crbilby.helper.UserRole;
import au.edu.unimelb.crbilby.json.JCase;
import au.edu.unimelb.crbilby.json.JCases;
import au.edu.unimelb.crbilby.json.JLocations;
import au.edu.unimelb.crbilby.util.RequestMapper;
import au.edu.unimelb.crbilby.util.ResponseBuilder;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CaseHandler {
    private LambdaLogger logger;

    /**
     * path: /case/report
     */
    public void create(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();
        try {
            RequestMapper mapper = RequestMapper.map(input);
            logger.log("Request: " + mapper.getRequest().toJSONString());
            String userName = mapper.optionalAuthorize(this.getClass().getCanonicalName() + ";create");

            // If its from a authorized user, make sure its a normal registered user only
            if(userName != null)
            {

                int role = ProfileHelper.getRole(userName);

                if(role != UserRole.NORMAL)
                    throw new NotAuthorizedException("User not authorized to report a crime");
            }

            JCase request = mapper.getBody(JCase.class);
            CaseHelper.create(userName, request);

            ResponseBuilder.build(output)
                    .body("")
                    .respond();
            if(userName == null)
                logger.log("Case reported by anonymous");
            else
                logger.log("Case reported by user: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e, true);
        }
    }

    /**
     * path: /case/report
     */
    public void findCaseById(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();
        try {
            RequestMapper mapper = RequestMapper.map(input).containsQuery("id");
            String userName = mapper.authorize(this.getClass().getCanonicalName() + ";findCaseById");

            int id = Integer.parseInt(mapper.getQueryString("id"));
            JCase c = CaseHelper.findById(id);

            ResponseBuilder.build(output)
                    .body(c)
                    .respond();

            logger.log("Retrieved Case with id: " + id + " by user: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /case/statistics
     */
    public void getStatistics(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();
        try {
            RequestMapper mapper = RequestMapper.map(input);
            String userName = mapper.authorize(this.getClass().getCanonicalName() + ";getStatistics");

            JLocations response = CaseHelper.getStatistics();

            ResponseBuilder.build(output)
                    .body(response)
                    .respond();

            logger.log("Crime location statistics was served to user with id: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /case/report/all
     * <p>Retrieve cases related to user. If user is:</p>
     * <p>1. Normal: Reported crime by that user will be fetched</p>
     * <p>2. LEA: Assigned crimes to LEA user will be fetched</p>
     * <p>3. Chief: All cases related to post and cases without a post assigned will be fetched</p>.
     */
    public void getCases(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();
        try {
            JCases response = null;
            RequestMapper mapper = RequestMapper.map(input);
            String userName = mapper.authorize(this.getClass().getCanonicalName() + ";getCases");
            int role = ProfileHelper.getRole(userName);

            switch (role) {
                case UserRole.NORMAL :
                    response = CaseHelper.getRegisterdUserCases(userName);
                    break;

                case UserRole.LEA:
                    response = CaseHelper.getLEACases(userName, false);
                    break;

                case UserRole.LEA_CHIEF:
                    response = CaseHelper.getChiefCases(userName, false);
                    break;

                default:
                    throw new NotAuthorizedException("User not authorised");
            }

            ResponseBuilder.build(output)
                    .body(response)
                    .respond();

            logger.log("Retrieved cases associated with user id: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /case/report/archive
     * <p>Retrieve closed cases related to user. If user is:</p>
     * <p>1. LEA: Assigned crimes to LEA user will be fetched</p>
     * <p>2. Chief: All cases related to post and cases without a post assigned will be fetched</p>
     * @param input
     * @param output
     * @param context
     * @throws IOException
     */
    public void getClosedCases(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();
        try {
            JCases response = null;
            RequestMapper mapper = RequestMapper.map(input);
            String userName = mapper.authorize(this.getClass().getCanonicalName() + ";getClosedCases");
            int role = ProfileHelper.getRole(userName);

            switch (role) {
                case UserRole.LEA:
                    response = CaseHelper.getLEACases(userName, true);
                    break;

                case UserRole.LEA_CHIEF:
                    response = CaseHelper.getChiefCases(userName, true);
                    break;

                default:
                    throw new NotAuthorizedException("User not authorised");
            }

            ResponseBuilder.build(output)
                    .body(response)
                    .respond();

            logger.log("Retrieved closed cases associated with user id: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /case/resolve
     */
    public void resolve(InputStream input, OutputStream output, Context context) throws IOException {
        setCaseStatus(input, output, context, true);
    }

    /**
     * path: /case/reopen
     */
    public void reopen(InputStream input, OutputStream output, Context context) throws IOException {
        setCaseStatus(input, output, context, false);
    }

    /**
     * Set Report case to be open or closed
     * @param close whether to close the case or not
     */
    private void setCaseStatus(InputStream input, OutputStream output, Context context,
                               boolean close) throws IOException {
        logger = context.getLogger();
        try {
            RequestMapper mapper = RequestMapper.map(input).containsQuery("id");
            String userName;

            if(close)
                userName = mapper.authorize(this.getClass().getCanonicalName() + ";resolve");
            else
                userName = mapper.authorize(this.getClass().getCanonicalName() + ";reopen");

            int caseID = Integer.parseInt(mapper.getQueryString("id"));

            CaseHelper.setCaseState(caseID, close);

            ResponseBuilder.build(output)
                    .body(mapper.getBody())
                    .respond();
            if(close)
                logger.log("Case with id: " + caseID + " was resolved by lea user: " + userName);
            else
                logger.log("Case with id: " + caseID + " was reopened by lea user: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }
}
