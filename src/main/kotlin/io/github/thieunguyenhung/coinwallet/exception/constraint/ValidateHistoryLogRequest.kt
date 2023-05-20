package io.github.thieunguyenhung.coinwallet.exception.constraint

import io.github.thieunguyenhung.coinwallet.model.HistoryLogRequest
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [HistoryLogRequestValidator::class])
annotation class ValidateHistoryLogRequest(
    val message: String = "startDatetime must be before endDatetime",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class HistoryLogRequestValidator : ConstraintValidator<ValidateHistoryLogRequest, HistoryLogRequest> {
    override fun isValid(value: HistoryLogRequest?, context: ConstraintValidatorContext?): Boolean {
        return if (value?.endDatetime == null || value.startDatetime == null)
            true
        else
            value.startDatetime.isBefore(value.endDatetime)
    }
}
