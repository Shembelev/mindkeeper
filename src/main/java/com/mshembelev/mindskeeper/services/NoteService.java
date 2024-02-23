package com.mshembelev.mindskeeper.services;

import com.mshembelev.mindskeeper.dto.note.CreateNoteRequest;
import com.mshembelev.mindskeeper.dto.note.UpdateNoteRequest;
import com.mshembelev.mindskeeper.models.NoteModel;
import com.mshembelev.mindskeeper.models.UserModel;
import com.mshembelev.mindskeeper.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    public NoteModel createNote(CreateNoteRequest request, Long userId) {
        NoteModel note = NoteModel.builder()
                .title(request.getTitle())
                .text(request.getText())
                .userId(userId)
                .codeImage(null)
                .build();
        if(request.getCodeImage() != null){
            note.setCodeImage(request.getCodeImage());
        }
        return saveNote(note);
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
            if(Objects.equals(note.get().getUserId(), userId)){
                result = true;
            }
        }
        return result;
    }

    /**
     * Удаление заметки
     *
     */
    public void deleteNote(Long noteId, Long userId){
        if(!checkNotesOwner(noteId, userId)) throw new AccessDeniedException("У вас нет доступа к этой заметке");
        repository.deleteById(noteId);
    }

    /**
     * Обновление заметки
     *
     * @return обновленная заметка
     */
    public NoteModel updateNote(UpdateNoteRequest request, Long userId) {
        if(!checkNotesOwner(request.getId(), userId)) throw new AccessDeniedException("У вас нет доступа к этой заметке");
        Optional<NoteModel> noteModelOptional = repository.findNoteModelById(request.getId());
        NoteModel note = noteModelOptional.get();
        note.setText(request.getText());
        note.setCodeImage(request.getCodeImage());
        note.setTitle(request.getTitle());
        return saveNote(note);
    }

    /**
     * Получение списка заметок по пользователю
     *
     * @return список заметок
     */
    public List<NoteModel> getAllUserNotes(Long userId) {
        return repository.findNoteModelsByUserId(userId);
    }
}
