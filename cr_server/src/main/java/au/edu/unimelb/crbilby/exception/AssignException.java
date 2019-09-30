
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.exception;

/**
 * Thrown when a chief has no authority to assign a case
 */
public class AssignException extends RuntimeException {

    public AssignException() {
        super();
    }

    public AssignException(String message) {
        super(message);
    }
}
