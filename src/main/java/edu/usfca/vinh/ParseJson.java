package edu.usfca.vinh;

import edu.usfca.vinh.model.Album;
import edu.usfca.vinh.model.Artist;
import edu.usfca.vinh.model.Song;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseJson {
    private List<Song> songList;
    private HashMap<String, Artist> hmArtist;
    private HashMap<String, Album> hmAlbum;

    /**
     * This is constructor and instantiated Object List of songs, Hashmap Artist, Hashmap Album.
     */

    public ParseJson() {
        songList = new ArrayList<>();
        hmArtist = new HashMap<>();
        hmAlbum = new HashMap<>();
    }

    public List<Song> getSongList() {
        return songList;
    }

    public List<Artist> getHmArtist() {
        return new ArrayList<Artist>(this.hmArtist.values());
    }

    public List<Album> getHmAlbum() {
        return new ArrayList<Album>(this.hmAlbum.values());
    }

    /**
     *
     * @param hmArtist hmArtist is Hashmap<id,name> of Artist,
     * @param artistsArray artistsArray is JSONArray of artist from musicJSON,
     * @return put Artist to hmArtist and return Artist.
     */
    private Artist parseArtistJSON(HashMap<String,Artist> hmArtist, JSONArray artistsArray) {
        if (!artistsArray.isEmpty()) {
            String idArtist = (String) ((JSONObject) ((JSONObject) artistsArray.get(0)).get("artist")).get("id");
            String nameArtist = (String) ((JSONObject) ((JSONObject) artistsArray.get(0)).get("artist")).get("name");
            if (!hmArtist.containsKey(idArtist)) {
                Artist artistNew = new Artist(nameArtist);
                hmArtist.put(idArtist, artistNew);
            }
            return hmArtist.get(idArtist);
        }
        return null;
    }

    /**
     *
     * @param hmAlbum hmAlbum is Hashmap<id,name> of Album,
     * @param albumsArray albumsArray is JSONArray of album from musicJSON,
     * @return put Album to hmAlbum and return Album.
     */

    private Album parseAlbumJSON(HashMap<String, Album> hmAlbum, JSONArray albumsArray) {
        if (!albumsArray.isEmpty()) {
            String idAlbum = (String)((JSONObject)albumsArray.get(0)).get("id");
            String nameAlbum = (String)((JSONObject)albumsArray.get(0)).get("title");
            if (!hmAlbum.containsKey(idAlbum)) {
                Album albumNew = new Album(nameAlbum);
                hmAlbum.put(idAlbum,albumNew);
            }
            return hmAlbum.get(idAlbum);
        }
        return null;
    }

    /**
     *
     * @param hmArtist hmArtist is Hashmap<id,name> of Artist,
     * @param hmAlbum hmAlbum is Hashmap<id,name> of Album,
     * @param songsArray songsArray is JSONArray of song from musicJSON,
     * Add song to songList.
     */

    private void parseSongJSON (HashMap<String,Artist> hmArtist, HashMap<String,Album> hmAlbum, JSONArray songsArray) {
        for (int i = 0; i < songsArray.size(); i++) {
            String title = (String)((JSONObject)songsArray.get(i)).get("title");
            JSONArray artistsArray = (JSONArray)((JSONObject)songsArray.get(i)).get("artist-credit");
            JSONArray albumsArray = (JSONArray)((JSONObject)songsArray.get(i)).get("releases");

            Song song = new Song(title);
            Album album = parseAlbumJSON(hmAlbum, albumsArray);
            Artist artist = parseArtistJSON(hmArtist, artistsArray);
            if (artist != null) {
                if (album != null)
                    album.setArtist(artist);
                song.setPerformer(artist);
            }
            if (album != null)
                song.setAlbum(album);
            songList.add(song);
        }
    }

    /**
     *
     * @param musicJson musicJson is return getHttp() on Musicbrainz API from DownloadManager.class,
     * parse musicJson and get songsArray.
     */

    public void parseJSONSAddSongs(String musicJson) {

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(musicJson);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray songsArray = (JSONArray) jsonObject.get("recordings");
            parseSongJSON(hmArtist, hmAlbum, songsArray);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
