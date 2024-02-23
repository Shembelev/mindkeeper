package com.mshembelev.mindskeeper.controllers;

import com.mshembelev.mindskeeper.dto.SuccessResponse;
import com.mshembelev.mindskeeper.dto.file.FileInfoResponse;
import com.mshembelev.mindskeeper.services.FileService;
import com.mshembelev.mindskeeper.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Tag(name = "Файлы")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @Operation(summary = "Загрузка файла на сервер")
    @PostMapping(value = "/", consumes = "multipart/form-data")
    public ResponseEntity<FileInfoResponse> uploadFile(@RequestParam(value = "file") MultipartFile file) throws NoSuchAlgorithmException, IOException {
        Long userId = userService.getCurrentUser().getId();
        FileInfoResponse response = fileService.create(file, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Загрузка файла с сервера")
    @GetMapping("/download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long fileId) throws AccessDeniedException {
        Long userId = userService.getCurrentUser().getId();
        return fileService.getFileById(fileId, userId);

    }

    @Operation(summary = "Получение списка файлов пользователя")
    @GetMapping("/")
    public ResponseEntity<List<FileInfoResponse>> getAllFilesByUser(){
        Long userId = userService.getCurrentUser().getId();
        List<FileInfoResponse> responseList = fileService.getAllFilesByUser(userId);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @Operation(summary = "Удаление файла")
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFileById(@PathVariable Long fileId) throws AccessDeniedException {
        Long userId = userService.getCurrentUser().getId();
        fileService.deleteFile(fileId, userId);
        return new ResponseEntity<>(SuccessResponse.builder().status(HttpStatus.OK.value()).message("Файл успешно удален").build(), HttpStatus.OK);
    }
}
