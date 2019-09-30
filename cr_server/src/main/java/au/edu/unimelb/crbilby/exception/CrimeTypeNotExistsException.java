
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.exception;

/**
 * Thrown when dealing with a crime type that does not exists
 */
public class CrimeTypeNotExistsException extends  RuntimeException {

    public CrimeTypeNotExistsException() {
        super();
    }

    public CrimeTypeNotExistsException(String message) {
        super(message);
    }
}
