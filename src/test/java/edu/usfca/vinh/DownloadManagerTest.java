package edu.usfca.vinh;

import junit.framework.TestCase;

public class DownloadManagerTest extends TestCase {
    DownloadManager download;
    String url = "https://musicbrainz.org/ws/2/";


    public void setUp() throws Exception {
        super.setUp();
        download = new DownloadManager(url);
    }

    public void testHttpGet() {
        String type = "recording", format = "json", query = "summer"; int limit = 5;
        String musicJson = download.httpGet(type,format,query,limit);
        System.out.println(musicJson);
    }
}