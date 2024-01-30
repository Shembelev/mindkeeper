package com.mshembelev.mindskeeper.repositories;

import com.mshembelev.mindskeeper.models.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileModel, Long> {
    Optional<List<FileModel>> findAllByUserId(Long userId);
}
