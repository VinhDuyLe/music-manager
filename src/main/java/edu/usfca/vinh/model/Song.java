package edu.usfca.vinh.model;

import java.sql.ResultSet;
import java.sql.SQLException;

//Student: Vinh Duy Le   Class: CS514
public class Song extends Entity {
    protected Album album;
    protected Artist performer;
    protected String genre;
    protected int liked;
    protected int BPM;
    protected boolean hasBeenPlayed;
    protected String mood;
    protected SongInterval duration;
    protected Playlist playlist;

    public Song() {
        //super();
        genre = "";
        liked = 0;
        BPM = 0;
        hasBeenPlayed = false;
        mood = "";
    }

    public Song(String name) {
        super(name);
        genre = "";
        liked = 0;
        BPM = 0;
        hasBeenPlayed = false;
        mood = "";
        duration = new SongInterval();
    }

    public void setLength(int length) {
        duration = new SongInterval(length);
    }

    public String showLength() {
        return duration.toString();
    }

    public String getGenre() {
       return genre;
   }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artist getPerformer() {
        return performer;
    }

    public void setPerformer(Artist performer) {
        this.performer = performer;
    }

    public boolean equals(Song otherSong) {
        return this.name.equals(otherSong.getName())
                && (this.album != null && otherSong.getAlbum() != null && this.album.equals(otherSong.getAlbum()))
                && (this.performer != null && otherSong.getPerformer() != null && this.performer.equals(otherSong.getPerformer()));
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public void increaseLike() {
        this.liked++;
    }

    public int getBPM() {
        return BPM;
    }

    public void setBPM(int BPM) {
        this.BPM = BPM;
    }

    public boolean isHasBeenPlayed() {
        return hasBeenPlayed;
    }

    public void setHasBeenPlayed(boolean hasBeenPlayed) {
        this.hasBeenPlayed = hasBeenPlayed;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String toString() {
        return "{ " +super.toString() + " " + this.performer + " " + this.album + " }"; //this.duration;

    }
    public String toSQL() {
        return "insert into songs (id, name, artist, album) values (" + this.entityID + ", '" + this.name + "', " + performer.entityID + ", " + album.entityID + ");";
    }

    public Song fromSQL(ResultSet rs) {
        Song s = new Song();
        try {
            s.entityID = rs.getInt("id");
            s.name = rs.getString("name");
            Artist art = new Artist();
            art.entityID = rs.getInt("artist_id");
            art.name = rs.getString("artist_name");
            s.setPerformer(art);
            Album alb = new Album();
            alb.entityID = rs.getInt("album_id");
            alb.name = rs.getString("album_name");
            s.setAlbum(alb);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return s;
    }




}
