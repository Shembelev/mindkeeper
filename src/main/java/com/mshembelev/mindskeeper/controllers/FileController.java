package com.mshembelev.mindskeeper.controllers;

import com.mshembelev.mindskeeper.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Tag(name = "Файлы")
public class FileController {
    private final FileService fileService;


    @Operation(summary = "Загрузка файла на сервер")
    @PostMapping(value = "/", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file") MultipartFile file) throws NoSuchAlgorithmException, IOException {
        return fileService.create(file);
    }

    @Operation(summary = "Загрузка файла с сервера")
    @GetMapping("/download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long fileId) {
        return fileService.getFileById(fileId);

    }

    @Operation(summary = "Получение списка файлов пользователя")
    @GetMapping("/")
    public ResponseEntity<?> getAllFilesByUser(){
        return fileService.getAllFilesByUser();
    }

    @Operation(summary = "Удаление файла")
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFileById(@PathVariable Long fileId) throws AccessDeniedException {
        return fileService.deleteFile(fileId);
    }
}
