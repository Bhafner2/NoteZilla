package ch.notezilla.Controller;

import ch.notezilla.model.Notes;
import ch.notezilla.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping(path = "/notes")
public class NoteController {

    @Autowired
    NoteRepository noteRepo;

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Notes> getAllContents() {
        return noteRepo.findAll();
    }

    @GetMapping(value = "/all/{id}")
    public @ResponseBody
    Notes getContentById(@PathVariable("id") int id) {
        return noteRepo.findOne(id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public @ResponseBody
    Notes deleteContent(@PathVariable("id") int id) {
        noteRepo.delete(id);
        return null;
    }

    @PutMapping(value = "/edit/{id}")
    public @ResponseBody
    Notes updateContent(@PathVariable("id") int id, String title, String content) {
        Notes noteModel = noteRepo.findOne(id);
        noteModel.setNotetitle(title);
        noteModel.setNotecontend(content);
        noteRepo.save(noteModel);
        return noteRepo.findOne(id);
    }

    @PutMapping(value = "/new/")
    public @ResponseBody
    Notes addNewContent(String title, String content) {
        Notes noteModel = new Notes();
        noteModel.setNotetitle(title);
        noteModel.setNotecontend(content);
        return noteRepo.save(noteModel);
    }
}
