
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

/**
 * Enumerates User Types used in database
 */
public interface UserRole {
    /**
     * Normally registered user that report crimes
     */
    public int NORMAL = 1;

    /**
     * Law Enforcement Agent User
     */
    public int LEA = 2;

    /**
     * Post LEA Chief
     */
    public int LEA_CHIEF = 3;

    /**
     * System Admin
     */
    public int ADMIN = 4;
}
