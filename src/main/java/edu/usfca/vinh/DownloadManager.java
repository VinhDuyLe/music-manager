package edu.usfca.vinh;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class DownloadManager {
    private CloseableHttpClient httpClient;
    private String url;
    private int retry;

    /**
     * This is constructor,
     * init connection to external source,
     * @param url Http url from external source,
     * Connection will retry 3 times if httpClient request fail.
     */

    public DownloadManager(String url) {
        init();
        this.url = url;
        retry = 3;
    }

    public void init() {
        try {
            httpClient = HttpClientBuilder.create().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param type type "recording" include: artists, releases, isrcs, url-rels,
     * @param format format "json" or "xml",
     * @param query query from user command-line,
     * @param limit number of songs,
     * @return return String musicJson.
     */

    public String httpGet(String type, String format, String query, int limit) {
        // Retry download if it failed
        for (int i = 0; i < retry; i++) {
            try {
                if (httpClient == null) {
                    init();
                }
                String newUrl = url + type + "?fmt=" + format + "&query=" + query + "&limit=" + limit;
                HttpGet request = new HttpGet(newUrl);
                request.addHeader("content-type", "application/json");
                HttpResponse result = httpClient.execute(request);
                if (result.getStatusLine().getStatusCode() < 299) {
                    return EntityUtils.toString(result.getEntity(), "UTF-8");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    public void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
