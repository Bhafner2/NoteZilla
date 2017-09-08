package ch.notezilla.repository;

import ch.notezilla.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Notes, Integer> {
}
