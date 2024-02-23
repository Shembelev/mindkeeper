package com.mshembelev.mindskeeper.services;

import com.mshembelev.mindskeeper.dto.task.CreateTaskRequest;
import com.mshembelev.mindskeeper.models.TaskModel;
import com.mshembelev.mindskeeper.models.UserModel;
import com.mshembelev.mindskeeper.repositories.TaskRepository;
import com.mshembelev.mindskeeper.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
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

    /**
     * Создание группы задач
     *
     * @return обновленный список задач
     */
    public List<TaskModel> createGroupTask(List<CreateTaskRequest> request) {
        UserModel user = userService.getCurrentUser();
        List<TaskModel> savedTask = new ArrayList<>();
        Long groupId = updateGroupId();
        for (CreateTaskRequest newTask : request) {
            TaskModel task = TaskModel.builder()
                    .userId(user.getId())
                    .text(newTask.getText())
                    .currentStatus(false)
                    .groupId(groupId)
                    .build();
            savedTask.add(saveTask(task));
        }
        return savedTask;
    }

    /**
     * Обновление группы задач
     *
     * @return обновленный список задач
     */
    public List<TaskModel> updateGroupTask(List<TaskModel> request) {
        UserModel user = userService.getCurrentUser();
        Long groupId = request.get(0).getGroupId();
        List<TaskModel> usersOldTask = taskRepository.findAllByGroupIdAndUserId(groupId, user.getId()).get();
        List<TaskModel> newGroupTask = new ArrayList<>();

        for (TaskModel oldTask : usersOldTask) {
            TaskModel requestDeleteTask = findTaskInListById(request, oldTask.getId());
            if (requestDeleteTask == null) {
                taskRepository.deleteById(oldTask.getId());
            }
        }

        for (TaskModel taskRequest : request) {
            TaskModel oldTask = findTaskInListById(usersOldTask, taskRequest.getId());
            if (oldTask != null) {
                oldTask.setCurrentStatus(taskRequest.getCurrentStatus());
                oldTask.setText(taskRequest.getText());
                newGroupTask.add(saveTask(oldTask));
            } else {
                TaskModel task = TaskModel.builder()
                        .userId(user.getId())
                        .text(taskRequest.getText())
                        .currentStatus(taskRequest.getCurrentStatus())
                        .groupId(groupId)
                        .build();
                newGroupTask.add(saveTask(task));
            }
        }
        return newGroupTask;
    }

    /**
     * Получние всех задач пользователя по его токену
     *
     * @return Список состоящий из списков-групп задач
     */
    public List<List<TaskModel>> getAllTask() {
        UserModel user = userService.getCurrentUser();
        List<List<TaskModel>> tasksByGroups = new ArrayList<>();
        List<Long> groupIds = taskRepository.findDistinctGroupIdsByUserId(user.getId());
        for (Long groupId : groupIds) {
            Optional<List<TaskModel>> taskModels = taskRepository.findAllByGroupIdAndUserId(groupId, user.getId());
            taskModels.ifPresent(tasksByGroups::add);
        }
        return tasksByGroups;
    }

    /**
     * Удаление группы задач со всеми задачами
     *
     */
    public void deleteGroupTask(Long groupId) {
        UserModel user = userService.getCurrentUser();
        taskRepository.deleteAllByGroupIdAndUserId(groupId, user.getId());
    }

    /**
     * Получние 1й группы задач
     *
     * @return список задач
     */
    public List<TaskModel> getGroupTask(Long groupId) {
        UserModel user = userService.getCurrentUser();
        return taskRepository.findAllByGroupIdAndUserId(groupId, user.getId()).get();
    }


    //Service
    /**
     * Обновление номера группы у пользователя
     *
     * @return новый номер
     */
    public Long updateGroupId() {
        UserModel user = userService.getCurrentUser();
        user.setGroupId(user.getGroupId() + 1);
        userRepository.save(user);
        return user.getGroupId();
    }

    /**
     * Сохранение списка задач в БД
     *
     * @return сохраненный список задач
     */
    public TaskModel saveTask(TaskModel task) {
        return taskRepository.save(task);
    }

    /**
     * Проверка уровня доступа к задаче
     *
     * @return есть ли доступ
     */
    public Boolean checkTaskOwners(Long taskId, Long userId) {
        Optional<TaskModel> task = taskRepository.findById(taskId);
        boolean result = false;
        if (task.isPresent()) {
            if (Objects.equals(task.get().getUserId(), userId)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Поиск задачи в списке по ее id
     *
     * @return найденная задача или null
     */
    public TaskModel findTaskInListById(List<TaskModel> taskList, Long id) {
        Optional<TaskModel> taskOptional = taskList.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
        return taskOptional.orElse(null);
    }
}
