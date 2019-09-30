
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.db.tables.daos.CaseDao;
import au.edu.unimelb.crbilby.db.tables.daos.CaseOtherCrimesDao;
import au.edu.unimelb.crbilby.db.tables.pojos.Case;
import au.edu.unimelb.crbilby.db.tables.pojos.CaseEvidence;
import au.edu.unimelb.crbilby.db.tables.pojos.CaseOtherCrimes;
import au.edu.unimelb.crbilby.exception.CaseNotExistsException;
import au.edu.unimelb.crbilby.exception.CrimeTypeNotExistsException;
import au.edu.unimelb.crbilby.exception.InternalErrorException;
import au.edu.unimelb.crbilby.exception.InvalidInputException;
import au.edu.unimelb.crbilby.json.JCase;
import au.edu.unimelb.crbilby.json.JCases;
import au.edu.unimelb.crbilby.json.JLocation;
import au.edu.unimelb.crbilby.json.JLocations;
import au.edu.unimelb.crbilby.util.Util;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import static au.edu.unimelb.crbilby.db.tables.Case.CASE;
import static au.edu.unimelb.crbilby.db.tables.CaseOtherCrimes.CASE_OTHER_CRIMES;
import static au.edu.unimelb.crbilby.db.tables.CaseEvidence.CASE_EVIDENCE;
import static au.edu.unimelb.crbilby.db.tables.CaseAssign.CASE_ASSIGN;
import static au.edu.unimelb.crbilby.db.tables.Post.POST;
import static org.jooq.impl.DSL.select;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class CaseHelper {

    /**
     * Create crime case
     * @param userName profile that reported the crime. Can be null if anonymous
     * @param request Case to report
     * @return Case ID
     */
    public static void create(String userName, JCase request) {
        request.validate();
        Case c = request.case_report;
        CaseOtherCrimes coc = request.case_other;
        CaseEvidence[] evidenceList = request.case_evidence;

        try(Connection conn = Util.createConn()) {
            DSLContext create = DSL.using(Util.createConf(conn));
            create.transaction(configuration -> {
                DSLContext ctx =  DSL.using(configuration);
                int id = ctx.insertInto(CASE, CASE.PROFILE, CASE.TYPE, CASE.ADDRESS, CASE.DATE,
                        CASE.DESCRIPTION, CASE.LONGITUDE, CASE.LATITUDE)
                        .values(userName, c.getType(), c.getAddress(), c.getDate(),
                                c.getDescription(), c.getLongitude(), c.getLatitude())
                        .returning(CASE.ID)
                        .fetchOne().getId();
                if (coc != null) {
                    ctx.insertInto(CASE_OTHER_CRIMES, CASE_OTHER_CRIMES.ID, CASE_OTHER_CRIMES.CRIME)
                            .values(id, coc.getCrime());
                }

                if (evidenceList != null) {
                    for(CaseEvidence ce : evidenceList ) {
                        ctx.insertInto(CASE_EVIDENCE, CASE_EVIDENCE.CASE, CASE_EVIDENCE.EVIDENCE)
                                .values(id, ce.getEvidence()).execute();
                    }
                }
            });
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }

    /**
     * Find case by id
     * @param id Case id
     */
    public static JCase findById(int id) {
        JCase jcase = null;

        try(Connection conn = Util.createConn()) {
            DSLContext create = DSL.using(Util.createConf(conn));

            /**
             *  The following is equivalent to:
             *  SELECT * FROM case as ct
             *  LEFT JOIN case_other_crimes as cot ON  ct.id = cot.id
             *  LEFT JOIN case_evidence as cet ON ct.id = cet.id
             *  WHERE ct.id = id
             */
            au.edu.unimelb.crbilby.db.tables.Case CT = CASE.as("ct");
            au.edu.unimelb.crbilby.db.tables.CaseOtherCrimes COT= CASE_OTHER_CRIMES.as("cot");
            au.edu.unimelb.crbilby.db.tables.CaseEvidence CET = CASE_EVIDENCE.as("cet");

            Map<JCase,List<CaseEvidence>> result = create.select(CT.fields())
                    .select(COT.fields())
                    .select(CET.fields())
                    .from(CT)
                    .leftJoin(COT).on(CT.ID.eq(COT.ID))
                    .leftJoin(CET).on(CT.ID.eq(CET.CASE))
                    .where(CT.ID.eq(id))
                    .fetchGroups(
                            // The fetch group maps case and case_other_crimes to case_evidence as the relationship
                            // is one to many
                            r ->  {
                                JCase jc = new JCase();
                                jc.case_report =  r.into(CT).into(Case.class);
                                jc.case_other = r.into(COT).into(CaseOtherCrimes.class);
                                // setting id as null as its already existing in case report
                                jc.case_other.setId(null);


                                return jc;
                            },
                            r -> {
                                CaseEvidence ev = r.into(CET).into(CaseEvidence.class);
                                // setting null as it is not needed to be represented
                                ev.setId(null);
                                ev.setCase(null);
                                return ev;
                            });

            // There is only one, so return first occurence
            for(JCase c : result.keySet())
            {
                c.case_evidence = result.get(c).toArray(new CaseEvidence[0]);
                c.is_assigned = create.fetchExists(
                        select()
                        .from(CASE_ASSIGN)
                        .where(CASE_ASSIGN.CASE.eq(c.case_report.getId()))
                );

                jcase = c;
            }

        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }

        return jcase;
    }

    /**
     * Retrieve location of all crimes
     * @return
     */
    public static JLocations getStatistics() {
        JLocations locs = new JLocations();
        try(Connection conn = Util.createConn()) {
            DSLContext create = DSL.using(Util.createConf(conn));

            List<Case> cases = create.selectFrom(CASE)
                    .where(CASE.LONGITUDE.isNotNull())
                    .and(au.edu.unimelb.crbilby.db.tables.Case.CASE.LATITUDE.isNotNull())
                    .fetchInto(Case.class);

            for(Case c : cases)
                locs.locations.add(new JLocation(c.getLongitude(), c.getLatitude()));

        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }

        return locs;
    }

    /**
     * Fetch cases that was created by a registered user
     * @param userName registered user id
     * @return JCases object
     */
    public static JCases getRegisterdUserCases(String userName) {
        JCases cases = new JCases();
        CaseOtherCrimes other = new CaseOtherCrimes();
        CaseEvidence evidence = new CaseEvidence();

        try(Connection conn = Util.createConn()) {
            DSLContext create = DSL.using(Util.createConf(conn));

            /**
             *  The following is equivalent to:
             *  SELECT * FROM case as ct
             *  LEFT JOIN case_other_crimes as cot ON  ct.id = cot.id
             *  LEFT JOIN case_evidence as cet ON ct.id = cet.id
             *  WHERE ct.profile = userName
             *  ORDER BY ct.date DESC
             */
            au.edu.unimelb.crbilby.db.tables.Case CT = CASE.as("ct");
            au.edu.unimelb.crbilby.db.tables.CaseOtherCrimes COT= CASE_OTHER_CRIMES.as("cot");
            au.edu.unimelb.crbilby.db.tables.CaseEvidence CET = CASE_EVIDENCE.as("cet");

            Map<JCase,List<CaseEvidence>> result = create.select(CT.fields())
                    .select(COT.fields())
                    .select(CET.fields())
                    .from(CT)
                    .leftJoin(COT).on(CT.ID.eq(COT.ID))
                    .leftJoin(CET).on(CT.ID.eq(CET.CASE))
                    .where(CT.PROFILE.eq(userName))
                    .orderBy(CT.DATE.desc())
                    .fetchGroups(
                    // The fetch group maps case and case_other_crimes to case_evidence as the relationship
                    // is one to many
                        r ->  {
                                JCase jc = new JCase();
                                jc.case_report =  r.into(CT).into(Case.class);
                                jc.case_other = r.into(COT).into(CaseOtherCrimes.class);
                                // setting id as null as its already existing in case report
                                jc.case_other.setId(null);


                                return jc;
                        },
                        r -> {
                        CaseEvidence ev = r.into(CET).into(CaseEvidence.class);
                        // setting null as it is not needed to be represented
                        ev.setId(null);
                        ev.setCase(null);
                        return ev;
                    });

            for(JCase c : result.keySet())
            {
                c.case_evidence = result.get(c).toArray(new CaseEvidence[0]);
                c.is_assigned = create.fetchExists(
                        select()
                                .from(CASE_ASSIGN)
                                .where(CASE_ASSIGN.CASE.eq(c.case_report.getId()))
                );
                cases.cases.add(c);
            }

        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }

        return cases;
    }

    /**
     * Retrieve cases assigned to an LEA
     * @param userName LEA user id
     * @param closed whether to fetch closed cases.
     * @return
     */
    public static JCases getLEACases(String userName, boolean closed) {
        int isClosed = 1;

        if(closed)
            isClosed = 0;

        JCases cases = new JCases();
        CaseOtherCrimes other = new CaseOtherCrimes();
        CaseEvidence evidence = new CaseEvidence();

        try(Connection conn = Util.createConn()) {
            DSLContext create = DSL.using(Util.createConf(conn));

            /**
             *  The following is equivalent to:
             *  SELECT * FROM case as ct
             *  LEFT JOIN case_other_crimes as cot ON  ct.id = cot.id
             *  LEFT JOIN case_evidence as cet ON ct.id = cet.id
             *  WHERE ct.id IN (
             *      SELECT case
             *      FROM case_assign
             *      WHERE profile = userName)
             *  AND ct.is_closed != 1
             *  ORDER BY ct.date DESC
             */
            au.edu.unimelb.crbilby.db.tables.Case CT = CASE.as("ct");
            au.edu.unimelb.crbilby.db.tables.CaseOtherCrimes COT= CASE_OTHER_CRIMES.as("cot");
            au.edu.unimelb.crbilby.db.tables.CaseEvidence CET = CASE_EVIDENCE.as("cet");

            Map<JCase,List<CaseEvidence>> result = create.select(CT.fields())
                    .select(COT.fields())
                    .select(CET.fields())
                    .from(CT)
                    .leftJoin(COT).on(CT.ID.eq(COT.ID))
                    .leftJoin(CET).on(CT.ID.eq(CET.CASE))
                    .where(CT.ID.in(
                            create.select(CASE_ASSIGN.CASE)
                                    .from(CASE_ASSIGN)
                                    .where(CASE_ASSIGN.PROFILE.eq(userName))
                                    .fetchInto(Integer.class)
                    ))
                    .and(CT.IS_CLOSED.ne(isClosed))
                    .orderBy(CT.DATE.desc())
                    .fetchGroups(
                            // The fetch group maps case and case_other_crimes to case_evidence as the relationship
                            // is one to many
                            r ->  {
                                JCase jc = new JCase();
                                jc.case_report =  r.into(CT).into(Case.class);
                                jc.case_other = r.into(COT).into(CaseOtherCrimes.class);
                                // setting id as null as its already existing in case report
                                jc.case_other.setId(null);


                                return jc;
                            },
                            r -> {
                                CaseEvidence ev = r.into(CET).into(CaseEvidence.class);
                                // setting null as it is not needed to be represented
                                ev.setId(null);
                                ev.setCase(null);
                                return ev;
                            });

            for(JCase c : result.keySet())
            {
                c.case_evidence = result.get(c).toArray(new CaseEvidence[0]);
                c.is_assigned = create.fetchExists(
                        select()
                                .from(CASE_ASSIGN)
                                .where(CASE_ASSIGN.CASE.eq(c.case_report.getId()))
                );
                cases.cases.add(c);
            }

        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }

        return cases;
    }

    /**
     * Retrieve cases a post chief has access to. This include:
     * <p>1. Cases belonging to a post the user is chief at</p>
     * <p>2. Cases belonging to no post at all (free roaming cases)</p>
     * @param userName
     * @param closed whether to fetch closed cases.
     * @return
     */
    public static JCases getChiefCases(String userName, boolean closed) {
        int isClosed = 1;

        if(closed)
            isClosed = 0;

        JCases cases = new JCases();
        CaseOtherCrimes other = new CaseOtherCrimes();
        CaseEvidence evidence = new CaseEvidence();

        try(Connection conn = Util.createConn()) {
            DSLContext create = DSL.using(Util.createConf(conn));

            /**
             *  The following is equivalent to:
             *  SELECT * FROM case as ct
             *  LEFT JOIN case_other_crimes as cot ON  ct.id = cot.id
             *  LEFT JOIN case_evidence as cet ON ct.id = cet.id
             *  WHERE
             *      ( ct.post = (
             *          SELECT id
             *          FROM post
             *          WHERE chief = userName) OR ct.post IS NULLS )
             *      AND ct.is_closed != 1
             *  ORDER BY ct.date DESC
             */
            au.edu.unimelb.crbilby.db.tables.Case CT = CASE.as("ct");
            au.edu.unimelb.crbilby.db.tables.CaseOtherCrimes COT= CASE_OTHER_CRIMES.as("cot");
            au.edu.unimelb.crbilby.db.tables.CaseEvidence CET = CASE_EVIDENCE.as("cet");

            Map<JCase,List<CaseEvidence>> result = create.select(CT.fields())
                    .select(COT.fields())
                    .select(CET.fields())
                    .from(CT)
                    .leftJoin(COT).on(CT.ID.eq(COT.ID))
                    .leftJoin(CET).on(CT.ID.eq(CET.CASE))
                    .where(CT.POST.in(
                            create.select(POST.ID)
                                    .from(POST)
                                    .where(POST.CHIEF.eq(userName))
                                    .fetchInto(Integer.class)
                    )).or(CT.POST.isNull())
                    .and(CT.IS_CLOSED.ne(isClosed))
                    .orderBy(CT.DATE.desc())
                    .fetchGroups(
                            // The fetch group maps case and case_other_crimes to case_evidence as the relationship
                            // is one to many
                            r ->  {
                                JCase jc = new JCase();
                                jc.case_report =  r.into(CT).into(Case.class);
                                jc.case_other = r.into(COT).into(CaseOtherCrimes.class);
                                // setting id as null as its already existing in case report
                                jc.case_other.setId(null);


                                return jc;
                            },
                            r -> {
                                CaseEvidence ev = r.into(CET).into(CaseEvidence.class);
                                // setting null as it is not needed to be represented
                                ev.setId(null);
                                ev.setCase(null);
                                return ev;
                            });

            for(JCase c : result.keySet())
            {
                c.case_evidence = result.get(c).toArray(new CaseEvidence[0]);
                c.is_assigned = create.fetchExists(
                        select()
                                .from(CASE_ASSIGN)
                                .where(CASE_ASSIGN.CASE.eq(c.case_report.getId()))
                );
                cases.cases.add(c);
            }

        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }

        return cases;
    }

    /**
     * Set a case to be resolved (closed) to reopened
     * @param id case ID
     * @param isClosed whether to resolve
     */
    public static void setCaseState(int id, boolean isClosed) {
        int state = 0;

        if(isClosed)
            state = 1;

        // Note: Considering checking if a case belongs to LEA
        try(Connection conn = Util.createConn()) {
            CaseDao dao = new CaseDao(Util.createConf(conn));
            Case c = dao.findById(id);

            if(c == null)
                throw new CaseNotExistsException("No case with id " + id);

            c.setIsClosed(state);
            dao.update(c);
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }
}
