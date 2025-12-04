package com.tomazbr9.pdfily.exception.handler;

import com.tomazbr9.pdfily.dto.exceptionDTO.ErrorResponseDTO;
import com.tomazbr9.pdfily.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponseDTO> handleFileStorageException(FileStorageException exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmptyExceptionException(EmptyFileException exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleFileUploadNotFoundException(FileUploadNotFoundException exception, HttpServletRequest request){
        logger.warn("Arquivo não foi encontrado");
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceDoesNotBelongToTheAuthenticatedUser.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceDoesNotBelongToTheAuthenticatedUser(ResourceDoesNotBelongToTheAuthenticatedUser exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConvertingFileException.class)
    public ResponseEntity<ErrorResponseDTO> handleFileUploadNotFoundException(ConvertingFileException exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidFileNameException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidFileNameException(InvalidFileNameException exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException (UserNotFoundException exception, HttpServletRequest request){
        logger.warn("Usuário não foi encontrado");
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FailedToSaveTemporaryFileException.class)
    public ResponseEntity<ErrorResponseDTO> handleFailedToSaveTemporaryFileException(FailedToSaveTemporaryFileException exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExpiredOrNonExistentFile.class)
    public ResponseEntity<ErrorResponseDTO> handleExpiredOrNonExistentFile(ExpiredOrNonExistentFile exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedFileFormatException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnsupportedFileFormatException(UnsupportedFileFormatException exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(ClearTemporaryFilesException.class)
    public ResponseEntity<ErrorResponseDTO> handleClearTemporaryFilesException(ClearTemporaryFilesException exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PrepareFileToDownloadException.class)
    public ResponseEntity<ErrorResponseDTO> handlePrepareFileToDownloadException(PrepareFileToDownloadException exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DownloadLogNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleDownloadLogNotFoundException(DownloadLogNotFoundException exception, HttpServletRequest request){
        logger.error("Registro de download não encontrado", exception);
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception, HttpServletRequest request){
        logger.error("Arquivo maior que o limite permitido", exception);
        return buildErrorResponse("Arquivo maior que o limite permitido.", request.getRequestURI(), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(ReadsFileInBytsException.class)
    public ResponseEntity<ErrorResponseDTO> handleReadsFileInBytsException (ReadsFileInBytsException exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsernameAlreadyExistsException (UsernameAlreadyExistsException exception, HttpServletRequest request){
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConversionNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleConversionNotFoundException(ConversionNotFoundException exception, HttpServletRequest request){
        logger.warn("Conversão não encontrada.");
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception exeption, HttpServletRequest request) {

        logger.error("Erro não tratado: ", exeption);

        return buildErrorResponse("Erro interno no servidor", request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(String message, String request, HttpStatus status) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                status.value(),
                message,
                request,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }

}