
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.util;

import au.edu.unimelb.crbilby.db.tables.daos.ProfileDao;
import au.edu.unimelb.crbilby.exception.InternalErrorException;
import au.edu.unimelb.crbilby.exception.InvalidInputException;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.GetUserRequest;
import com.amazonaws.services.cognitoidp.model.GetUserResult;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

public class RequestMapper {
    private static Properties authProp = Util.getAuthSettings();
    private BufferedReader reader;
    private final Gson gson;
    private JSONObject request;
    private JSONParser parser;

    private RequestMapper(InputStream input) throws IOException{
        reader = new BufferedReader(new InputStreamReader(input));
        gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimeStampAdapter()).create();
        parser = new JSONParser();
        try {
            request = (JSONObject) parser.parse(reader);
        } catch (ParseException e) {
            throw new InvalidInputException("Cannot parse request");
        }
    }

    public static RequestMapper map(InputStream input) throws IOException{
       return new RequestMapper(input);
    }

    /**
     * Authorise the current user with the specified lambda function.
     * @param lambda lambda function name
     * @return User Name
     * @throws NotAuthorizedException if user is not authorised
     */
    public String authorize(String lambda) throws NotAuthorizedException {
        String value = authProp.getProperty(lambda);
        if(Util.isNullorEmpty(value))
            throw new NotAuthorizedException("User not authorised");

        AWSCognitoIdentityProvider cognito = Util.getCognitoIdProvider();
        JSONObject header = (JSONObject) getHeader();
        if(header == null)
            throw new InvalidInputException("Error parsing request. No header was provided");

        String token = (String) header.get("access_token");
        if(Util.isNullorEmpty(token))
            throw new NotAuthorizedException("User not authorised");


        GetUserRequest userRequest = new GetUserRequest();
        userRequest.setAccessToken(token);
        GetUserResult result =  cognito.getUser(userRequest);
        String userName = result.getUsername();

        int userRole;
        try (Connection conn = Util.createConn()) {
            ProfileDao dao = new ProfileDao(Util.createConf(conn));
            userRole = dao.findById(userName).getRole();
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }

        int level = Integer.parseInt(value);
        if(userRole < level)
            throw new NotAuthorizedException("User not authorised");

        return userName;
    }

    /**
     * Authorise user and treats it optional (Does not thrown exception if not authorised)
     * @param lambda Lambda function to authorise
     * @return User Name if authorised, null if not
     */
    public String optionalAuthorize(String lambda) {
        try {
            return authorize(lambda);
        } catch (NotAuthorizedException e) {
            return null;
        }
    }

    /**
     * Ensure the existance of a given query string within request
     * @param query Query String to check
     * @return RequestMapper as a builder pattern
     * @throws InvalidInputException If query string was not found
     */
    public RequestMapper containsQuery(String query) throws InvalidInputException {
        if (getQuery().get(query) == null)
            throw new InvalidInputException("Expected query string: " + query);

        return this;
    }

    /**
     * Retrieve value of a query String
     * @param query name of query
     * @return
     */
    public String getQueryString(String query){
        return getQuery().get(query).toString();
    }

    /**
     * Retrieve body message and marshalise it as a given class
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T getBody(Class<T> cls) {
        try {
            return gson.fromJson(getBody().toString(), cls);
        } catch (Exception e) {
            System.out.println(e);
            throw new InvalidInputException("Cannot parse request");
        }

    }

    /**
     * Retrieve HTTP Message body
     * @return
     */
    public Object getBody() {
        return request.get("body");
    }

    /**
     * Retrieve HTTP Header
     * @return
     */
    public Object getHeader() {
        return request.get("headers");
    }

    public JSONObject getRequest() {
        return request;
    }

    public JSONObject getQuery() {
        return (JSONObject) request.get("queryStringParameters");
    }
}
