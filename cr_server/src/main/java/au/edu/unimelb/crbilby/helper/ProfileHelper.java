
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.db.tables.daos.ProfileDao;
import au.edu.unimelb.crbilby.db.tables.pojos.Profile;
import au.edu.unimelb.crbilby.exception.InternalErrorException;
import au.edu.unimelb.crbilby.json.JSignUp;
import au.edu.unimelb.crbilby.json.JUpdateProfile;
import au.edu.unimelb.crbilby.util.Util;

import java.sql.Connection;
import java.sql.SQLException;

public class ProfileHelper {

    /**
     * Create a new profile user
     * @param request
     */
    public static void create(JSignUp request) {
        request.validate();

        try(Connection conn = Util.createConn()) {
            ProfileDao dao = new ProfileDao(Util.createConf(conn));
            Profile profile = new Profile();
            profile.setId(request.userName);
            profile.setFirstName(request.firstName);
            profile.setLastName(request.lastName);
            profile.setPhone(request.phone);
            profile.setRole(UserRole.NORMAL);
            dao.insert(profile);
        } catch (SQLException e) {
            throw new InternalErrorException();
        }
    }

    /**
     * Update a profile with a specified id
     * @param userName User id
     * @param request
     */
    public static void update(String userName, JUpdateProfile request) {
        request.validate();

        try(Connection conn = Util.createConn()) {
            ProfileDao dao = new ProfileDao(Util.createConf(conn));
            Profile profile = dao.findById(userName);
            profile.setFirstName(request.firstName);
            profile.setLastName(request.lastName);
            profile.setPhone(request.phone);

            dao.update(profile);
        } catch (SQLException e) {
            throw new InternalErrorException();
        }
    }

    /**
     * Retrieve role of a user
     * @param userName User id
     * @return Integer representing a role
     */
    public static int getRole(String userName) {
        try(Connection conn = Util.createConn()) {
            ProfileDao dao = new ProfileDao(Util.createConf(conn));
            Profile profile = dao.findById(userName);

            return profile.getRole();
        } catch (SQLException e) {
            throw new InternalErrorException();
        }
    }
}
