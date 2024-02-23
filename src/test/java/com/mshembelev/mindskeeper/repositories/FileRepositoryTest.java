package com.mshembelev.mindskeeper.repositories;

import com.mshembelev.mindskeeper.models.FileModel;
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
public class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    private FileModel fileModel;

    @BeforeEach
    public void init(){
        fileModel = FileModel.builder().id(0L).hash("codedhash").mimeType("codedType").name("File Name").size(255).userId(0L).build();
    }

    @Test
    public void FileRepository_SaveAll_ReturnSavedFile(){
        FileModel savedFile = fileRepository.save(fileModel);

        Assertions.assertThat(savedFile).isNotNull();
        Assertions.assertThat(savedFile.getId()).isGreaterThan(0);
    }

    @Test
    public void FileRepository_FindFileById_ReturnFile(){
        FileModel savedFile = fileRepository.save(fileModel);
        Optional<FileModel> foundFile = fileRepository.findById(savedFile.getId());

        Assertions.assertThat(foundFile.isPresent()).isTrue();
        Assertions.assertThat(foundFile.get()).isNotNull();
        Assertions.assertThat(foundFile.get().getId()).isGreaterThan(0);
    }

    @Test
    public void FileRepository_DeleteById_ReturnNull(){
        FileModel savedFile = fileRepository.save(fileModel);
        fileRepository.deleteById(savedFile.getId());
        Optional<FileModel> allFiles = fileRepository.findById(savedFile.getId());

        Assertions.assertThat(allFiles.isPresent()).isFalse();
    }

    @Test
    public void FileRepository_FindAllFilesByUser_ReturnFilesList(){
        FileModel savedFile = fileRepository.save(fileModel);
        Optional<List<FileModel>> allUserFiles = fileRepository.findAllByUserId(savedFile.getUserId());

        Assertions.assertThat(allUserFiles.isPresent()).isTrue();
        Assertions.assertThat(allUserFiles.get().size()).isGreaterThan(0);
    }
}
