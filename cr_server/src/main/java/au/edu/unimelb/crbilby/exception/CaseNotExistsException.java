
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.exception;

/**
 * Thrown when dealing with a case that does not exist
 */
public class CaseNotExistsException extends RuntimeException {

    public CaseNotExistsException() {
        super();
    }

    public CaseNotExistsException(String message) {
        super(message);
    }
}
