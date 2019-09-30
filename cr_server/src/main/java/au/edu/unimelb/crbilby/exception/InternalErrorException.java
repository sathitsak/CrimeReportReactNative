
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.exception;

/**
 * Generic error that hides an internal server error from user
 */
public class InternalErrorException extends RuntimeException {

    public InternalErrorException() {
        super();
    }

    public InternalErrorException(String message) {
        super(message);
    }
}
