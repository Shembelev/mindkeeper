package com.mshembelev.mindskeeper.repositories;

import com.mshembelev.mindskeeper.models.NoteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<NoteModel, Long> {
    Optional<NoteModel> findNoteModelById(Long id);
    List<NoteModel> findNoteModelsByUserId(Long id);
}
