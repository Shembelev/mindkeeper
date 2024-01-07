package com.mshembelev.mindskeeper.dto.note;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление заметки")
public class UpdateNoteRequest {

    @Schema(description = "ID Заметки", example = "0")
    @NotBlank(message = "ID Заметки не может быть пустым")
    private Long id;

    @Schema(description = "Текст заметки", example = "Текст")
    @NotBlank(message = "Текст заметки не может быть пустым")
    private String text;
}
