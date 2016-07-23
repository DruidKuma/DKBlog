package com.druidkuma.blog.util;

import com.druidkuma.blog.dao.blogEntry.BlogEntryRepository;
import com.druidkuma.blog.dao.country.CountryRepository;
import com.druidkuma.blog.dao.country.LanguageRepository;
import com.druidkuma.blog.domain.BlogEntry;
import com.druidkuma.blog.domain.Comment;
import com.druidkuma.blog.domain.Content;
import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.domain.country.Language;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/15/16
 */
//@Service
public class WordpressBlogMigrator {

    @Autowired
    private BlogEntryRepository blogEntryRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void migrate() throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<Integer, BlogEntry> entryMap = new HashMap<>();

        String postFile = "/Users/DruidKuma/Desktop/posts.txt";
        Reader in = new FileReader(postFile);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

        System.out.println("Starting to parse posts...");
        for (CSVRecord record : records) {
            if (record.size() != 23) {
                throw new RuntimeException("Incorrect format of line: " + record);
            }

            String content = record.get(4);

            if(content == null || content.equals("")) {
                continue;
            }

            String src = extractImageUrlFromFirstCaption(content);
            if (src == null) continue;
//            if(!exists(src)) continue;

            entryMap.put(Integer.parseInt(record.get(0)),
                    BlogEntry.builder()
                            .author(record.get(1))
                            .creationDate(LocalDateTime.parse(record.get(2), formatter).toInstant(ZoneOffset.UTC))
                            .content(Content.builder().contents(normalizeContent(record.get(4))).imageUrl(src).title(record.get(5)).build())
                            .numViews(1000L)
                            .permalink("Temporarily null")
                            .numComments(Long.parseLong(record.get(22)))
                            .isPublished("publish".equals(record.get(7)))
                            .commentsEnabled("open".equals(record.get(8)))
                            .build());

            System.out.println("Accepted blog post with ID: " + record.get(0));
        }
        System.out.println("Finished parsing posts...");

        String commentsFile = "/Users/DruidKuma/Desktop/comments.txt";
        in = new FileReader(commentsFile);
        records = CSVFormat.DEFAULT.parse(in);

        Map<Integer, Comment> commentMap = new HashMap<>();

        System.out.println("Starting to parse comments...");
        for (CSVRecord record : records) {
            if(record.size() != 15) {
                throw new RuntimeException("Incorrect format of line: " + record);
            }

            if(entryMap.get(Integer.parseInt(record.get(1))) == null) {
                System.out.println("Comment declined, blog post does not exist");
            }

            commentMap.put(Integer.parseInt(record.get(0)), Comment.builder()
//                    .blogPostId(Integer.parseInt(record.get(1)))
                    .author(record.get(2))
                    .email(record.get(3))
                    .authorIp(record.get(5))
                    .creationDate(LocalDateTime.parse(record.get(6), formatter).toInstant(ZoneOffset.UTC))
                    .body(record.get(8))
                    .isApproved("1".equals(record.get(10)))
                    .authorUserAgent(record.get(11))
//                    .parentId(StringUtils.isEmpty(record.get(13)) ? null : Integer.parseInt(record.get(13)))
                    .build());
        }
        System.out.println("Finished parsing comments");

        System.out.println("Mapping blog etries with comments...");
        for (Map.Entry<Integer, Comment> entry : commentMap.entrySet()) {
            Comment comment = entry.getValue();
//            if(comment.getParentId() != null) {
//                comment.addParent(commentMap.get(comment.getParentId()));
//                BlogEntry blogEntry = entryMap.get(comment.getBlogPostId());
//                if(blogEntry == null) continue;
//                if(blogEntry.getComments() == null) blogEntry.setComments(new ArrayList<>());
//                blogEntry.getComments().add(comment);
//            }
        }

        System.out.println("Saving blog entries to database...");
        for (BlogEntry blogEntry : entryMap.values()) {
            blogEntryRepository.save(blogEntry);
        }
        blogEntryRepository.flush();

