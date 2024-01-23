package com.mshembelev.mindskeeper.controllers;

import com.mshembelev.mindskeeper.dto.note.CreateNoteRequest;
import com.mshembelev.mindskeeper.dto.note.UpdateNoteRequest;
import com.mshembelev.mindskeeper.exception.ValidationRuntimeException;
import com.mshembelev.mindskeeper.models.NoteModel;
import com.mshembelev.mindskeeper.services.NoteService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
@Tag(name = "Заметки")
public class NoteController {
    private final NoteService noteService;

    @Operation(summary = "Создание заметки")
    @PostMapping("/")
    public ResponseEntity<?> createNote(@RequestBody @Valid CreateNoteRequest request, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new ValidationRuntimeException(bindingResult);
        return noteService.createNote(request);
    }

    @Operation(summary = "Обновление заметки")
    @PostMapping("/update")
    public NoteModel updateNote(@RequestBody UpdateNoteRequest request){
        return noteService.updateNote(request);
    }

    @Operation(summary = "Удаление заметки")
    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable("noteId") Long id) throws AccessDeniedException {
        noteService.deleteNote(id);
    }



    @Operation(summary = "Все заметки пользователя")
    @GetMapping("/")
    public List<NoteModel> getAllUserNotes(){
        return noteService.getAllUserNotes();
    }
}
