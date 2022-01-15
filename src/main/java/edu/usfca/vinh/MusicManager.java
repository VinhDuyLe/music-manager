package edu.usfca.vinh;

import edu.usfca.vinh.model.Album;
import edu.usfca.vinh.model.Artist;
import edu.usfca.vinh.model.Playlist;
import edu.usfca.vinh.model.Song;

import java.util.List;
import java.util.Scanner;

public class MusicManager {
    private Playlist playlist;
    private DatabaseRepository database;
    private DownloadManager download;

    /**
     * This constructor,
     * init connection of Database,
     * init connection of API.
     */
    public MusicManager() {
        database = new DatabaseRepository();
        playlist = new Playlist();
        download = new DownloadManager("https://musicbrainz.org/ws/2/");
    }

    /**
     * TASK 1: Implement a UI that can take basic text-based commands,
     * Prompt user to choose: adding song to playlist or adding song with download info and Export data to XML.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        outSideLoop:
        while (true) {
            System.out.println("Choose from of the following menu options to add song to current Playlist:");
            System.out.println("1 - Add a song to current playlist");
            System.out.println("2 - Add song with download info");
            System.out.println("3 - Export to XML");
            System.out.println("4 - Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createNewSong(scanner);
                    break;
                case 2:
                    createSongsAndDownload(scanner);
                    break;
                case 3:
                    playlist.exportToXML("src/main/java/edu/usfca/vinh/exportXML.xml");
                    break;
                default:
                    database.close();
                    download.close();
                    break outSideLoop;
            }
        }
    }

    /**
     *
     * @param scanner Get name of song, artist, album from user, then add to current playlist.
     */

    public void createNewSong(Scanner scanner) {
        System.out.println("What is your Song name");
        String s = scanner.nextLine();
        System.out.println("What is your Artist name");
        String art = scanner.nextLine();
        System.out.println("What is your Album name");
        String al = scanner.nextLine();

        Song song = new Song(s);
        Artist artist = new Artist(art);
        Album album = new Album(al);
        album.setArtist(artist);
        song.setPerformer(artist);
        song.setAlbum(album);

        addMusicToDB(song, album, artist);
        playlist.addSong(song);
    }


    /**
     * TASK 2: Your songs, artists, and albums should all be stored in a SQLite database,
     * @param song Song inserts to Database,
     * @param album Album inserts to Database,
     * @param artist Artist inserts to Database.
     */

    public void addMusicToDB(Song song, Album album, Artist artist){
        database.insertArtist(artist);
        database.insertAlbum(album);
        database.insertSong(song);
    }

    public void addMusicsToDB(List<Song> songs, List<Album> albums, List<Artist> artists){
        database.insertArtists(artists);
        database.insertAlbums(albums);
        database.insertSongs(songs);
    }

    /**
     * TASK 4: Allow a user to partially specify a song, artist, or album, and then use a third-party tool to fill in missing details.
     * @param scanner Get query song name from user and add more information from Musicbrainz API.
     * API get request musicJson.
     * Parser musicJson return list of songs, albums, and artists.
     * Add songs, albums, artists to Database.
     */

    public void createSongsAndDownload(Scanner scanner) {
        System.out.println("Input your name of song: ");
        String songQuery = scanner.nextLine();
        String musicJson = download.httpGet("recording", "json",songQuery,3);
        ParseJson parser = new ParseJson();
        parser.parseJSONSAddSongs(musicJson);
        List<Song> songs = parser.getSongList();
        playlist.addAllSongs(songs);
        List<Album> albums = parser.getHmAlbum();
        List<Artist> artists = parser.getHmArtist();

        addMusicsToDB(songs, albums, artists);
    }

    public static void main(String[] args) {
        MusicManager manager = new MusicManager();
        manager.start();
    }
}
