
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

public class JAssign implements Validable {
    public Integer caseID;
    public String userName;


    @Override
    public void validate() throws InvalidInputException {
        if ( caseID == null
                ||Util.isNullorEmpty(userName))
            throw new InvalidInputException("Invalid assign request");
    }
}
