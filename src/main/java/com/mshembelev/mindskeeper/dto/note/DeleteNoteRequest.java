package com.mshembelev.mindskeeper.dto.note;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание заметки")
public class DeleteNoteRequest {
    @Schema(description = "ID Заметки", example = "0")
    @NotBlank(message = "ID Заметки не может быть пустым")
    private Long id;
}
