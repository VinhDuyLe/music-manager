package edu.usfca.vinh.model;//Student: Vinh Duy Le   Class: CS514

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@XmlRootElement
public class Playlist {
    private ArrayList<Song> songs;

    public Playlist() {
        songs = new ArrayList<Song>();
    }

    public void addSong(Song s) {
        songs.add(s);
    }

    public void addAllSongs(List<Song> songs) {
        this.songs.addAll(songs);
    }

    public void deleteSong(Song s) {
        songs.remove(s);
    }

    @XmlElement(name = "song")
    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void findDefinitelyDuplicate() {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < songs.size(); i++) {
            for (int j = i + 1; j < songs.size(); j++) {
                if (songs.get(i).equals(songs.get(j))) {
                    System.out.println("This song is definitely duplicate: " + "\n" + songs.get(i) + "\n" + songs.get(j) + "\n" + "Input Y/N to delete this song:");
                    if (sc.nextLine().equals("Y")) {
                        songs.remove(songs.get(i));
                    }
                }
            }
        }
    }

    public void findPossiblyDuplicate() {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < songs.size(); i++) {
            for (int j = i + 1; j < songs.size(); j++) {
                if ((songs.get(i).getName().equals(songs.get(j).getName())
                        && (songs.get(i).getPerformer().equals(songs.get(j).getPerformer())
                        || songs.get(i).getAlbum().equals(songs.get(j).getAlbum())  ))
                        || (songs.get(i).getPerformer().equals(songs.get(j).getPerformer())
                        && songs.get(i).getAlbum().equals(songs.get(j).getAlbum())
                        && songs.get(i).getName().toLowerCase().replaceAll("\\p{Punct}","").trim()
                        .equals(songs.get(j).getName().toLowerCase().replaceAll("\\p{Punct}","").trim())    )
                ) {

                    System.out.println("This two songs is possibly duplicate: " + "\n" + songs.get(i) + "\n" + songs.get(j) + "\n" + "Input Y/N to delete this song:");
                    if (sc.nextLine().equals("Y")) {
                        songs.remove(songs.get(i));
                    }
                }
            }
        }
    }

    public ArrayList<Song> mergeTwoArrayList(ArrayList<Song> otherListOfSongs) {
        for (Song song : otherListOfSongs) {
            if (!this.songs.contains(song))
                songs.add(song);
        }
        return songs;
    }

    public ArrayList<Song> sortPlayList() {
        ArrayList<Song> sortSongs = (ArrayList<Song>) songs.clone();
        Collections.sort(sortSongs, (o1, o2) -> o2.getLiked() - o1.getLiked());

        return sortSongs;
    }

    public ArrayList<Song> randomShuffle() {
        Random r = new Random();
        Collections.shuffle(this.songs, r);
        return songs;
    }

    public Playlist randomFeaturePlaylist() {
        Playlist randomPlaylist = new Playlist();
        Random r = new Random();
        for (Song song : this.songs) {
            if(song.getGenre().equals("jazz") || song.getBPM() > 120) {
                randomPlaylist.getSongs().add(song);
            }
        }
        Collections.shuffle(randomPlaylist.getSongs(), r);
        return randomPlaylist;
    }

    public void exportToXML(String fileName) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Playlist.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            File file = new File(fileName);
            jaxbMarshaller.marshal(this, file);

        }catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void exportToJSON(String filename) {
        JSONObject obj = new JSONObject();
        JSONArray listArtists = new JSONArray();
        JSONArray listSongs = new JSONArray();
        JSONArray listAlbums = new JSONArray();

        for (Song song : this.songs) {
            JSONObject childAlbumObject = new JSONObject();
            childAlbumObject.put("id", song.getAlbum().getEntityID());
            childAlbumObject.put("name", song.getAlbum().getName());

            listAlbums.add(childAlbumObject);

            JSONObject childArtistObject = new JSONObject();
            childArtistObject.put("id", song.getPerformer().getEntityID());
            childArtistObject.put("name", song.getPerformer().getName());

            listArtists.add(childArtistObject);

            JSONObject childSongObject = new JSONObject();
            childSongObject.put("id", song.getEntityID());
            childSongObject.put("title", song.getName());
            childSongObject.put("artist", childArtistObject);
            childSongObject.put("album", childAlbumObject);

            listSongs.add(childSongObject);
        }

        obj.put("albums", listAlbums);
        obj.put("artists", listArtists);
        obj.put("songs", listSongs);

        try (FileWriter file = new FileWriter(filename)) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
