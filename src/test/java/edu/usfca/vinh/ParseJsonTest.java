package edu.usfca.vinh;

import junit.framework.TestCase;

public class ParseJsonTest extends TestCase {
    ParseJson parser;
    String url = "https://musicbrainz.org/ws/2/";
    DownloadManager download = new DownloadManager(url);
    String musicJson;

    public void setUp() throws Exception {
        super.setUp();
        parser = new ParseJson();
        String type = "recording", format = "json", query = "Christmas"; int limit = 3;
        musicJson = download.httpGet(type,format,query,limit);
    }

    public void testParseJSONSAddSongs() {
        parser.parseJSONSAddSongs(musicJson);
        System.out.println(parser.getSongList());
    }
}