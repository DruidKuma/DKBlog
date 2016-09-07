package com.druidkuma.blog.test;

import com.druidkuma.blog.service.excel.ExcelDocument;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 9/7/16
 */
public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(new FileReader("/Users/DruidKuma/Desktop/toroleo/DKBlogBuilder/src/main/resources/attend.json"));
        JSONObject jsonObject = (JSONObject) obj;


        ExcelDocument document = new ExcelDocument("Attendees");

        document.addHeaderCell("Name");
        document.addHeaderCell("Company");
        document.addHeaderCell("Country");
        document.addHeaderCell("Avatar URL");
        document.newLine();

        int count = 1;
        for (Object attendObj : ((JSONArray) jsonObject.get("attendees"))){

            System.out.println("Mapping attendee with index: " + count);
            JSONObject attendee = (JSONObject) attendObj;

            document.addStringCell((String)attendee.get("name"));
            document.addStringCell((String)attendee.get("company"));
            document.addStringCell((String)attendee.get("country"));
            document.addStringCell((String)attendee.get("avatar_url"));
            document.newLine();

            count++;

        }


        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("/Users/DruidKuma/Desktop/toroleo/DKBlogBuilder/src/main/resources/attendees.xls");
        fileOut.write(document.getBytes());
        fileOut.close();
    }
}
