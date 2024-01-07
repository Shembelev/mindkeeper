package com.mshembelev.mindskeeper.dto.note;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание заметки")
public class CreateNoteRequest {
    @Schema(description = "Текст", example = "Текст заметки")
    @NotBlank(message = "Текст заметки не может быть пустым")
    private String text;
}
