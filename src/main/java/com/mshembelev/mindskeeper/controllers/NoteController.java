package com.mshembelev.mindskeeper.controllers;

import com.mshembelev.mindskeeper.dto.note.CreateNoteRequest;
import com.mshembelev.mindskeeper.dto.note.UpdateNoteRequest;
import com.mshembelev.mindskeeper.exception.ValidationRuntimeException;
import com.mshembelev.mindskeeper.models.NoteModel;
import com.mshembelev.mindskeeper.services.NoteService;
import com.mshembelev.mindskeeper.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
@Tag(name = "Заметки")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    @Operation(summary = "Создание заметки")
    @PostMapping("/")
    public ResponseEntity<NoteModel> createNote(@RequestBody @Valid CreateNoteRequest request, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new ValidationRuntimeException(bindingResult);
        Long userId = userService.getCurrentUser().getId();
        NoteModel savedNote = noteService.createNote(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    @Operation(summary = "Обновление заметки")
    @PostMapping("/update")
    public ResponseEntity<NoteModel> updateNote(@RequestBody UpdateNoteRequest request){
        Long userId = userService.getCurrentUser().getId();
        NoteModel savedNote = noteService.updateNote(request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(savedNote);
    }

    @Operation(summary = "Удаление заметки")
    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable("noteId") Long id){
        Long userId = userService.getCurrentUser().getId();
        noteService.deleteNote(id, userId);
    }



    @Operation(summary = "Все заметки пользователя")
    @GetMapping("/")
    public List<NoteModel> getAllUserNotes(){
        Long userId = userService.getCurrentUser().getId();
        return noteService.getAllUserNotes(userId);
    }
}
