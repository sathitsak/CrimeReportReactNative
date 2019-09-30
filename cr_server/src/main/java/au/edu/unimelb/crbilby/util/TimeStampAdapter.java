
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

package au.edu.unimelb.crbilby.util;

import au.edu.unimelb.crbilby.exception.InvalidInputException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Used to serialise timestamp for gson parsing
public class TimeStampAdapter implements JsonDeserializer<Timestamp> {


    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        long value = Long.parseLong(json.getAsString()) * 1000;

        Date value = null;
        try {
            value = new SimpleDateFormat("ddMMyy").parse(json.getAsString());
        } catch (ParseException e) {
            throw new InvalidInputException("Date cannot be parsed. Expected form is (ddMMyy");
        }


        return new Timestamp(value.getTime());
    }
}
