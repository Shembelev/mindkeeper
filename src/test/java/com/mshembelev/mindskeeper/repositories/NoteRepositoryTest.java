package com.mshembelev.mindskeeper.repositories;

import com.mshembelev.mindskeeper.models.NoteModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    private NoteModel noteModel;
    @BeforeEach
    public void init(){
        noteModel = NoteModel.builder().text("New text").title("New title").userId(0L).codeImage("code image").build();
    }

    @Test
    public void NoteRepository_SaveAll_ReturnSavedNote(){
        NoteModel savedNote = noteRepository.save(noteModel);

        Assertions.assertThat(savedNote).isNotNull();
        Assertions.assertThat(savedNote.getId()).isGreaterThan(0);
    }

    @Test
    public void NoteRepository_FindNoteById_ReturnNote(){
        NoteModel savedNote = noteRepository.save(noteModel);
        Optional<NoteModel> findNote = noteRepository.findById(savedNote.getId());

        Assertions.assertThat(findNote.isPresent()).isTrue();
        Assertions.assertThat(findNote.get()).isNotNull();
        Assertions.assertThat(findNote.get().getId()).isGreaterThan(0);
    }

    @Test
    public void NoteRepository_DeleteById_ReturnNull(){
        NoteModel savedNote = noteRepository.save(noteModel);
        noteRepository.deleteById(savedNote.getId());
        Optional<NoteModel> allNotes = noteRepository.findById(savedNote.getId());

        Assertions.assertThat(allNotes.isPresent()).isFalse();
    }

    @Test
    public void NoteRepository_FindNoteByUserId_ReturnNote(){
        Long userId = 0L;
        NoteModel savedNote = noteRepository.save(noteModel);
        List<NoteModel> notesByUserId = noteRepository.findNoteModelsByUserId(userId);

        Assertions.assertThat(notesByUserId.size()).isGreaterThan(0);
    }
}
