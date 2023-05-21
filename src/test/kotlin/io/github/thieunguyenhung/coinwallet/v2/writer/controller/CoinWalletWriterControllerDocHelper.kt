package io.github.thieunguyenhung.coinwallet.v2.writer.controller

import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.resourceDetails
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.COIN_WALLET_DOCUMENTATION_TAG_V2
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.POST_DEPOSIT_PATH
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.REQUEST_MAPPING_API_PATH_V2
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured3.RestDocumentationFilter

object CoinWalletWriterControllerDocHelper {
    val POST_DEPOSIT: RestDocumentationFilter = document(
        identifier = "my-wallet$REQUEST_MAPPING_API_PATH_V2$POST_DEPOSIT_PATH",
        resourceDetails = resourceDetails().description("API V2 to deposit coin to wallet, has option to disable through application properties if required.")
            .tag(COIN_WALLET_DOCUMENTATION_TAG_V2),
        snippets = arrayOf(
            requestFields(
                fieldWithPath("datetime").description("The timestamp of the deposit action performed, must be in the past or present and in format yyyy-MM-dd'T'HH:mm:ssXXX"),
                fieldWithPath("amount").description("The deposit amount must be equal to or greater than zero")
            ),
            responseFields(
                fieldWithPath("id").description("UUID of the record"),
                fieldWithPath("amount").description("The deposit amount"),
                fieldWithPath("datetime").description("The timestamp of the deposit action performed"),
            )
        )
    )
}
