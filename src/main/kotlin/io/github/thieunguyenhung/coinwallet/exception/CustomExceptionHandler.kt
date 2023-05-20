package io.github.thieunguyenhung.coinwallet.exception

import io.github.thieunguyenhung.coinwallet.model.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.function.Consumer

@RestControllerAdvice
class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ErrorResponse {
        val errors: MutableList<String> = ArrayList()
        ex.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val key = if (error is FieldError) error.field else error.objectName
            errors.add("$key ${error.defaultMessage}")
        })
        return ErrorResponse(code = HttpStatus.BAD_REQUEST.value(), error = errors)
    }
}
