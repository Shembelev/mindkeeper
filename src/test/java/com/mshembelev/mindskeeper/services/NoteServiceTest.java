package com.mshembelev.mindskeeper.services;

import com.mshembelev.mindskeeper.dto.note.CreateNoteRequest;
import com.mshembelev.mindskeeper.dto.note.UpdateNoteRequest;
import com.mshembelev.mindskeeper.models.NoteModel;
import com.mshembelev.mindskeeper.repositories.NoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    private NoteModel note;
    private CreateNoteRequest noteRequest;

    @BeforeEach
    public void init(){
        note = NoteModel.builder().userId(0L).text("Some text").title("Title").id(0L).build();
        noteRequest = CreateNoteRequest.builder().text("Some text").title("Title").build();
    }
    @Test
    public void NoteService_CreateNote_ReturnsNote(){
        when(noteRepository.save(any(NoteModel.class))).thenReturn(note);
        NoteModel savedNote = noteService.createNote(noteRequest, 0L);
        Assertions.assertThat(savedNote).isNotNull();
    }

    @Test
    public void NoteService_CheckNotesOwner_ReturnIsOwner(){
        when(noteRepository.findNoteModelById(Mockito.anyLong())).thenReturn(Optional.ofNullable(note));
        Boolean isOwner = noteService.checkNotesOwner(0L, 0L);
        Boolean isFakeOwner = noteService.checkNotesOwner(0L, 1L);
        Assertions.assertThat(isOwner).isTrue();
        Assertions.assertThat(isFakeOwner).isFalse();
    }

    @Test
    public void NoteService_UpdateNote_ReturnUpdatedNote(){
        UpdateNoteRequest updateNoteRequest = UpdateNoteRequest.builder()
                .text("New text")
                .codeImage("new code image")
                .id(0L)
                .title("New title")
                .build();

        when(noteRepository.save(any())).thenReturn(note);
        when(noteRepository.findNoteModelById(any())).thenReturn(Optional.ofNullable(note));

        NoteModel updatedNote = noteService.updateNote(updateNoteRequest, 0L);

        Assertions.assertThat(updatedNote).isNotNull();
    }

    @Test
    public void NoteService_GetAllUserNote_ReturnAllUserNote(){
        when(noteRepository.findNoteModelsByUserId(any())).thenReturn(List.of(note));
        Long userId = 0L;
        List<NoteModel> allUserNotes = noteService.getAllUserNotes(userId);

        Assertions.assertThat(allUserNotes).isNotNull();
    }

    @Test
    public void NoteService_DeleteNote_ReturnVoid(){
        Long userId = 0L;
        Long fakeUserId = 1L;
        Long noteId = 0L;

        doNothing().when(noteRepository).deleteById(anyLong());
        when(noteRepository.findNoteModelById(any())).thenReturn(Optional.ofNullable(note));

        Assertions.assertThatCode(() -> noteService.deleteNote(noteId, userId)).doesNotThrowAnyException();
        Assertions.assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(() -> noteService.deleteNote(noteId, fakeUserId));
    }

}
