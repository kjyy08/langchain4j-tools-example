package dev.luigi.example.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDTO {
    private final int status;
    private final String error;
    private final String message;
    private final String path;
}
