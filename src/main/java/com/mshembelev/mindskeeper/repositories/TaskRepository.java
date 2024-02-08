package com.mshembelev.mindskeeper.repositories;

import com.mshembelev.mindskeeper.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    Optional<List<TaskModel>> findAllByGroupIdAndUserId(Long groupId, Long userId);
    void deleteAllByGroupIdAndUserId(Long groupId, Long userId);

    @Query("SELECT DISTINCT t.groupId FROM TaskModel t WHERE t.userId = :userId")
    List<Long> findDistinctGroupIdsByUserId(Long userId);
}
