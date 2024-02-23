package com.mshembelev.mindskeeper.controllers;

import com.mshembelev.mindskeeper.dto.task.CreateTaskRequest;
import com.mshembelev.mindskeeper.exception.ValidationRuntimeException;
import com.mshembelev.mindskeeper.models.TaskModel;
import com.mshembelev.mindskeeper.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Tag(name = "Задачи")
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Получение всех задач пользователя")
    @GetMapping("/")
    public ResponseEntity<List<List<TaskModel>>> getAllTask(){
        List<List<TaskModel>> listTask = taskService.getAllTask();
        return ResponseEntity.status(HttpStatus.OK).body(listTask);
    }

    @Operation(summary = "Получение всех задач в группе")
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<TaskModel>> getGroupTask(@PathVariable("groupId") Long groupId){
        List<TaskModel> list = taskService.getGroupTask(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @Operation(summary = "Создание задачи")
    @PostMapping("/")
    public ResponseEntity<List<TaskModel>> createTask(@RequestBody @Valid List<CreateTaskRequest> request, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new ValidationRuntimeException(bindingResult);
        List<TaskModel> savedTask = taskService.createGroupTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @Operation(summary = "Обновление всех задач в группе")
    @PostMapping("/group/update")
    public ResponseEntity<List<TaskModel>> updateGroupTask(@RequestBody List<TaskModel> request){
        List<TaskModel> savedTask =  taskService.updateGroupTask(request);
        return ResponseEntity.status(HttpStatus.OK).body(savedTask);
    }


    @Operation(summary = "Удаление всех группы задач")
    @DeleteMapping("/group/{groupId}")
    public void deleteGroupTask(@PathVariable("groupId") Long groupId){
        taskService.deleteGroupTask(groupId);
    }
}
