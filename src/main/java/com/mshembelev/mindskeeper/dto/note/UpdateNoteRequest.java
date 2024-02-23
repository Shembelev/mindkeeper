package com.mshembelev.mindskeeper.dto.note;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление заметки")
public class UpdateNoteRequest {

    @Schema(description = "ID Заметки", example = "0")
    @NotBlank(message = "ID Заметки не может быть пустым")
    private Long id;

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
