package com.mshembelev.mindskeeper.repositories;

import com.mshembelev.mindskeeper.models.TaskModel;
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
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    private TaskModel taskModel;

    @BeforeEach
    public void init(){
        taskModel = TaskModel.builder().id(0L).userId(0L).currentStatus(false).groupId(0L).text("task text").build();
    }

    @Test
    public void TaskRepository_SaveAll_ReturnSavedTask(){
        TaskModel savedTask = taskRepository.save(taskModel);

        Assertions.assertThat(savedTask).isNotNull();
        Assertions.assertThat(savedTask.getId()).isGreaterThan(0);
    }

    @Test
    public void TaskRepository_FindTaskById_ReturnTask(){
        TaskModel savedTask = taskRepository.save(taskModel);
        Optional<TaskModel> foundTask = taskRepository.findById(savedTask.getId());

        Assertions.assertThat(foundTask.isPresent()).isTrue();
        Assertions.assertThat(foundTask.get()).isNotNull();
        Assertions.assertThat(foundTask.get().getId()).isGreaterThan(0);
    }

    @Test
    public void TaskRepository_DeleteById_ReturnNull(){
        TaskModel savedTask = taskRepository.save(taskModel);
        taskRepository.deleteById(savedTask.getId());
        Optional<TaskModel> allUserTasks = taskRepository.findById(savedTask.getId());

        Assertions.assertThat(allUserTasks.isPresent()).isFalse();
    }

    @Test
    public void TaskRepository_FindAllByGroupIdAndUserId_ReturnListOfGroups(){
        TaskModel savedTaskFromFirstGroup = taskRepository.save(taskModel);
        taskRepository.save(TaskModel.builder().id(1L).userId(0L).currentStatus(false).groupId(1L).text("task text").build());
        taskRepository.save(TaskModel.builder().id(2L).userId(0L).currentStatus(false).groupId(1L).text("task text").build());

        Optional<List<TaskModel>> listOfTasksByFirstGroup = taskRepository.findAllByGroupIdAndUserId(savedTaskFromFirstGroup.getGroupId(), savedTaskFromFirstGroup.getUserId());

        Assertions.assertThat(listOfTasksByFirstGroup.isPresent()).isTrue();
        Assertions.assertThat(listOfTasksByFirstGroup.get().size()).isLessThan(2);
    }

    @Test
    public void TaskRepository_DeleteAllByGroupIdAndUserId_ReturnNull(){
        TaskModel savedTaskFromFirstGroup = taskRepository.save(taskModel);
        TaskModel savedTaskFromSecondGroup = taskRepository.save(TaskModel.builder().id(1L).userId(0L).currentStatus(false).groupId(1L).text("task text").build());
        taskRepository.save(TaskModel.builder().id(2L).userId(0L).currentStatus(false).groupId(1L).text("task text").build());

        taskRepository.deleteAllByGroupIdAndUserId(savedTaskFromSecondGroup.getGroupId(), savedTaskFromSecondGroup.getUserId());
        Optional<TaskModel> deletedTaskByGroupId = taskRepository.findById(2L);

        Assertions.assertThat(deletedTaskByGroupId.isPresent()).isFalse();
    }

    @Test
    public void TaskRepository_FineDistinctGroupIdsByUserId_ReturnListOfGroupsIds(){
        TaskModel savedTaskFromFirstGroup = taskRepository.save(taskModel);
        taskRepository.save(TaskModel.builder().id(1L).userId(0L).currentStatus(false).groupId(1L).text("task text").build());
        taskRepository.save(TaskModel.builder().id(2L).userId(0L).currentStatus(false).groupId(2L).text("task text").build());

        List<Long> listOfIds = taskRepository.findDistinctGroupIdsByUserId(savedTaskFromFirstGroup.getUserId());

        Assertions.assertThat(listOfIds).isNotNull();
        Assertions.assertThat(listOfIds.size()).isEqualTo(2);
    }
}
