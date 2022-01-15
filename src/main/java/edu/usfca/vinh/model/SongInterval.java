package edu.usfca.vinh.model;

/* Vinh Le Duy Class: CS514*/
public class SongInterval {
    private int length;

    public SongInterval() {
        length = 0;
    }

    public SongInterval(int l) {
        this.length = l;
    }

    public String toString() {
        int minutes = length / 60;
        int seconds = length % 60;
        return String.format("%d:%d", minutes, seconds);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
