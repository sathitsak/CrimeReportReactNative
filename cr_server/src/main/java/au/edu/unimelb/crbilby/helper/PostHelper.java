
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.db.tables.daos.CaseAssignDao;
import au.edu.unimelb.crbilby.db.tables.daos.LeaPostDao;
import au.edu.unimelb.crbilby.db.tables.daos.PostDao;
import au.edu.unimelb.crbilby.db.tables.pojos.CaseAssign;
import au.edu.unimelb.crbilby.db.tables.pojos.LeaPost;
import au.edu.unimelb.crbilby.db.tables.pojos.Post;
import au.edu.unimelb.crbilby.exception.InternalErrorException;
import au.edu.unimelb.crbilby.json.JPost;
import au.edu.unimelb.crbilby.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PostHelper {

    /**
     * Creates a new post
     * @param request
     * @return Post ID
     */
    public static int createPost(JPost request) {
        request.validate();

        try (Connection conn = Util.createConn()) {
            PostDao dao = new PostDao(Util.createConf(conn));
            Post post = new Post(null, request.name, request.level, request.chief);
            dao.insert(post);

            return post.getId();

        } catch (SQLException e) {
            throw new InternalErrorException();
        }
    }


    /**
     * Makes an LEA part of a post
     * @param lea LEA user id
     * @param post Post ID
     */
    public static void linkLeaToPost(String lea, int post) {

        try (Connection conn = Util.createConn()) {
            LeaPostDao dao = new LeaPostDao(Util.createConf(conn));
            LeaPost link = new LeaPost(lea, post);
            dao.insert(link);
        } catch (SQLException e) {
            throw new InternalErrorException();
        }
    }

    /**
     * Retrieve post id for a post with a given chief
     * @param userName Chief user id
     * @return post id if found, null if not
     */
    public static Integer getChiefPostID(String userName) {
        try(Connection conn = Util.createConn()) {
            PostDao dao = new PostDao(Util.createConf(conn));

            List<Post> list = dao.fetchByChief(userName);

            if(list.isEmpty())
                return null;

            return list.get(0).getId();
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }
}
