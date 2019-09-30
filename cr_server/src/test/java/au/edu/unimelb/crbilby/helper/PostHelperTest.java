package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.json.JPost;
import au.edu.unimelb.crbilby.json.JSignUp;
import au.edu.unimelb.crbilby.util.Util;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static au.edu.unimelb.crbilby.db.tables.Profile.PROFILE;
import static au.edu.unimelb.crbilby.db.tables.Post.POST;

public class PostHelperTest {

    public static void clean() throws SQLException {
        try(Connection conn = Util.createConn()) {
            DSLContext dsl = DSL.using(conn, SQLDialect.MYSQL);
            dsl.deleteFrom(POST).execute();
            dsl.deleteFrom(PROFILE).execute();
        }
    }

    @Before
    public void setUp() throws SQLException {
        clean();
    }

    @AfterClass
    public static void after() throws SQLException {
        clean();
    }

    @Test
    public void testGetChiefPostId() {
        // Create Profile
        JSignUp profile = new JSignUp();
        profile.userName = "chief";
        profile.password = "123456";
        profile.firstName = "Big";
        profile.lastName = "Boss";
        profile.phone = "123456";

        ProfileHelper.create(profile);

        // Create Post
        JPost post = new JPost();
        post.name = "Station";
        post.level = EscalationLevel.LOCAL;
        post.chief = profile.userName;

        PostHelper.createPost(post);

        // Check functionality
        Integer postID = PostHelper.getChiefPostID(profile.userName);

        System.out.println(postID);
    }
}
