
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.json;

import au.edu.unimelb.crbilby.db.tables.pojos.CaseEvidence;
import au.edu.unimelb.crbilby.db.tables.pojos.CaseOtherCrimes;
import au.edu.unimelb.crbilby.exception.InvalidInputException;
import au.edu.unimelb.crbilby.util.Util;
import au.edu.unimelb.crbilby.util.Validable;

import java.util.List;

public class JCase implements Validable {
    public au.edu.unimelb.crbilby.db.tables.pojos.Case case_report;
    public CaseOtherCrimes case_other;
    public CaseEvidence[] case_evidence;
    public boolean is_assigned;

    @Override
    public void validate() throws InvalidInputException {

        if(case_report == null
                || Util.isNullorEmpty(case_report.getAddress())
                || case_report.getDate() == null
                || Util.isNullorEmpty(case_report.getDescription()))
            throw new InvalidInputException("Invalid case report request");

        // Make sure that if other type crimes were supplied, a crime name is given
        if(case_other != null && Util.isNullorEmpty(case_other.getCrime())) {
            throw new InvalidInputException("Invalid case report request");
        }

        // Make sure if evidences are provide, that each evidence contain a evidence string
        if(case_evidence != null) {
            for(CaseEvidence ce : case_evidence)
                if(Util.isNullorEmpty(ce.getEvidence()))
                    throw new InvalidInputException("Invalid case report request");
        }

        // Check that either case type id is given, or other crime name but
        // not both are given.
        if(case_report.getType() == null && (case_other == null || case_other.getCrime() == null))
            throw new InvalidInputException("Invalid case report request");

        if(case_report.getType() != null && case_other != null && case_other.getCrime() != null)
            throw new InvalidInputException("Invalid case report request");
    }


}
