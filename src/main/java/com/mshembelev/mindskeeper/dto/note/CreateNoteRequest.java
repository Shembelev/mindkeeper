package com.mshembelev.mindskeeper.dto.note;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание заметки")
public class CreateNoteRequest {

    @Schema(description = "Заголовок заметки", example = "Тема 1")
    @Size(min = 1, max = 32, message = "Длина заголовка от 1 до 32 символов")
    @NotBlank(message = "Текст заметки не может быть пустым")
    private String title;


    @Schema(description = "Текст", example = "Текст заметки")
    @NotBlank(message = "Текст заметки не может быть пустым")
    private String text;

    @Schema(description = "Изображение закодировано Base64", example = "data:image/jpg;base64,....")
    private String codeImage;
}
