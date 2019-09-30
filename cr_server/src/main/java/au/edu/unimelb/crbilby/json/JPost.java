
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.json;

import au.edu.unimelb.crbilby.exception.InvalidInputException;
import au.edu.unimelb.crbilby.helper.EscalationLevel;
import au.edu.unimelb.crbilby.util.Util;
import au.edu.unimelb.crbilby.util.Validable;

public class JPost implements Validable {
    public String name;
    public int level;
    public String chief;


    @Override
    public void validate() throws InvalidInputException {
        if (Util.isNullorEmpty(name)
                || Util.isNullorEmpty(chief)
                || level < EscalationLevel.LOCAL
                || level > EscalationLevel.FEDERAL)
            throw new InvalidInputException("Invalid post request");
    }
}
