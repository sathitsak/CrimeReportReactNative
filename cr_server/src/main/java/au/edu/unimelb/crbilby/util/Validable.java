
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.util;

import au.edu.unimelb.crbilby.exception.InvalidInputException;

/**
 * Used to determine if a given json request includes all necessary information
 * to deem the request valid
 */
public interface Validable {

    /**
     * Validates JSON request
     * @throws InvalidInputException if request is not valid
     */
    public void validate() throws InvalidInputException;
}
