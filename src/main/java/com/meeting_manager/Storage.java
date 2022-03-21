package com.meeting_manager;


import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Object> findAll(Class objectClass) {
        List<Object> outputList = new ArrayList<>();
        for (Object jsonObject : json) {
            outputList.add(convertJsonToObject((JSONObject) jsonObject,
                    objectClass));
        }
        return outputList;
    }


    public void writeJsonToFile() throws IOException {
        FileWriter file = new FileWriter("src/main/resources/storage.json");
        file.write(json.toJSONString());
        file.close();
    }

    public Object convertJsonToObject(JSONObject json, Class objectClass) {
        return new Gson().fromJson(json.toJSONString(),
                objectClass);
    }

    public JSONObject convertObjectToJson(Object object) throws ParseException {
        return (JSONObject) new JSONParser().parse(new Gson().toJson(object));
    }

}
