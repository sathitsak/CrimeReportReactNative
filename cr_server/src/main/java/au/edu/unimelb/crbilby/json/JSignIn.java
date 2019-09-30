
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.json;

import au.edu.unimelb.crbilby.exception.InvalidInputException;
import au.edu.unimelb.crbilby.util.Util;
import au.edu.unimelb.crbilby.util.Validable;

public class JSignIn implements Validable {
    public String userName;
    public String password;

    @Override
    public void validate() throws InvalidInputException {
        if (Util.isNullorEmpty(userName)
                || Util.isNullorEmpty(password))
            throw new InvalidInputException("Invalid sign in request");
    }
}
