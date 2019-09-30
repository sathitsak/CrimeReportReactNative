
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.db.tables.Profile;
import au.edu.unimelb.crbilby.db.tables.pojos.Case;
import au.edu.unimelb.crbilby.db.tables.pojos.CaseOtherCrimes;
import au.edu.unimelb.crbilby.json.JCase;
import au.edu.unimelb.crbilby.json.JPost;
import au.edu.unimelb.crbilby.json.JSignUp;
import au.edu.unimelb.crbilby.util.Util;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static au.edu.unimelb.crbilby.db.tables.Case.CASE;
import static au.edu.unimelb.crbilby.db.tables.CaseOtherCrimes.CASE_OTHER_CRIMES;
import static au.edu.unimelb.crbilby.db.tables.CrimeType.CRIME_TYPE;
import static au.edu.unimelb.crbilby.db.tables.Profile.PROFILE;
import static au.edu.unimelb.crbilby.db.tables.Post.POST;

public class ChiefHelperTest {
    public static void clean() throws SQLException {
        try(Connection conn = Util.createConn()) {
            DSLContext dsl = DSL.using(conn, SQLDialect.MYSQL);
            dsl.deleteFrom(POST).execute();
            dsl.deleteFrom(PROFILE).execute();
            dsl.deleteFrom(CASE_OTHER_CRIMES).execute();
            dsl.deleteFrom(CASE).execute();
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
    public void testGetLEA() {
        // Create Case
        Case c = new Case(1, null, null, "some place", new Timestamp(System.currentTimeMillis()), " ", null, null, null, 0);
        CaseOtherCrimes coc = new CaseOtherCrimes(1, "crime");
        JCase jc = new JCase();
        jc.case_report = c;
        jc.case_other = coc;
        CaseHelper.create(null, jc);

        // Create Profiles

        // LEA
        JSignUp lea = new JSignUp();
        lea.userName = "lea";
        lea.password = "123456";
        lea.firstName = "LEA";
        lea.lastName = "LEA";
        lea.phone = "123456";

        // Chief
        JSignUp chief = new JSignUp();
        chief.userName = "chief";
        chief.password = "123456";
        chief.firstName = "chief";
        chief.lastName = "chief";
        chief.phone = "123456";

        ProfileHelper.create(lea);
        ProfileHelper.create(chief);

        // Create Post
        JPost post = new JPost();
        post.name = "post";
        post.level = EscalationLevel.LOCAL;
        post.chief = chief.userName;

        int postID = PostHelper.createPost(post);

        // Link leas to post
        PostHelper.linkLeaToPost(lea.userName, postID);

        List<String> leas = ChiefHelper.getLEA(chief.userName);

        assertEquals(lea.userName, leas.get(0));
    }
}
