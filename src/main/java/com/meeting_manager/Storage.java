package com.meeting_manager;


import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Storage {

    private final JSONArray json;

    public Storage() throws IOException, ParseException {
        this.json = loadJson();
    }

    public static JSONArray loadJson() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(new FileReader("src/main/resources" +
                "/storage.json"));
    }

//    public JSONArray getJson() {
//        return json;
//    }

    public Object saveObject(Object object) throws IOException,
            ParseException {
        json.add(convertObjectToJson(object));
        writeJsonToFile();
        return object;
    }

    public void deleteObject(Object toDelete) throws IOException,
            ParseException {
        json.remove(convertObjectToJson(toDelete));
        writeJsonToFile();
    }

    public Object findByField(String fieldName, String fieldValue,
                              Class objectClass) throws ClassNotFoundException {
        JSONObject objectToReturn = null;
        for (Object object : json) {
            JSONObject objectJson = (JSONObject) object;
            if (objectJson.get(fieldName).equals(fieldValue)) {
                objectToReturn = objectJson;
            }
        }
        return objectToReturn == null ? null :
                convertJsonToObject(objectToReturn, objectClass);
    }

    public void writeJsonToFile() throws IOException {
        FileWriter file = new FileWriter("src/main/resources/storage.json");
        file.write(json.toJSONString());
        file.close();
    }

    public Object convertJsonToObject(JSONObject json, Class objectClass) throws ClassNotFoundException {
        return new Gson().fromJson(json.toJSONString(),
                objectClass);
    }

    public JSONObject convertObjectToJson(Object object) throws ParseException {
        return (JSONObject) new JSONParser().parse(new Gson().toJson(object));
    }

}
