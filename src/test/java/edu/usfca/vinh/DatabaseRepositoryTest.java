package edu.usfca.vinh;

import junit.framework.TestCase;

import java.util.concurrent.ThreadLocalRandom;

public class DatabaseRepositoryTest extends TestCase {
    DatabaseRepository database;
    String artistSql = "", albumSql = "", songSql ="";
    public void setUp() throws Exception {
        super.setUp();
        database = new DatabaseRepository();
        int counter = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
        int idArtist = counter + 1, idAlbum = counter + 2, idSong = counter + 3;

        artistSql = "insert into artists (id, name) values ("+ idArtist +", 'justin bieber');";
        albumSql = "insert into albums (id, name, artist) values ("+ idAlbum +", 'Love', "+ idArtist +");";
        songSql = "insert into songs (id, name, artist, album) values ("+ idSong +", 'Love Yourself', "+ idArtist +", "+ idAlbum +");";
    }

    public void testExecuteInsert() {
        database.executeInsert(artistSql);
        database.executeInsert(albumSql);
        database.executeInsert(songSql);

        database.printMusicDB();
    }
}