
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

public class JUpdateProfile implements Validable {
    public String firstName;
    public String lastName;
    public String phone;

    @Override
    public void validate() throws InvalidInputException {
        if (Util.isNullorEmpty(firstName)
                || Util.isNullorEmpty(lastName)
                || Util.isNullorEmpty(phone))
            throw new InvalidInputException("Invalid profile update request");
    }
}
