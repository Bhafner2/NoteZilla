package ch.teko.benj.notezillaapp.objects;

/**
 * Created by b7hafnb on 09.09.2017.
 */

public class Note {
    private int id;
    private String titel;
    private String contend;

    public Note(int id, String titel, String contend) {
        this.id = id;
        this.titel = titel;
        this.contend = contend;
    }

    public Note(String titel, String contend) {
        this.titel = titel;
        this.contend = contend;
    }

    @Override
    public String toString() {
        return titel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;

    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getContend() {
        return contend;
    }

    public void setContend(String contend) {
        this.contend = contend;
    }

}
