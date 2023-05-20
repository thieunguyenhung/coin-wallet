package io.github.thieunguyenhung.coinwallet.model

data class ErrorResponse(
    val code: Int,
    val error: List<String>? = listOf()
)