package br.com.starfusion.useful;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Henrique on 18/04/2016.
 */
public class DateTimeDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String data = json.getAsString();
        data = data.replaceAll("/", "");
        data = data.replaceAll("Date", "");
        data = data.substring(1, data.indexOf("-"));
        return new Date(Long.valueOf(data));
    }
}
