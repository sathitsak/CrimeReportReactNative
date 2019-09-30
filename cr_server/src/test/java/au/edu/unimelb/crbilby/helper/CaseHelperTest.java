
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.db.tables.pojos.Case;
import au.edu.unimelb.crbilby.db.tables.pojos.CaseOtherCrimes;
import au.edu.unimelb.crbilby.db.tables.pojos.CrimeType;
import au.edu.unimelb.crbilby.json.JCase;
import au.edu.unimelb.crbilby.json.JCases;
import au.edu.unimelb.crbilby.json.JCrimeType;
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
import java.sql.Timestamp;
import java.util.List;

import static au.edu.unimelb.crbilby.db.tables.Case.CASE;
import static au.edu.unimelb.crbilby.db.tables.CaseOtherCrimes.CASE_OTHER_CRIMES;
import static au.edu.unimelb.crbilby.db.tables.CrimeType.CRIME_TYPE;
import static au.edu.unimelb.crbilby.db.tables.Profile.PROFILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CaseHelperTest {

    public static void clean() throws SQLException {
        try(Connection conn = Util.createConn()) {
            DSLContext dsl = DSL.using(conn, SQLDialect.MYSQL);
            dsl.deleteFrom(CASE_OTHER_CRIMES).execute();
            dsl.deleteFrom(CASE).execute();
            dsl.deleteFrom(PROFILE).execute();
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


//    @Test
//    public void testAll() {
//        String street = "bb";
//        String street2 = "bc";
//        int ctId = CrimeTypeHelper.create(new CrimeType(1, "crime"));
//        Case c1 = new Case(1, null, ctId,street,new Timestamp(1)," ", 0.0,0.0, null,0);
//        int id =  CaseHelper.create(c1);
//        Case c2 = CaseHelper.findById(id);
//
//        Case c3 = new Case(1, null, null,street,new Timestamp(1)," ",0.0,0.0, null,0);
//        int id2 = CaseHelper.create(c3);
//        CaseOtherCrimes coc3 =
//                new CaseOtherCrimes(id2, "other");
//        CaseHelper.createOtherCrimeType(coc3);
//        assertTrue(street.equals(c2.getAddress()));
//
//        c1.setAddress(street2);
//        CaseHelper.update(c1);
//        Case c4 = CaseHelper.findById(id);
//        assertTrue(c4.getAddress().equals(street2));
//        List<Case> list = CaseHelper.findAll();
//        assertEquals(2, list.size());
//        List<Case> c5 = CaseHelper.findByCrimeType(ctId);
//        assertEquals(1, c5.size());
//    }

    @Test
    public void testGetRegisteredUserCases() {
        // Create profile
        JSignUp prof1 = new JSignUp();
        prof1.userName = "demo";
        prof1.password = "123456";
        prof1.firstName = "f1";
        prof1.lastName = "l1";
        prof1.phone = "1";
        ProfileHelper.create(prof1);

        // Create cases
        String street = "bb";

        // Case 1
        JCase c1 = new JCase();
        JCrimeType ct = new JCrimeType();
        ct.type = "crime";
        int ctId = CrimeTypeHelper.create(ct);
        Case cr1 = new Case(1, "demo", ctId,street,new Timestamp(System.currentTimeMillis())," ", 0.0,0.0, null,0);
        c1.case_report = cr1;
        CaseHelper.create("demo", c1);

        // Case 2
        JCase c2 = new JCase();
        Case cr2 = new Case(1, "demo", null,street,new Timestamp(System.currentTimeMillis())," ",0.0,0.0, null,0);
        CaseOtherCrimes coc =
                new CaseOtherCrimes(null, "other");
        c2.case_report = cr2;
        c2.case_other = coc;
        CaseHelper.create("demo", c2);

        // Case 3
        JCase c3 = new JCase();
        Case cr3 = new Case(1, null, ctId,street,new Timestamp(System.currentTimeMillis())," ", 0.0,0.0, null,0);
        c3.case_report = cr3;
        CaseHelper.create(null, c3);

        JCases cases = CaseHelper.getRegisterdUserCases("demo");
        assertEquals(cases.cases.get(0).case_report.getAddress(), street);
    }


}
