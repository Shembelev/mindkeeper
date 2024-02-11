package com.mshembelev.mindskeeper.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "")
public class UpdateTaskRequest {
    @Schema(description = "Текст", example = "Текст задачи")
    @NotBlank(message = "Текст задачи не может быть пустым")
    @Size(min = 1, max = 32, message = "Длина задачи от 1 до 32 символов")
    private String text;

    @Schema(description = "ID задачи", example = "1")
    @NotBlank(message = "ID задачи должно быть указано")
    private Long id;

    @Schema(description = "Статус задачи", example = "true")
    @NotBlank(message = "Статус задачи должен быть указан")
    private Boolean currentStatus;

    @Schema(description = "Номер группы", example = "1")
    @NotBlank(message = "Номер группы должен быть указан")
    private Long groupId;
}
