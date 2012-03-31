package com.openplanetideas.plusyou.provider.external;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * @author Mark Taylor <marktaycon@googlemail.com>
 * @version 1.6
 * @since 30/03/12
 */
public class JSONParseTest {
    private JsonParser jsonParser = new JsonParser();


    @Test
    public void testJsonParser(){
        JsonElement jsonElement = jsonParser.parse(getJson());
        Set<Map.Entry<String, JsonElement>> jsonEntries = jsonElement.getAsJsonObject().entrySet();

        System.out.println();
    }

    private String getJson(){
       return  "{\"status\":null,\"organisation\":{\"name\":null,\"firstname\":null,\"lastname\":null,\"location\":{\"name\":null,\"country\":null,\"lat\":null,\"lon\":null,\"virtual\":null},\"urls\":{\"organisation\":null,\"agency\":null,\"image\":null,\"email\":null},\"accreditation\":null,\"categories\":[],\"dates\":{\"created\":\"2012-03-30T16:57:44.482Z\",\"updated\":null},\"description\":{\"html\":null,\"plain\":null}},\"categories\":[],\"availability\":{\"start\":null,\"end\":null},\"dates\":{\"created\":\"2012-03-30T16:57:44.482Z\",\"updated\":null},\"title\":null,\"description\":null,\"urls\":{\"organisation\":null,\"agency\":null,\"image\":null,\"email\":null},\"location\":{\"name\":null,\"country\":null,\"lat\":null,\"lon\":null,\"virtual\":null},\"ageRange\":{\"min\":null,\"max\":null},\"volunteers\":{\"referred\":null,\"max\":null,\"min\":null},\"skillsNeeded\":{\"html\":null,\"plain\":null},\"type\":null}";
    }
}
