package ch.notezilla.model;

import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@CrossOrigin
public class Notes {

    private int idNotes;
    private String noteTitle;
    private String noteContent;

    @Id
    public int getIdNotes() {
        return idNotes;
    }

    public Notes() {
    }

    public void setIdNotes(int idNotes) {
        this.idNotes = idNotes;
    }

    public String getNotetitle() {
        return noteTitle;
    }

    public void setNotetitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNotecontend() {
        return noteContent;
    }

    public void setNotecontend(String noteContent) {
        this.noteContent = noteContent;
    }


}
