
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds a configurable Http body
 */
public class ResponseBuilder {
    public static final int DEFAULT_STATUS = 200;
    private static Gson gson = new GsonBuilder().create();

    private OutputStreamWriter writer;
    private JSONObject headers;
    private JSONObject response;
    private JSONObject error;
    private String body;
    private int status;

    // Private access
    private ResponseBuilder(OutputStream output) throws UnsupportedEncodingException {
        writer = new OutputStreamWriter(output, "UTF-8");
        headers = new JSONObject();
        response = new JSONObject();
        error = new JSONObject();
        body = "";
        status = DEFAULT_STATUS;
    }

    /**
     * Starts building the body
     * @param output Output stream to write information to
     * @return ResponseBuilder object
     */
    public static ResponseBuilder build(OutputStream output) throws UnsupportedEncodingException {
        return new ResponseBuilder(output);
    }

    /**
     * Sends a respond based on build settings given. Output is closed after that
     */
    public void respond() throws IOException {
        response.put("isBase64Encoded", false);
        response.put("statusCode", status);
        response.put("headers", headers);
        response.put("body", body);
        writer.write(response.toJSONString());

        writer.close();
    }

    /**
     * Adds body message to the body
     * @param content content of the message to send
     * @return
     */
    public ResponseBuilder body(String content) {
        body = content;

        return this;
    }

    /**
     * Adds body message to the body. The given object will be marshalised to json
     * @param content content of the message to send
     * @return
     */
    public ResponseBuilder body(Object content) {
        body = gson.toJson(content);

        return this;
    }

    /**
     * Adds a key-value pair to the headers of the body
     * @param key
     * @param value
     * @return
     */
    public ResponseBuilder addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    /**
     * Treats a body as an error by setting status code to 400
     * and respond accordingly
     * @return
     */
    public void error(Exception e) throws IOException {
        error(e, false);
    }

    /**
     * Treats a body as an error by setting status code to 400
     * and respond accordingly. Allows to output trace for debug purposes if requested
     * @return
     */
    public void error(Exception e, boolean withTrace) throws IOException {
        status = 400;
        error.put("errorMessage", e.getMessage());
        error.put("errorType", e.getClass().getCanonicalName());

        if(withTrace)
        {
            List<String> errorStack = new ArrayList<>();
            for(StackTraceElement s: e.getStackTrace())
                errorStack.add(s.toString());

            error.put("stackTrace", errorStack);
        }
        body = error.toString();
        respond();

//        response.put("isBase64Encoded", false);
//        response.put("statusCode", status);
//        response.put("headers", headers);
//        response.put("body", error);
//        writer.write(response.toJSONString());
//
//        writer.close();
    }

    /**
     * Set a custom status code. The default is {@value DEFAULT_STATUS}
     * @param code status code
     * @return
     */
    public ResponseBuilder status(int code) {
        status = code;

        return this;
    }

    /**
     * Mirror a respond as given
     * @param response JSON Object to respond
     * @throws IOException
     */
    public void mirror(JSONObject response) throws IOException {
        this.response = response;
        writer.write(this.response.toJSONString());
    }
}
