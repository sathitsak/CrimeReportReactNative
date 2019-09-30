
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.db.tables.daos.CaseDao;
import au.edu.unimelb.crbilby.db.tables.daos.CaseNotesDao;
import au.edu.unimelb.crbilby.db.tables.pojos.CaseNotes;
import au.edu.unimelb.crbilby.exception.InternalErrorException;
import au.edu.unimelb.crbilby.json.JComment;
import au.edu.unimelb.crbilby.util.Util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CaseCommentHelper {

    /**
     * Adds a new comment to a case
     * @param request
     */
    public static void create(JComment request) {
        request.validate();

        try(Connection conn = Util.createConn()) {
            CaseNotesDao dao = new CaseNotesDao(Util.createConf(conn));
            CaseNotes comment = new CaseNotes();
            comment.setCase(request.caseID);
            comment.setProfile(request.userName);
            comment.setTimestamp(new Timestamp(System.currentTimeMillis()));
            comment.setNote(request.comment);
            dao.insert(comment);
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }

    /**
     * Retrieve all comments related to a case
     * @return
     */
    public static List<JComment> getComments(int caseID) {
        try(Connection conn = Util.createConn()) {
            CaseNotesDao dao = new CaseNotesDao(Util.createConf(conn));
            List<CaseNotes> list =  dao.fetchByCase(caseID);
            List<JComment> comments = new ArrayList<>(list.size());

            for(CaseNotes note : list) {
                JComment comment = new JComment();
                comment.caseID = note.getId();
                comment.userName = note.getProfile();
                comment.timeStamp = note.getTimestamp().getTime();
                comment.comment = note.getNote();

                comments.add(comment);
            }

            return comments;
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }
}
