package com.mshembelev.mindskeeper.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Информация о файле")
public class FileInfoResponse {
    @Schema(description = "ID файла", example = "1")
    private Long id;
    @Schema(description = "ID пользователя", example = "1")
    private Long userId;
    @Schema(description = "Название файла", example = "text.txt")
    private String name;
    @Schema(description = "Тип файла", example = "txt")
    private String memeType;
    @Schema(description = "Размер файла в байтах", example = "1000")
    private Long size;
}
