package edu.usfca.vinh;
//Student: Vinh Duy Le   Class: CS514
/* from the sqlite-jdbc github repo */

import edu.usfca.vinh.model.Album;
import edu.usfca.vinh.model.Artist;
import edu.usfca.vinh.model.Song;

import java.sql.*;
import java.util.List;

public class DatabaseRepository {
    private Connection connection = null;
    private Statement statement = null;
    private int retry;

    /**
     * This is constructor,
     * init connection to database,
     * connection will retry 3 times if unable to insert into database.
     */
    public DatabaseRepository() {
        initConnectionDB();
        retry = 3;
    }

    /**
     * initConnectionDB to music.db
     */

    public void initConnectionDB() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param sql this from song/album/artist toSql() method to store in music.db
     */

    public void executeInsert(String sql) {
        for (int i = 0; i < retry; i++) {
            try {
                if (connection == null || statement == null || statement.isClosed()) {
                    initConnectionDB();
                }
                statement.executeUpdate(sql);
                return;
            } catch (SQLException e) {
                // if the error message is "out of memory",
                // it probably means no database file is found
                System.err.println(e.getMessage());
            }
        }
     }

     public void insertSong(Song song) {
        executeInsert(song.toSQL());
     }

    public void insertSongs(List<Song> songs) {
        for (Song song : songs) {
            insertSong(song);
        }
    }

    public void insertAlbum(Album album) {
        executeInsert(album.toSQL());
    }

    public void insertAlbums(List<Album> albums) {
        for (Album album : albums) {
            insertAlbum(album);
        }
    }

    public void insertArtist(Artist artist) {
        executeInsert(artist.toSQL());
    }

    public void insertArtists(List<Artist> artists) {
        for (Artist artist : artists) {
            insertArtist(artist);
        }
    }

    /**
     * Print data in music.db from songs, albums, artists tables.
     */

    public void printMusicDB() {
        try {
            if (connection == null || statement == null || statement.isClosed()) {
                initConnectionDB();
            }
            ResultSet rs = statement.executeQuery("select songs.id, songs.name, " +
                    "albums.id as album_id, albums.name as album_name, " +
                    "artists.id as artist_id, artists.name as artist_name " +
                    "from songs " +
                    "inner join artists on artists.id = songs. artist " +
                    "inner join albums on albums.id = songs.album");
            Song s = new Song();
            while (rs.next()) {
                System.out.println(s.fromSQL(rs).toString());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }
}
