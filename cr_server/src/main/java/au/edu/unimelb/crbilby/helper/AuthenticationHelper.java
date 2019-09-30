
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.json.JSignIn;
import au.edu.unimelb.crbilby.json.JSignUp;
import au.edu.unimelb.crbilby.json.JWT;
import au.edu.unimelb.crbilby.util.CognitoConfig;
import au.edu.unimelb.crbilby.util.Util;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;

import java.util.HashMap;
import java.util.Map;


public class AuthenticationHelper {

    /**
     * Create new user in cognito and database. Created used is for normal users only. Please see {@link UserRole}
     * @param request
     */
    public static void signUp(JSignUp request) {

        CognitoConfig config = Util.getCognitoConfig();
        AWSCognitoIdentityProvider cognito = Util.getCognitoIdProvider();

        // Confirmation of user is handled as a pre sign up lambda trigger
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setClientId(config.client_app_id);
        signUpRequest.setUsername(request.userName);
        signUpRequest.setPassword(request.password);
        SignUpResult signUpResult = cognito.signUp(signUpRequest);

        // Reaching this point without error indicates a successful result
        ProfileHelper.create(request);

    }

    /**
     * Authenticate a user
     * @param request Sign In Request with user name and password
     * @return JWT Token information
     */
    public static JWT signIn(JSignIn request) {
        request.validate();

        CognitoConfig config = Util.getCognitoConfig();
        AWSCognitoIdentityProvider cognito = Util.getCognitoIdProvider();

        Map<String, String> params = new HashMap<>();
        params.put("USERNAME", request.userName);
        params.put("PASSWORD", request.password);

        InitiateAuthRequest login = new InitiateAuthRequest();
        login.withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .withClientId(config.client_app_id)
                .withAuthParameters(params);

        AuthenticationResultType result = cognito.initiateAuth(login).getAuthenticationResult();
        JWT token = new JWT();
        token.accessToken = result.getAccessToken();
        token.role = ProfileHelper.getRole(request.userName);
        token.idToken = result.getIdToken();
        token.refreshToken = result.getRefreshToken();

        return token;
    }

    /**
     * SignOut user
     * @param accessToken User current access token
     */
    public static void signOut(String accessToken) {


        CognitoConfig config = Util.getCognitoConfig();
        AnonymousAWSCredentials creds = new AnonymousAWSCredentials();
        AWSCognitoIdentityProvider cognito = AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .withRegion(Regions.fromName(config.region))
                .build();

        GlobalSignOutRequest signOutRequest = new GlobalSignOutRequest();
        signOutRequest.setAccessToken(accessToken);
        cognito.globalSignOut(signOutRequest);
    }
}
