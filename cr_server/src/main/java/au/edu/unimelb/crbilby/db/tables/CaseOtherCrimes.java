/*
 * This file is generated by jOOQ.
 */
package au.edu.unimelb.crbilby.db.tables;


import au.edu.unimelb.crbilby.db.CrDb;
import au.edu.unimelb.crbilby.db.tables.records.CaseOtherCrimesRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;


/**
 * The table <code>cr_db.case_other_crimes</code>.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CaseOtherCrimes extends TableImpl<CaseOtherCrimesRecord> {

    private static final long serialVersionUID = 1841139968;

    /**
     * The reference instance of <code>cr_db.case_other_crimes</code>
     */
    public static final CaseOtherCrimes CASE_OTHER_CRIMES = new CaseOtherCrimes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CaseOtherCrimesRecord> getRecordType() {
        return CaseOtherCrimesRecord.class;
    }

    /**
     * The column <code>cr_db.case_other_crimes.id</code>.
     */
    public final TableField<CaseOtherCrimesRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>cr_db.case_other_crimes.crime</code>.
     */
    public final TableField<CaseOtherCrimesRecord, String> CRIME = createField("crime", org.jooq.impl.SQLDataType.VARCHAR(128).nullable(false), this, "");

    /**
     * Create a <code>cr_db.case_other_crimes</code> table reference
     */
    public CaseOtherCrimes() {
        this(DSL.name("case_other_crimes"), null);
    }

    /**
     * Create an aliased <code>cr_db.case_other_crimes</code> table reference
     */
    public CaseOtherCrimes(String alias) {
        this(DSL.name(alias), CASE_OTHER_CRIMES);
    }

    /**
     * Create an aliased <code>cr_db.case_other_crimes</code> table reference
     */
    public CaseOtherCrimes(Name alias) {
        this(alias, CASE_OTHER_CRIMES);
    }

    private CaseOtherCrimes(Name alias, Table<CaseOtherCrimesRecord> aliased) {
        this(alias, aliased, null);
    }

    private CaseOtherCrimes(Name alias, Table<CaseOtherCrimesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return CrDb.CR_DB;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CaseOtherCrimesRecord> getPrimaryKey() {
        return Internal.createUniqueKey(au.edu.unimelb.crbilby.db.tables.CaseOtherCrimes.CASE_OTHER_CRIMES, "KEY_case_other_crimes_PRIMARY", au.edu.unimelb.crbilby.db.tables.CaseOtherCrimes.CASE_OTHER_CRIMES.ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CaseOtherCrimesRecord>> getKeys() {
        return Arrays.<UniqueKey<CaseOtherCrimesRecord>>asList(
              Internal.createUniqueKey(au.edu.unimelb.crbilby.db.tables.CaseOtherCrimes.CASE_OTHER_CRIMES, "KEY_case_other_crimes_PRIMARY", au.edu.unimelb.crbilby.db.tables.CaseOtherCrimes.CASE_OTHER_CRIMES.ID)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CaseOtherCrimes as(String alias) {
        return new CaseOtherCrimes(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CaseOtherCrimes as(Name alias) {
        return new CaseOtherCrimes(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CaseOtherCrimes rename(String name) {
        return new CaseOtherCrimes(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CaseOtherCrimes rename(Name name) {
        return new CaseOtherCrimes(name, null);
    }
}
