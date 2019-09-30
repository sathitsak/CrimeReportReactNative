
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

public class JComment implements Validable {
    public Integer caseID;
    public String userName;
    public Long timeStamp;
    public String comment;

    @Override
    public void validate() throws InvalidInputException {
        if ( caseID == null
                || Util.isNullorEmpty(comment))
            throw new InvalidInputException("Invalid comment request");
    }
}
