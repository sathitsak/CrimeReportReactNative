
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.db.tables.pojos.CrimeType;
import au.edu.unimelb.crbilby.json.JCrimeType;
import au.edu.unimelb.crbilby.util.Util;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static au.edu.unimelb.crbilby.db.tables.CrimeType.CRIME_TYPE;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class CrimeTypeHelperTest {
    public static void clean() throws SQLException {
        CaseHelperTest.clean();
        try(Connection conn = Util.createConn()) {
            DSLContext dsl = DSL.using(conn, SQLDialect.MYSQL);
            dsl.deleteFrom(CRIME_TYPE).execute();
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
    public void testAll() {
        String crime = "crimeType";
        JCrimeType ct = new JCrimeType();
        ct.type = crime;
        int id = CrimeTypeHelper.create(ct);
        JCrimeType retrieved = CrimeTypeHelper.findById(id);
        assertTrue(crime.equals(retrieved.type));
        CrimeTypeHelper.create(ct);
        List<JCrimeType> list = CrimeTypeHelper.findAll();
        assertEquals(2, list.size());
    }

}
