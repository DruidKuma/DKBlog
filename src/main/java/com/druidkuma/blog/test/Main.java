package com.druidkuma.blog.test;

import com.druidkuma.blog.service.excel.ExcelDocument;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Set;

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
        test2();
    }

    public static void test() throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        ExcelDocument document = new ExcelDocument("Attendees");
        document.addHeaderCell("Name");
        document.addHeaderCell("Company");
        document.addHeaderCell("Country");
        document.addHeaderCell("Avatar URL");
        document.newLine();

        Set<Long> unique = Sets.newHashSet();

        for (int i = 1; i <= 1000; i++) {

            String body = Jsoup
                    .connect("https://api.cilabs.net/v1/conferences/ws16/info/attendees?limit=1000&page=" + i)
                    .header("Accept-Charset", "UTF-8")
                    .header("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)")
                    .ignoreContentType(true)
                    .execute().body().trim();


            Object obj = parser.parse(body);
            JSONObject jsonObject = (JSONObject) obj;

            int count = 1;
            for (Object attendObj : ((JSONArray) jsonObject.get("attendees"))){
                System.out.println(String.format("Mapping attendee for page %s with index: %s", i, count));

                JSONObject attendee = (JSONObject) attendObj;

                Long id = (Long)attendee.get("id");
                if(!unique.contains(id)) {
                    unique.add(id);

                    document.addStringCell((String)attendee.get("name"));
                    document.addStringCell((String)attendee.get("company"));
                    document.addStringCell((String)attendee.get("country"));
                    document.addStringCell((String)attendee.get("avatar_url"));
                    document.newLine();
                }
                count++;
            }
        }

        System.out.println("Unique visitors: " + unique.size());

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("/Users/DruidKuma/Desktop/corside/blog/src/main/resources/attendees.xls");
        fileOut.write(document.getBytes());
        fileOut.close();
    }

    public static void test2() throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        ExcelDocument document = new ExcelDocument("Startups");
        document.addHeaderCell("Company Name");
        document.addHeaderCell("Logo URL");
        document.addHeaderCell("Website URL");
        document.addHeaderCell("Country");
        document.addHeaderCell("City");
        document.newLine();

        Set<Long> unique = Sets.newHashSet();

        for (int i = 1; i <= 25; i++) {

            String body = Jsoup
                    .connect("https://api.cilabs.net/v1/conferences/ws16/info/startups/alumni?limit=25&page=" + i)
                    .header("Accept-Charset", "UTF-8")
                    .header("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)")
                    .ignoreContentType(true)
                    .execute().body().trim();


            Object obj = parser.parse(body);
            JSONObject jsonObject = (JSONObject) obj;

            int count = 1;
            for (Object startUpObj : ((JSONArray) jsonObject.get("startups"))){
                System.out.println(String.format("Mapping startup for page %s with index: %s", i, count));

                JSONObject startup = (JSONObject) startUpObj;

                Long id = (Long)startup.get("id");
                if(!unique.contains(id)) {
                    unique.add(id);

                    document.addStringCell((String)startup.get("company_name"));
                    document.addStringCell((String)startup.get("brandisty_url"));
                    document.addStringCell((String)startup.get("website_url"));
                    document.addStringCell((String)startup.get("country"));
                    document.addStringCell((String)startup.get("city"));

                    document.newLine();
                }
                count++;
            }
        }

        System.out.println("Unique visitors: " + unique.size());

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("/Users/DruidKuma/Desktop/corside/blog/src/main/resources/startups.xls");
        fileOut.write(document.getBytes());
        fileOut.close();
    }
}
