
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

public class JSignUp implements Validable {
    public String userName;
    public String password;
    public String firstName;
    public String lastName;
    public String phone;


    @Override
    public void validate() {
        if (Util.isNullorEmpty(userName)
                || Util.isNullorEmpty(password)
                || Util.isNullorEmpty(firstName)
                || Util.isNullorEmpty(lastName)
                || Util.isNullorEmpty(phone))
            throw new InvalidInputException("Invalid sign up request");
    }
}
