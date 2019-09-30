
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.handler;

import au.edu.unimelb.crbilby.helper.CaseCommentHelper;
import au.edu.unimelb.crbilby.helper.ProfileHelper;
import au.edu.unimelb.crbilby.json.JComment;
import au.edu.unimelb.crbilby.json.JUpdateProfile;
import au.edu.unimelb.crbilby.util.RequestMapper;
import au.edu.unimelb.crbilby.util.ResponseBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class CaseCommentHandler {
    private LambdaLogger logger;

    /**
     * path: /case/comment
     */
    public void create(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            RequestMapper mapper = RequestMapper.map(input);
            String userName = mapper.authorize(this.getClass().getCanonicalName() + ";create");
            JComment request = mapper.getBody(JComment.class);
            request.userName = userName;
            CaseCommentHelper.create(request);

            ResponseBuilder.build(output)
                    .body(request)
                    .respond();

            logger.log("added new comment for case with id: " + request.caseID);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /case/comment
     */
    public void get(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            RequestMapper mapper = RequestMapper.map(input).containsQuery("id");
            String userName = mapper.authorize(this.getClass().getCanonicalName() + ";get");
            int id = Integer.parseInt(mapper.getQueryString("id"));
            List<JComment> response =  CaseCommentHelper.getComments(id);

            ResponseBuilder.build(output)
                    .body(response)
                    .respond();

            logger.log("Retrieved comments for case with id: " + id);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }
}
