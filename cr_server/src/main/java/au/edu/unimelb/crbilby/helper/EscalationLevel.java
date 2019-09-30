
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

/**
 * Enumerates Escalation level for a post
 */
public interface EscalationLevel {

    /**
     * Post at local level
     */
    public int LOCAL = 1;

    /**
     * Post at state level
     */
    public int STATE = 2;

    /**
     * Post at federal level
     */
    public int FEDERAL = 3;

}
