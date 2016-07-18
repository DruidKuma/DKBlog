package com.druidkuma.blog.util;

import com.druidkuma.blog.dao.BlogEntryRepository;
import com.druidkuma.blog.domain.BlogEntry;
import com.druidkuma.blog.domain.Comment;
import com.druidkuma.blog.domain.Content;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/15/16
 */
@Service
public class WordpressBlogMigrator {

    @Autowired
    private BlogEntryRepository blogEntryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @PostConstruct
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
                    .blogPostId(Integer.parseInt(record.get(1)))
                    .author(record.get(2))
                    .email(record.get(3))
                    .authorIp(record.get(5))
                    .creationDate(LocalDateTime.parse(record.get(6), formatter).toInstant(ZoneOffset.UTC))
                    .body(record.get(8))
                    .isApproved("1".equals(record.get(10)))
                    .authorUserAgent(record.get(11))
                    .parentId(StringUtils.isEmpty(record.get(13)) ? null : Integer.parseInt(record.get(13)))
                    .build());
        }
        System.out.println("Finished parsing comments");

        System.out.println("Mapping blog etries with comments...");
        for (Map.Entry<Integer, Comment> entry : commentMap.entrySet()) {
            Comment comment = entry.getValue();
            if(comment.getParentId() != null) {
                comment.addParent(commentMap.get(comment.getParentId()));
                BlogEntry blogEntry = entryMap.get(comment.getBlogPostId());
                if(blogEntry == null) continue;
                if(blogEntry.getComments() == null) blogEntry.setComments(new ArrayList<>());
                blogEntry.getComments().add(comment);
            }
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

    @PostConstruct
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
}
