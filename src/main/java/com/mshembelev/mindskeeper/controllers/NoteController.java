package com.mshembelev.mindskeeper.controllers;

import com.mshembelev.mindskeeper.dto.note.CreateNoteRequest;
import com.mshembelev.mindskeeper.dto.note.DeleteNoteRequest;
import com.mshembelev.mindskeeper.dto.note.UpdateNoteRequest;
import com.mshembelev.mindskeeper.models.NoteModel;
import com.mshembelev.mindskeeper.services.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public NoteModel createNote(@RequestBody @Valid CreateNoteRequest request){
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
