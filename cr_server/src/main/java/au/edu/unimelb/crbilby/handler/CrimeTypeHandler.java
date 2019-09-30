
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.handler;

import au.edu.unimelb.crbilby.db.tables.pojos.CrimeType;
import au.edu.unimelb.crbilby.exception.CaseNotExistsException;
import au.edu.unimelb.crbilby.exception.InvalidInputException;
import au.edu.unimelb.crbilby.helper.*;
import au.edu.unimelb.crbilby.json.JAssign;
import au.edu.unimelb.crbilby.json.JCrimeType;
import au.edu.unimelb.crbilby.util.RequestMapper;
import au.edu.unimelb.crbilby.util.ResponseBuilder;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class CrimeTypeHandler
{
    private LambdaLogger logger;

    /**
     * path: /crimetype
     */
    public void create(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            RequestMapper mapper = RequestMapper.map(input);
            String userName = mapper.authorize(this.getClass().getCanonicalName() + ";create");

            JCrimeType request = mapper.getBody(JCrimeType.class);
            int id = CrimeTypeHelper.create(request);

            ResponseBuilder.build(output)
                    .body(request)
                    .respond();

            logger.log("New crime type created with id: " + id + " by user: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /crimetype/all
     */
    public void findAll(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            RequestMapper mapper = RequestMapper.map(input);
            List<JCrimeType> response = CrimeTypeHelper.findAll();

            ResponseBuilder.build(output)
                    .body(response)
                    .respond();

            logger.log("All crime types were fetched");
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /crimetype
     */
    public void findById(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            RequestMapper mapper = RequestMapper.map(input).containsQuery("id");
            int id = Integer.parseInt(mapper.getQueryString("id"));

            JCrimeType response = CrimeTypeHelper.findById(id);

            ResponseBuilder.build(output)
                    .body(response)
                    .respond();

            logger.log("Crime type retrieved with id: " + id);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }
}
