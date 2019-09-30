
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.handler;

import au.edu.unimelb.crbilby.helper.AuthenticationHelper;
import au.edu.unimelb.crbilby.helper.ProfileHelper;
import au.edu.unimelb.crbilby.json.JSignIn;
import au.edu.unimelb.crbilby.json.JSignUp;
import au.edu.unimelb.crbilby.json.JWT;
import au.edu.unimelb.crbilby.json.JUpdateProfile;
import au.edu.unimelb.crbilby.util.RequestMapper;
import au.edu.unimelb.crbilby.util.ResponseBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.json.simple.JSONObject;

import java.io.*;

/**
 * Handles authentication services:
 * <p>1. Sign Up Normal User. See {@link au.edu.unimelb.crbilby.helper.UserRole}</p>
 * <p>2. Update normal user profile</p>
 */
public class AuthenticateHandler {

    private LambdaLogger logger;

    /**
     * path: /auth/signup
     */
    public void signUp(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            RequestMapper mapper = RequestMapper.map(input);
            JSignUp request = mapper.getBody(JSignUp.class);

            AuthenticationHelper.signUp(request);

            ResponseBuilder.build(output)
                    .body(request)
                    .respond();

            logger.log("Registered user with id: " + request.userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /auth/signin
     */
    public void signIn(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            RequestMapper mapper = RequestMapper.map(input);
            JSignIn request = mapper.getBody(JSignIn.class);
            JWT token =  AuthenticationHelper.signIn(request);

            logger.log("Access Token granted for: " + request.userName);

            ResponseBuilder.build(output)
                    .body(token)
                    .respond();

            logger.log("Access Token granted for: " + request.userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /auth/signout
     */
    public void signOut(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            RequestMapper mapper = RequestMapper.map(input);
            String userName = mapper.authorize(this.getClass().getCanonicalName() + ";signOut");
            JSONObject header = (JSONObject) mapper.getHeader();
            String token = (String) header.get("access_token");
            AuthenticationHelper.signOut(token);

            ResponseBuilder.build(output)
                    .body(mapper.getBody())
                    .respond();

            logger.log("Sign out performed for user: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }

    /**
     * path: /auth/update
     */
    public void updateProfile(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();

        try {
            RequestMapper mapper = RequestMapper.map(input);
            String userName = mapper.authorize(this.getClass().getCanonicalName() + ";updateProfile");
            JUpdateProfile request = mapper.getBody(JUpdateProfile.class);
            ProfileHelper.update(userName, request);

            ResponseBuilder.build(output)
                    .body(request)
                    .respond();

            logger.log("Updated profile with id: " + userName);
        } catch (Exception e) {
            logger.log("Error:\n" + e);
            ResponseBuilder.build(output).error(e);
        }
    }
}
