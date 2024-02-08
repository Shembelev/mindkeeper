package com.mshembelev.mindskeeper.controllers;

import com.mshembelev.mindskeeper.dto.task.CreateTaskRequest;
import com.mshembelev.mindskeeper.dto.task.UpdateTaskRequest;
import com.mshembelev.mindskeeper.exception.ValidationRuntimeException;
import com.mshembelev.mindskeeper.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> getAllTask(){
        return taskService.getAllTask();
    }

    @Operation(summary = "Получение всех задач в группе")
    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getGroupTask(@PathVariable("groupId") Long groupId){
        return taskService.getGroupTask(groupId);
    }

    @Operation(summary = "Создание задачи")
    @PostMapping("/")
    public ResponseEntity<?> createTask(@RequestBody @Valid CreateTaskRequest request, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new ValidationRuntimeException(bindingResult);
        return taskService.createTask(request);
    }

    @Operation(summary = "Обновление задачи")
    @PostMapping("/update")
    public ResponseEntity<?> updateTask(@RequestBody UpdateTaskRequest request){
        return taskService.updateTask(request);
    }

    @Operation(summary = "Удаление задачи")
    @DeleteMapping("/{taskId}")
    public void  deleteTask(@PathVariable("taskId") Long id){
         taskService.deleteTask(id);
    }

    @Operation(summary = "Обновление всех задач в группе")
    @PostMapping("/group/update")
    public ResponseEntity<?> updateGroupTask(@RequestBody @Valid List<UpdateTaskRequest> request){
        return taskService.updateGroupTask(request);
    }


    @Operation(summary = "Удаление всей группы задач")
    @DeleteMapping("/group/{groupId}")
    public void deleteGroupTask(@PathVariable("groupId") Long groupId){
        taskService.deleteGroupTask(groupId);
    }
}
