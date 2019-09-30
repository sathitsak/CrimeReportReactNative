/*
 * This file is generated by jOOQ.
 */
package au.edu.unimelb.crbilby.db.tables.daos;


import au.edu.unimelb.crbilby.db.tables.CaseNotes;
import au.edu.unimelb.crbilby.db.tables.records.CaseNotesRecord;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * The table <code>cr_db.case_notes</code>.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CaseNotesDao extends DAOImpl<CaseNotesRecord, au.edu.unimelb.crbilby.db.tables.pojos.CaseNotes, Integer> {

    /**
     * Create a new CaseNotesDao without any configuration
     */
    public CaseNotesDao() {
        super(CaseNotes.CASE_NOTES, au.edu.unimelb.crbilby.db.tables.pojos.CaseNotes.class);
    }

    /**
     * Create a new CaseNotesDao with an attached configuration
     */
    public CaseNotesDao(Configuration configuration) {
        super(CaseNotes.CASE_NOTES, au.edu.unimelb.crbilby.db.tables.pojos.CaseNotes.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getId(au.edu.unimelb.crbilby.db.tables.pojos.CaseNotes object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<au.edu.unimelb.crbilby.db.tables.pojos.CaseNotes> fetchById(Integer... values) {
        return fetch(CaseNotes.CASE_NOTES.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public au.edu.unimelb.crbilby.db.tables.pojos.CaseNotes fetchOneById(Integer value) {
        return fetchOne(CaseNotes.CASE_NOTES.ID, value);
    }

    /**
     * Fetch records that have <code>case IN (values)</code>
     */
    public List<au.edu.unimelb.crbilby.db.tables.pojos.CaseNotes> fetchByCase(Integer... values) {
        return fetch(CaseNotes.CASE_NOTES.CASE, values);
    }

    /**
     * Fetch records that have <code>profile IN (values)</code>
     */
    public List<au.edu.unimelb.crbilby.db.tables.pojos.CaseNotes> fetchByProfile(String... values) {
        return fetch(CaseNotes.CASE_NOTES.PROFILE, values);
    }

    /**
     * Fetch records that have <code>timestamp IN (values)</code>
     */
    public List<au.edu.unimelb.crbilby.db.tables.pojos.CaseNotes> fetchByTimestamp(Timestamp... values) {
        return fetch(CaseNotes.CASE_NOTES.TIMESTAMP, values);
    }

    /**
     * Fetch records that have <code>note IN (values)</code>
     */
    public List<au.edu.unimelb.crbilby.db.tables.pojos.CaseNotes> fetchByNote(String... values) {
        return fetch(CaseNotes.CASE_NOTES.NOTE, values);
    }
}
