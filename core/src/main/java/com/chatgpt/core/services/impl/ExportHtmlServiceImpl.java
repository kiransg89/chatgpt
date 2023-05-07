package com.chatgpt.core.services.impl;

import com.chatgpt.core.services.ExportHtmlService;
import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.wcm.api.WCMMode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.engine.SlingRequestProcessor;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component(service = ExportHtmlService.class, immediate = true, name = "HTML Content Export service")
public class ExportHtmlServiceImpl implements ExportHtmlService{

    @Reference
    ResourceResolverFactory resolverFactory;

    @Reference
    private SlingRequestProcessor slingProcessor;

    @Reference
    private RequestResponseFactory requestResponseFactory;

    Map<String, Object> wgServiceParam = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
            "service-user-name");

    @Override
    public String getExportHTMLContent(String xfPath) {
        if (StringUtils.isNotEmpty(xfPath)) {
            try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(wgServiceParam)) {
                if (StringUtils.isNotEmpty(xfPath)) {
                    retrieveContent(xfPath, resolver);
                    //End point details : you can also use http client builder factory
                    String url = "https://example.com/api";
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    Document document = Jsoup.parse(retrieveContent(xfPath, resolver));
                    JSONObject jsonObject = new JSONObject();
                    Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
                    jsonObject.put("from", "aem");
                    jsonObject.put("html", gson.toJson(document.body().html()));
                    return sendPost(url, headers, gson.toJson(jsonObject));
                }
            } catch (LoginException | ServletException | IOException | JSONException e) {
                log.error("An error occurred while proccessing the request {} ", e.getMessage());
            }
        }
        return StringUtils.EMPTY;
    }

    protected String retrieveContent(String requestUri, ResourceResolver resourceResolver)
            throws ServletException, IOException {
        HttpServletRequest req = requestResponseFactory.createRequest("GET", requestUri + ".nocloudconfigs.html");
        WCMMode.DISABLED.toRequest(req);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpServletResponse res = requestResponseFactory.createResponse(out);
        slingProcessor.processRequest(req, res, resourceResolver);
        return out.toString();
    }

    public static String sendPost(String urlString, Map<String, String> headers, String body) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        // Set headers
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }

        // Write body to output stream
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Read response from input stream
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}
