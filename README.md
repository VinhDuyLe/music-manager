# Music Manager

Music Manager allows to manage a music collection and load information from third-party repositories.
User Interface with text-based commands display a prompt allowing user to have options menu to choose upload full information or partially specify name of song/artist/ablum to Playlist or export to XML file. 

## Purpose

This is an assignment of CS514-Homework 7: Putting it all together.

## Tasks

1. Implement a UI that can take basic text-based commands.
   ```
   MusicManger.class
   Method start()
   ```
2. Your songs, artists, and albums should all be stored in a SQLite database, with separate tables for each datatype.
   ```
   MusicManger.class
   Method addMusicToDB() and addMusicsToDB() allow to add single song or list of songs to database
   
   DatabaseRepository.class
   This class have initconnectionDB() connecting DB, executeInsert() store data to DB, and method printMusicDB() to print data from DB
   ```
 
3. Generate XML files containing a playlist.
   ```
   Playlist.class
   Method exportToXML() alow to export playlist to file exportXML.xml
   ```
4. Allow a user to partially specify a song, artist, or album, and then use a third-party tool to fill in missing details.
   
   Using REST-based API with format JSON from [MusicBrainz](https://musicbrainz.org/doc/MusicBrainz_API)

   ```
   MusicManager.class
   Method createSongsAndDownload() prompt user to input song/artist/album's name query.
   
   DownloadManager.class
   Url format: "https://musicbrainz.org/ws/2/" + type + "?fmt=" + format + "&query=" + query + "&limit=" + limit
      type: "recording"
      format: "json"
      query: user input query from command-line
      limit: 1-20 songs 
   This class create init() connecting to MusicBrainz API and getHttp() get request to download String musicJson.
   
   ParseJson.class
   Method parseJSONSAddSongs() to parse String musicJson and return List of Songs, HashMap Artist, and HashMap Album.
   
   ```

## What next

Two pieces that we have not integrated yet:

1. A tool for playing music files. The java.io.* package provides support for this.
2. A GUI for managing music: using a standalone application Swing, but most modern approaches instead do this inside a web browser using Javascript.

## License
CS514 OOP class.
[USFCA](https://www.usfca.edu/arts-sciences/graduate-programs/computer-science-bridge)