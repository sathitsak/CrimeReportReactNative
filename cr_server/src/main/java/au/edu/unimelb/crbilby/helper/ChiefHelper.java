
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.db.tables.Post;
import au.edu.unimelb.crbilby.db.tables.daos.CaseAssignDao;
import au.edu.unimelb.crbilby.db.tables.daos.CaseDao;
import au.edu.unimelb.crbilby.db.tables.pojos.Case;
import au.edu.unimelb.crbilby.db.tables.pojos.CaseAssign;
import au.edu.unimelb.crbilby.exception.AssignException;
import au.edu.unimelb.crbilby.exception.InternalErrorException;
import au.edu.unimelb.crbilby.json.JAssign;
import au.edu.unimelb.crbilby.json.JAssigned;
import au.edu.unimelb.crbilby.util.Util;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import static au.edu.unimelb.crbilby.db.tables.CaseAssign.CASE_ASSIGN;
import static au.edu.unimelb.crbilby.db.tables.LeaPost.LEA_POST;
import static au.edu.unimelb.crbilby.db.tables.Post.POST;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ChiefHelper {

    /**
     * Assign a task by chief to an LEA
     * @param chief chief user Name
     * @param request
     */
    public static void assign(String chief, JAssign request) {
        request.validate();

        Integer post = PostHelper.getChiefPostID(chief);
        if(post == null)
            throw new AssignException("Chief has no authority");

        try(Connection conn = Util.createConn()) {
            CaseAssignDao dao = new CaseAssignDao(Util.createConf(conn));
            CaseAssign assign = new CaseAssign(request.caseID, request.userName);

            // Note: Consider making it smarter by checking the following:
            // 1. Current Case is within post for designated LEA
            // 2. userName Given is for an LEA role
            // 3. Chief is the chief for that LEA
            // 4. The case and profile exists (or handle the error)
            dao.insert(assign);

            // Note: Consider making it one transaction
            CaseDao caseDao = new CaseDao(Util.createConf(conn));
            Case c = caseDao.findById(request.caseID);
            c.setPost(post);

            caseDao.update(c);
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }

    /**
     * Fetch profile user name that is assigned to case
     * @param id case ID
     * @return
     */
    public static JAssigned assignedTo(int id) {
        try(Connection conn = Util.createConn()) {
            DSLContext create = DSL.using(Util.createConf(conn));

            String userName = create.select(CASE_ASSIGN.PROFILE)
                    .from(CASE_ASSIGN)
                    .where(CASE_ASSIGN.CASE.eq(id))
                    .fetchOne().into(String.class);

            if(userName == null)
                return null;

            JAssigned response = new JAssigned();
            response.userName = userName;

            return response;
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }

    /**
     * Retrieve list of LEAs in a post
     * @param chief chief user name for a post
     * @return
     */
    public static List<String> getLEA(String chief) {
        try(Connection conn = Util.createConn()) {
            DSLContext create = DSL.using(Util.createConf(conn));

            List<String> response = create.select(LEA_POST.PROFILE)
                    .from(LEA_POST)
                    .where(LEA_POST.POST.eq(
                            create.select(POST.ID)
                            .from(POST)
                            .where(POST.CHIEF.eq(chief))
                            .fetchOne().value1()
                    ).and(LEA_POST.PROFILE.ne(chief)))
                    .fetchInto(String.class);

            return response;
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }
}