        System.out.println("Done");

    }

    public static boolean exists(String URLName) throws IOException {
        try {
            final URL url = new URL(URLName);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            return huc.getResponseCode() == 200;
        } catch (Exception e) {
            System.out.println("Declined blog entry, image does not exist");
            return false;
        }
    }

    public static String extractImageUrlFromFirstCaption(String content) {
        Document parse = Jsoup.parse(content);
        Elements img = parse.getElementsByTag("img");
        if(img.size() < 1) {
            return null;
        }
        return img.get(0).attr("src");
    }

    public static String normalizeContent(String content) {
        Document document = Jsoup.parse(content.replaceAll("\\[caption", "<caption").replaceAll("\"\\]","\">").replaceAll("\\[/caption\\]", "</caption>"));
        return document.select("body").html();
    }

    public void test() {
        for (BlogEntry blogEntry : blogEntryRepository.findAll()) {

            //normalize content
//            Content content = blogEntry.getContent();
//            if(StringUtils.isEmpty(content)) continue;
//            content.setContents(normalizeContent(content.getContents()));
//            blogEntryRepository.save(blogEntry);

            //recount comments
            Long aLong = jdbcTemplate.queryForObject("SELECT count(1) FROM comments cm WHERE cm.cm_blog_entry_id = " + blogEntry.getId(), Long.class);
            blogEntry.setNumComments(aLong);
            blogEntryRepository.save(blogEntry);
        }
        blogEntryRepository.flush();
    }

    public void saveCountries() throws IOException, ParseException {
        JSONArray countries = (JSONArray) new JSONParser().parse(new FileReader(this.getClass().getClassLoader().getResource("db/countries.json").getFile()));

        List<String> availableCountries = Lists.newArrayList("BE", "FR", "ES", "AR", "BR", "PT", "CH", "IT", "PL", "NL", "RU",
                "UA", "SK", "BG", "CL", "CZ", "SI", "RO", "EE", "FI", "GR", "NO", "LV", "IN", "ZA", "IE", "NZ", "MY", "SG", "TH",
                "HR", "RS", "BY", "KZ", "MX", "PE", "EC", "HU", "CN", "TR", "SE", "DK", "JP", "LT", "DE", "AT", "GB", "US", "CA", "AU");

        for (Object countryObj : countries) {
            JSONObject country = (JSONObject) countryObj;
            countryRepository.save(Country.builder()
                    .isEnabled(availableCountries.contains((String)country.get("cca2")))
                    .isoAlpha2Code((String)country.get("cca2"))
                    .isoAlpha3CodeCode((String)country.get("cca3"))
                    .isoNumeric((String)country.get("ccn3"))
                    .name(((String)((JSONObject)country.get("name")).get("common")).split(";")[0])
                    .build());
        }
        countryRepository.flush();
    }

    public void saveLanguages() throws IOException, ParseException {
        JSONArray languages = (JSONArray) new JSONParser().parse(new FileReader(this.getClass().getClassLoader().getResource("db/languages.json").getFile()));
        for (Object languageObj : languages) {
            JSONObject language = (JSONObject) languageObj;
            languageRepository.save(Language.builder()
                    .isoCode((String)language.get("code"))
                    .name((String)language.get("name"))
                    .nativeName((String)language.get("nativeName"))
                    .build());
        }
        languageRepository.flush();
    }

//    @PostConstruct
    public void createCountryLanguageMappings() throws IOException, ParseException {
        JSONArray countries = (JSONArray) new JSONParser().parse(new FileReader(this.getClass().getClassLoader().getResource("db/countries.json").getFile()));

        Reader in = new FileReader(this.getClass().getClassLoader().getResource("db/lang_iso.txt").getFile());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('|').parse(in);

        Map<String, Language> langIso3ToIso2 = Maps.newHashMap();
        for (CSVRecord record : records) {
            if(StringUtils.isNotBlank(record.get(2))) {
                Language language = languageRepository.findByIsoCode(record.get(2));
                if(language != null) langIso3ToIso2.put(StringUtils.isNotBlank(record.get(1)) ? record.get(1) : record.get(0), language);
            }
        }

        for (Object countryObj : countries) {
            JSONObject country = (JSONObject) countryObj;

            String isoCountry = (String)country.get("cca2");
            Country country1 = countryRepository.findByIsoAlpha2Code(isoCountry);

            Set languages = ((JSONObject) country.get("languages")).keySet();
            for (Object language : languages) {
                String langIso3 = (String) language;
                if(langIso3ToIso2.get(langIso3) != null) {
                    country1.getLanguages().add(langIso3ToIso2.get(langIso3));
                }
            }
            countryRepository.save(country1);
        }
        countryRepository.flush();
    }

//    @PostConstruct
    public void initi18nData() throws IOException, ParseException {
        saveCountries();
        saveLanguages();
        createCountryLanguageMappings();
    }
}
