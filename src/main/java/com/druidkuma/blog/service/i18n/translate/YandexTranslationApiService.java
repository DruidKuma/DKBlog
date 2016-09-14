package com.druidkuma.blog.service.i18n.translate;

import com.druidkuma.blog.service.property.PropertyService;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@Service
public class YandexTranslationApiService implements TranslationApiService {

    @Autowired
    private PropertyService propertyService;

    private static final String SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
    private static final String TRANSLATION_LABEL = "text";
    protected static final String ENCODING = "UTF-8";
    private static final String PARAM_API_KEY = "key=";
    private static final String PARAM_LANG_PAIR = "&lang=";
    private static final String PARAM_TEXT = "&text=";

    private static String API_KEY;

    @PostConstruct
    public void init() {
        API_KEY = propertyService.getString("yandex_api_key");
    }

    @Override
    public String getType() {
        return "yandex";
    }

    @Override
    @SneakyThrows
    public String translate(String source, String srcLangIso, String destLangIso) {
        validateServiceState(source);
        final String params =
                PARAM_API_KEY + URLEncoder.encode(API_KEY, ENCODING)
                        + PARAM_LANG_PAIR + URLEncoder.encode(String.format("%s-%s", srcLangIso, destLangIso), ENCODING)
                        + PARAM_TEXT + URLEncoder.encode(source, ENCODING);
        final URL url = new URL(SERVICE_URL + params);

        try {
            return retrievePropArrString(url, TRANSLATION_LABEL).trim();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<String, String> translate(Map<String, String> srcTranslations, String srcLangIso, String destLangIso) {
        Map<String, String> destTranslations = Maps.newHashMap();
        for (Map.Entry<String, String> srcEntry : srcTranslations.entrySet()) {
            destTranslations.put(srcEntry.getKey(), translate(srcEntry.getValue(), srcLangIso, destLangIso));
        }
        return destTranslations;
    }

    private static void validateServiceState(final String text) throws UnsupportedEncodingException {
        if(API_KEY == null || API_KEY.length() < 27) {
            throw new RuntimeException("INVALID_YANDEX_API_KEY - Please set the API Key with your Yandex API Key");
        }
        final int byteLength = text.getBytes(ENCODING).length;
        if(byteLength > 10240) {
            throw new RuntimeException("TEXT_TOO_LARGE");
        }
    }

    /**
     * Forms a request, sends it using the GET method and returns the contents of the array of strings
     * with the given label, with multiple strings concatenated.
     */
    private String retrievePropArrString(final URL url, final String jsonValProperty) throws Exception {
        final String response = retrieveResponse(url);
        String[] translationArr = jsonObjValToStringArr(response, jsonValProperty);
        String combinedTranslations = "";
        for (String s : translationArr) {
            combinedTranslations += s;
        }
        return combinedTranslations.trim();
    }

    private String retrieveResponse(final URL url) throws Exception {
        final HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
        uc.setRequestProperty("Content-Type","text/plain; charset=" + ENCODING);
        uc.setRequestProperty("Accept-Charset",ENCODING);
        uc.setRequestMethod("GET");

        try {
            final int responseCode = uc.getResponseCode();
            final String result = inputStreamToString(uc.getInputStream());
            if(responseCode!=200) {
                throw new Exception("Error from Yandex API: " + result);
            }
            return result;
        } finally {
            uc.disconnect();
        }
    }

    // Helper method to parse a JSONObject containing an array of Strings with the given label.
    private static String[] jsonObjValToStringArr(final String inputString, final String subObjPropertyName) throws Exception {
        JSONObject jsonObj = (JSONObject) JSONValue.parse(inputString);
        JSONArray jsonArr = (JSONArray) jsonObj.get(subObjPropertyName);
        return jsonArrToStringArr(jsonArr.toJSONString(), null);
    }

    // Helper method to parse a JSONArray. Reads an array of JSONObjects and returns a String Array
    // containing the toString() of the desired property. If propertyName is null, just return the String value.
    private static String[] jsonArrToStringArr(final String inputString, final String propertyName) throws Exception {
        final JSONArray jsonArr = (JSONArray)JSONValue.parse(inputString);
        String[] values = new String[jsonArr.size()];

        int i = 0;
        for(Object obj : jsonArr) {
            if(propertyName!=null&&propertyName.length()!=0) {
                final JSONObject json = (JSONObject)obj;
                if(json.containsKey(propertyName)) {
                    values[i] = json.get(propertyName).toString();
                }
            } else {
                values[i] = obj.toString();
            }
            i++;
        }
        return values;
    }

    /**
     * Reads an InputStream and returns its contents as a String.
     * Also effects rate control.
     * @param inputStream The InputStream to read from.
     * @return The contents of the InputStream as a String.
     * @throws Exception on error.
     */
    private String inputStreamToString(final InputStream inputStream) throws Exception {
        final StringBuilder outputBuilder = new StringBuilder();
        String string;
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
            while (null != (string = reader.readLine())) {
                outputBuilder.append(string.replaceAll("\uFEFF", ""));
            }
        }
        return outputBuilder.toString();
    }
}
