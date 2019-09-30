
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.helper;

import au.edu.unimelb.crbilby.db.tables.daos.CrimeTypeDao;
import au.edu.unimelb.crbilby.db.tables.pojos.CrimeType;
import au.edu.unimelb.crbilby.exception.CrimeTypeNotExistsException;
import au.edu.unimelb.crbilby.exception.InternalErrorException;
import au.edu.unimelb.crbilby.exception.InvalidInputException;
import au.edu.unimelb.crbilby.json.JCrimeType;
import au.edu.unimelb.crbilby.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrimeTypeHelper {


    /**
     * Create crime type
     * @param
     */
    public static int create(JCrimeType request) {
        request.validate();

        try(Connection conn = Util.createConn()) {
            CrimeTypeDao dao = new CrimeTypeDao(Util.createConf(conn));
            CrimeType ct = new CrimeType();
            ct.setType(request.type);
            dao.insert(ct);

            return ct.getId();
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }

    /**
     * Retrieve all Crime types
     * @return a list of CrimeType object
     */
    public static List<JCrimeType> findAll() {
        List<JCrimeType> result;

        try(Connection conn = Util.createConn()) {
            CrimeTypeDao dao = new CrimeTypeDao(Util.createConf(conn));
            List<CrimeType> list = dao.findAll();
            result = new ArrayList<>(list.size());

            for(CrimeType ct : list)
            {
                JCrimeType json = new JCrimeType();
                json.id = ct.getId();
                json.type = ct.getType();
                result.add(json);
            }
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }

        return result;
    }

    /**
     * Retrieve crime type by id
     * @param id Crime Type id
     * @return CrimeType if found, null if not
     */
    public static JCrimeType findById(int id) {
        try(Connection conn = Util.createConn()) {
            CrimeTypeDao dao = new CrimeTypeDao(Util.createConf(conn));
            CrimeType tc = dao.findById(id);

            if(tc == null)
                throw new CrimeTypeNotExistsException("No crime type with id " + id);

            JCrimeType json = new JCrimeType();
            json.id = tc.getId();
            json.type = tc.getType();

            return json;
        }
        catch (SQLException e) {
            throw new InternalErrorException();
        }
    }
}
