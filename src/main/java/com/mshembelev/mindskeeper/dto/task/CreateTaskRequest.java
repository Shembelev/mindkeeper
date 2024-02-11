    package com.mshembelev.mindskeeper.dto.task;

    import io.swagger.v3.oas.annotations.media.Schema;
    import jakarta.annotation.Nullable;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Size;
    import lombok.Data;

    @Data
    @Schema(description = "Запрос на создание задачи")
    public class CreateTaskRequest {
        @Schema(description = "Текст", example = "Текст задачи")
        @NotBlank(message = "Текст задачи не может быть пустым")
        @Size(min = 1, max = 32, message = "Длина задачи от 1 до 32 символов")
        private String text;

        @Schema(description = "ID Группы", example = "2")
        @Nullable
        private Long groupId;
    }
