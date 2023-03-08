package uz.isdaha.handle;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import uz.isdaha.bots.LogsRequestBot;
import uz.isdaha.exception.*;
import uz.isdaha.payload.response.ErrorMessageResponse;


import java.io.IOException;
import java.util.Date;


@RequiredArgsConstructor
@RestControllerAdvice
public class ApplicationExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    private final LogsRequestBot logsRequestBot;

    @ExceptionHandler(value = {IllegalArgumentException.class, ClassCastException.class, IllegalStateException.class,
        InvalidDataAccessApiUsageException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageResponse handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn(ex.getMessage());
        logsRequestBot.sendMessage(ex + "\n\n" + request.getContextPath());
        return ErrorMessageResponse.builder()
            .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
            .timestamp(new Date())
            .message(ex.getMessage())
            .build();
    }


    @ExceptionHandler(value = MotiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageResponse> handleMotiException(MotiException ex, WebRequest request) {
        logsRequestBot.sendMessage(ex + "\n\n" + request.getContextPath());
        return ResponseEntity.badRequest().body(ErrorMessageResponse.builder()
            .statusCode(ex.errorCode().code)
            .message(ex.errorCode().name())
            .timestamp(new Date())
            .build());
    }


    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageResponse> handleOther(Exception ex, WebRequest request) throws IOException {
        logsRequestBot.sendLog(ex);
        if (ex instanceof MotiException) {
            logsRequestBot.sendMessage(ex + "\n\n" + request.getContextPath());
            return ResponseEntity.badRequest().body(ErrorMessageResponse.builder()
                .statusCode(((MotiException) ex).errorCode().code)
                .message(((MotiException) ex).errorCode().name())
                .timestamp(new Date())
                .build());
        } else {
            return ResponseEntity.badRequest().body(ErrorMessageResponse.builder()
                .statusCode(500)
                .message("Internal server error").build());
        }
    }

}



