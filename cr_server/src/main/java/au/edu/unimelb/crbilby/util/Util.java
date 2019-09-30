
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Provides Utilities to interact with database
 */
public class Util {

    /**
     * Create DB Connection. Settings including url, username and password is read from
     * db.config file
     * @return Connection Object if connection was created successfully, not if not
     */
    public static Connection createConn() {
        Properties prop = new Properties();
        Connection conn;

        try {
            InputStream in = Util.class.getResourceAsStream("/db.config");
            prop.load(in);
            String url = prop.getProperty("url");
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");

            conn = DriverManager.getConnection(url, user, password);

        } catch (IOException | SQLException e) {
            return null;
        }

        return conn;
    }

    /**
     * Create Jooq DB Configuration to connect to a database.
     * @param conn JDBC Connection
     * @return JooQ Configuration Object
     */
    public static Configuration createConf(Connection conn) {
        return new DefaultConfiguration().set(conn).set(SQLDialect.MYSQL);
    }

    /**
     * Determine if given string is null or empty
     * @param data String to check
     * @return Boolean
     */
    public static boolean isNullorEmpty(String data) {
        return data == null || data.isEmpty();
    }

    /**
     * Retrieve configuration needed by AWS Cognito API
     * @return CognitoConfig object
     */
    public static CognitoConfig getCognitoConfig() {
        Properties prop = new Properties();
        CognitoConfig config = new CognitoConfig();

        try {
            InputStream in = Util.class.getResourceAsStream("/cognito.config");
            prop.load(in);
            config.pool_id = prop.getProperty("pool_id");
            config.client_app_id = prop.getProperty("client_app_id");
            config.region = prop.getProperty("region");
        } catch (IOException e) {
            return null;
        }

        return config;
    }

    public static Properties getAuthSettings() {
        Properties prop = new Properties();
        try {
            InputStream in = Util.class.getResourceAsStream("/auth.properties");
            prop.load(in);
        } catch (IOException e) {
            return null;
        }

        return prop;
    }

    /**
     * Builds an AWS Cognito Identity Provider. Build settings depends on default
     * cognito configurations in the associated config file
     * @return
     */
    public static AWSCognitoIdentityProvider getCognitoIdProvider() {
        CognitoConfig config = getCognitoConfig();
        AnonymousAWSCredentials creds = new AnonymousAWSCredentials();
        AWSCognitoIdentityProvider cognito = AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .withRegion(Regions.fromName(config.region))
                .build();

        return cognito;
    }
}
