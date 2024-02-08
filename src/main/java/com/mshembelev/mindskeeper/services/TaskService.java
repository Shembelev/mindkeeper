package com.mshembelev.mindskeeper.services;

import com.mshembelev.mindskeeper.dto.task.CreateTaskRequest;
import com.mshembelev.mindskeeper.dto.task.UpdateTaskRequest;
import com.mshembelev.mindskeeper.models.TaskModel;
import com.mshembelev.mindskeeper.models.UserModel;
import com.mshembelev.mindskeeper.repositories.TaskRepository;
import com.mshembelev.mindskeeper.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public ResponseEntity<?> createTask(CreateTaskRequest request) {
        UserModel user = userService.getCurrentUser();
        Long currentGroupId = request.getGroupId() != null ? request.getGroupId() : updateGroupId();
        TaskModel task = TaskModel.builder()
                .userId(user.getId())
                .text(request.getText())
                .currentStatus(false)
                .groupId(currentGroupId)
                .build();

        TaskModel savedTask = saveTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    public ResponseEntity<?> updateTask(UpdateTaskRequest request) {
        UserModel user = userService.getCurrentUser();
        if(!checkTaskOwners(request.getId(), user.getId())) throw new AccessDeniedException("У вас нет доступа к этой задаче");
        TaskModel task = taskRepository.findById(request.getId()).get();
        task.setText(request.getText());
        task.setCurrentStatus(request.getCurrentStatus());
        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    public void deleteTask(Long id) {
        UserModel user = userService.getCurrentUser();
        if(!checkTaskOwners(id, user.getId())) throw new AccessDeniedException("У вас нет доступа к этой задаче");
        taskRepository.deleteById(id);
    }

    public ResponseEntity<?> getAllTask() {
        UserModel user = userService.getCurrentUser();
        List<List<TaskModel>> tasksByGroups = new ArrayList<>();
        List<Long> groupIds = taskRepository.findDistinctGroupIdsByUserId(user.getId());
        for(Long groupId : groupIds){
            Optional<List<TaskModel>> taskModels = taskRepository.findAllByGroupIdAndUserId(groupId, user.getId());
            taskModels.ifPresent(tasksByGroups::add);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tasksByGroups);
    }

    public ResponseEntity<?> updateGroupTask(List<UpdateTaskRequest> request) {
        UserModel user = userService.getCurrentUser();
        Long groupId = null;
        for(UpdateTaskRequest taskRequest : request){
            System.out.println();
            Optional<TaskModel> taskOptional = taskRepository.findById(taskRequest.getId());
            if(taskOptional.isPresent()){
                TaskModel task = taskOptional.get();
                if(!task.getUserId().equals(user.getId())) throw new AccessDeniedException("У вас нет доступа к этой задаче");
                if(groupId == null) groupId = task.getGroupId();
                if(!Objects.equals(groupId, task.getGroupId())) throw new RuntimeException("Данные задачи не относятся к одной группе");
                task.setText(taskRequest.getText());
                task.setCurrentStatus(taskRequest.getCurrentStatus());
                taskRepository.save(task);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.findAllByGroupIdAndUserId(groupId, user.getId()));
    }

    public void deleteGroupTask(Long groupId) {
        UserModel user = userService.getCurrentUser();
        taskRepository.deleteAllByGroupIdAndUserId(groupId, user.getId());
    }

    public ResponseEntity<?> getGroupTask(Long groupId) {
        UserModel user = userService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.findAllByGroupIdAndUserId(groupId, user.getId()).get());
    }


    //Service
    public Long updateGroupId(){
        UserModel user = userService.getCurrentUser();
        user.setGroupId(user.getGroupId()+1);
        userRepository.save(user);
        return user.getGroupId();
    }

    public TaskModel saveTask(TaskModel task){
        return taskRepository.save(task);
    }

    public Boolean checkTaskOwners(Long taskId, Long userId){
        Optional<TaskModel> task = taskRepository.findById(taskId);
        boolean result = false;
        if(task.isPresent()){
            if(Objects.equals(task.get().getUserId(), userId)){
                result = true;
            }
        }
        return result;
    }

}
