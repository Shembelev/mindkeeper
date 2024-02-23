package com.mshembelev.mindskeeper.services;

import com.mshembelev.mindskeeper.dto.file.FileInfoResponse;
import com.mshembelev.mindskeeper.models.FileModel;
import com.mshembelev.mindskeeper.models.UserModel;
import com.mshembelev.mindskeeper.property.FileStorageProperty;
import com.mshembelev.mindskeeper.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final UserService userService;
    private Path fileStorageLocation;

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    public void FileService(FileStorageProperty fileStorageProperty) throws IOException {
        this.fileStorageLocation = Paths.get(fileStorageProperty.getUploadDirectory()).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    /**
     * Загрузка файла
     *
     * @param multipartFile
     * @return информация о файле
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public FileInfoResponse create(MultipartFile multipartFile, Long userId) throws NoSuchAlgorithmException, IOException {
        FileModel file = new FileModel();
        file.setName(multipartFile.getOriginalFilename());
        file.setMimeType(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());
        file.setUserId( userId);
        file.setHash();
        fileRepository.save(file);
        storeFile(multipartFile, file.getHash());
        FileInfoResponse response = FileInfoResponse.builder()
                .name(file.getName())
                .userId(file.getUserId())
                .size(file.getSize())
                .id(file.getId())
                .memeType(file.getMimeType())
                .build();
        logger.info("Пользователь " + userId + " загрузил файл размером - " + file.getSize());
        return response;
    }

    /**
     * Сохранение файла
     * @param file
     * @param hash
     * @throws IOException
     */
    private void storeFile(MultipartFile file, String hash) throws IOException {
        Path targetLocation = this.fileStorageLocation.resolve(hash);
        Files.copy(file.getInputStream(), targetLocation);
    }

    /**
     * Получение файла по его айди
     * @param fileId
     * @return
     */
    public ResponseEntity<?> getFileById(Long fileId, Long userId) throws AccessDeniedException {
        Optional<FileModel> fileOptional = fileRepository.findById(fileId);
        if(fileOptional.isPresent()){
            FileModel file = fileOptional.get();
            Path filePath = this.fileStorageLocation.resolve(file.getHash());
            if(!Objects.equals(file.getUserId(), userId)) throw new AccessDeniedException("У вас нет доступа к этому файлу");
            try {
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists() && resource.isReadable()) {
                    String encodedFilename = UriUtils.encode(file.getName(), StandardCharsets.UTF_8);
                    String decodedFilename = new String(encodedFilename.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + decodedFilename + "\"")
                            .body(resource);
                } else {
                    throw new RuntimeException("Файл не найден или не читаем.");
                }
            } catch (MalformedURLException | RuntimeException e) {
                e.printStackTrace();
                throw new RuntimeException("Произошла ошибка при получении файла");
            }
        } else {
            throw new RuntimeException("Произошла ошибка при поиске файла. Попробуйте позже");
        }
    }

    /**
     * Получение информации о всех файлах пользователя
     *
     * @return обработанный список (без hash)
     */
    public List<FileInfoResponse> getAllFilesByUser(Long userId) {
        Optional<List<FileModel>> fileModelOptional = fileRepository.findAllByUserId( userId);
        if (fileModelOptional.isEmpty()) throw new RuntimeException("У этого пользователя нет файлов.");
        List<FileModel> fileModels = fileModelOptional.get();
        List<FileInfoResponse> fileInfoList = new ArrayList<>();
        for (FileModel file : fileModels){
            FileInfoResponse fileInfoResponse = FileInfoResponse.builder()
                            .id(file.getId())
                            .userId(file.getUserId())
                            .name(file.getName())
                            .memeType(file.getMimeType())
                            .size(file.getSize())
                            .build();
            fileInfoList.add(fileInfoResponse);
        }
        return fileInfoList;
    }

    /**
     * Проверка есть ли у пользователя права на файл
     * @return есть ли права на файл у пользователя
     */
    public boolean checkFileOwner(Long fileId, Long userId){
        Optional<FileModel> file = fileRepository.findById(fileId);
        boolean result = false;
        if(file.isPresent()){
            if(file.get().getUserId() == userId){
                result = true;
            }
        }
        return result;
    }

    /**
     * Удаление файла по его id
     */
    public void deleteFile(Long fileId, Long userId) throws AccessDeniedException {
        if(!checkFileOwner(fileId,  userId)) throw new AccessDeniedException("У вас нет прав на этот файл.");
        fileRepository.deleteById(fileId);
    }
}
