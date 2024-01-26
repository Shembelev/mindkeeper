package com.mshembelev.mindskeeper.services;

import com.mshembelev.mindskeeper.dto.note.CreateNoteRequest;
import com.mshembelev.mindskeeper.dto.note.DeleteNoteRequest;
import com.mshembelev.mindskeeper.dto.note.UpdateNoteRequest;
import com.mshembelev.mindskeeper.models.NoteModel;
import com.mshembelev.mindskeeper.models.UserModel;
import com.mshembelev.mindskeeper.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.nio.file.FileSystemNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository repository;
    private final UserService userService;

    /**
     * Сохранение заметки
     *
     * @return сохраненная заметка
     */
    public NoteModel saveNote(NoteModel note){
        return repository.save(note);
    }

    /**
     * Создание заметки
     *
     * @return созданная заметка
     */
    public ResponseEntity<?> createNote(CreateNoteRequest request) {
            UserModel user = userService.getCurrentUser();
            NoteModel note = NoteModel.builder()
                    .text(request.getText())
                    .userId(user.getId())
                    .build();
            NoteModel savedNote = saveNote(note);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    /**
     * Проверка прав пользователя на заметку
     *
     * @return boolean
     */
    public boolean checkNotesOwner(Long noteId, Long userId){
        Optional<NoteModel> note = repository.findNoteModelById(noteId);
        boolean result = false;
        if(note.isPresent()){
            if(note.get().getUserId() == userId){
                result = true;
            }
        }
        return result;
    }

    /**
     * Удаление заметки
     *
     */
    public void deleteNote(Long noteId){
        UserModel user = userService.getCurrentUser();
        if(!checkNotesOwner(noteId, user.getId())) throw new AccessDeniedException("У этого пользователя нет доступа к этой заметке");
        repository.deleteById(noteId);
    }

    /**
     * Удаление заметки
     *
     * @return обновленная заметка
     */
    public NoteModel updateNote(UpdateNoteRequest request) {
        UserModel user = userService.getCurrentUser();
        if(!checkNotesOwner(request.getId(), user.getId())) throw new AccessDeniedException("У этого пользователя нет доступа к этой заметке");
        Optional<NoteModel> note = repository.findNoteModelById(request.getId());
        note.get().setText(request.getText());
        return saveNote(note.get());
    }

    /**
     * Получение списка заметок по пользователю
     *
     * @return список заметок
     */
    public List<NoteModel> getAllUserNotes() {
        UserModel user = userService.getCurrentUser();
        List<NoteModel> noteList = repository.findNoteModelsByUserId(user.getId());
        return noteList;
    }
}
