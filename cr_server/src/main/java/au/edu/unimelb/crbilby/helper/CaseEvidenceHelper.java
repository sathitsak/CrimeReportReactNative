
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.db.tables.daos.CaseEvidenceDao;
import au.edu.unimelb.crbilby.db.tables.pojos.CaseEvidence;
import au.edu.unimelb.crbilby.exception.CaseNotExistsException;
import au.edu.unimelb.crbilby.exception.InternalErrorException;
import au.edu.unimelb.crbilby.exception.InvalidInputException;
import au.edu.unimelb.crbilby.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CaseEvidenceHelper {

    /**
     * Create evidence for a crime case
     * @param list List of evidence to create
     */
    public static void create(List<CaseEvidence> list) {
        if(list == null || list.isEmpty())
            throw new InvalidInputException("Invalid evidences");

        for(CaseEvidence ce : list) {
            if(ce.getCase() == null || Util.isNullorEmpty(ce.getEvidence()))
                throw new InvalidInputException("Invalid evidence was given: " + ce.toString());

            if(CaseHelper.findById(ce.getCase()) == null)
                throw new CaseNotExistsException("JCase with id: " + ce.getId());

            ce.setId(null);
        }

        try(Connection conn = Util.createConn()) {
            CaseEvidenceDao dao = new CaseEvidenceDao(Util.createConf(conn));
            dao.insert(list);
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }

    /**
     * Retrieve all evidences related to a crime case
     * @param id JCase id
     * @return List of Evidences
     */
    public static List<CaseEvidence> findById(int id) {
        try(Connection conn = Util.createConn()) {
            CaseEvidenceDao dao = new CaseEvidenceDao(Util.createConf(conn));
            return dao.fetchByCase(id);
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }
}
