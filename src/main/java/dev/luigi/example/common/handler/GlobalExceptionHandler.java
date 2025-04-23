package dev.luigi.example.common.handler;

import dev.luigi.example.common.dto.ErrorResponseDTO;
import dev.luigi.example.common.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(CustomException e, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .status(e.getHttpStatus().value())
                .error(e.getHttpStatus().getReasonPhrase())
                .message(e.getMessage())
                .path(request.getDescription(false).substring(4))
                .build();

        return ResponseEntity.status(errorResponseDTO.getStatus()).body(errorResponseDTO);
    }
}
