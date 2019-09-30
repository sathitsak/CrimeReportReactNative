
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.handler;

import au.edu.unimelb.crbilby.helper.ChiefHelper;
import au.edu.unimelb.crbilby.helper.CrimeTypeHelper;
import au.edu.unimelb.crbilby.helper.ProfileHelper;
import au.edu.unimelb.crbilby.helper.UserRole;
import au.edu.unimelb.crbilby.json.JAssign;
import au.edu.unimelb.crbilby.json.JAssigned;
import au.edu.unimelb.crbilby.json.JCrimeType;
import au.edu.unimelb.crbilby.util.RequestMapper;
import au.edu.unimelb.crbilby.util.ResponseBuilder;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ChiefHandler {

    private LambdaLogger logger;

    /**
     * path: /case/chief
     */
    public void assign(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            RequestMapper mapper = RequestMapper.map(input);
            String userName = mapper.authorize(this.getClass().getCanonicalName() + ";assign");
            int role = ProfileHelper.getRole(userName);

            if(role != UserRole.LEA_CHIEF)
                throw new NotAuthorizedException("User not authorised");

            JAssign request = mapper.getBody(JAssign.class);
            ChiefHelper.assign(userName, request);

            ResponseBuilder.build(output)
                    .body(request)
                    .respond();

            logger.log("Updated profile with id: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /case/chief/assign
     */
    public void assigned(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            RequestMapper mapper = RequestMapper.map(input).containsQuery("id");
            String userName = mapper.authorize(this.getClass().getCanonicalName() + ";assigned");
            int id = Integer.parseInt(mapper.getQueryString("id"));

            JAssigned response = ChiefHelper.assignedTo(id);

            ResponseBuilder.build(output)
                    .body(response)
                    .respond();

            logger.log("Case Assigned profile request was fetched for case id: "
                    + id + " by user: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /case/chief/lea
     */
    public void getLEA(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            logger.log("Before mapper");
            RequestMapper mapper = RequestMapper.map(input);
            logger.log("Before auth");
            //String userName = mapper.authorize(this.getClass().getCanonicalName() + ";getLEA");
            String userName="chief1";
            logger.log("Before call");
            List<String> response = ChiefHelper.getLEA(userName);
            logger.log("After call");


            ResponseBuilder.build(output)
                    .body(response)
                    .respond();

            logger.log("Post LEAs were fetched by user: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }
}
