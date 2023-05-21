package io.github.thieunguyenhung.coinwallet.controller

import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.resourceDetails
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.COIN_WALLET_DOCUMENTATION_TAG_V1
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.POST_DEPOSIT_PATH
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.POST_HISTORY_PATH
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.REQUEST_MAPPING_API_PATH_V1
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.restassured3.RestDocumentationFilter

object CoinWalletControllerDocHelper {
    val POST_DEPOSIT: RestDocumentationFilter = document(
        identifier = "my-wallet$REQUEST_MAPPING_API_PATH_V1$POST_DEPOSIT_PATH",
        resourceDetails = resourceDetails().description("API to deposit coin to wallet")
            .tag(COIN_WALLET_DOCUMENTATION_TAG_V1),
        snippets = arrayOf(
            requestFields(
                fieldWithPath("datetime").description("The timestamp of the deposit action performed, must be in the past or present and in format yyyy-MM-dd'T'HH:mm:ssXXX"),
                fieldWithPath("amount").description("The deposit amount must be equal to or greater than zero")
            ),
            responseFields(
                id(),
                amount(),
                datetime()
            )
        )
    )

    val POST_HISTORY: RestDocumentationFilter = document(
        identifier = "my-wallet$REQUEST_MAPPING_API_PATH_V1$POST_HISTORY_PATH",
        resourceDetails = resourceDetails()
            .description("API to log history of wallet deposit actions within a specified timestamp range")
            .tag(COIN_WALLET_DOCUMENTATION_TAG_V1),
        snippets = arrayOf(
            requestFields(
                fieldWithPath("startDatetime").description("The start timestamp of history log, in format yyyy-MM-dd'T'HH:mm:ssXXX"),
                fieldWithPath("endDatetime").description("The end timestamp of history log, in format yyyy-MM-dd'T'HH:mm:ssXXX")
            ),
            responseFields(
                id("content[]."),
                amount("content[]."),
                datetime("content[]."),
                subsectionWithPath("pageable").description("pagination information"),
                fieldWithPath("totalPages").description("number of all pages"),
                fieldWithPath("totalElements").description("number of all elements"),
                fieldWithPath("size").description("size of one page"),
                fieldWithPath("number").description("number of the page"),
                subsectionWithPath("sort").description("sorting attributes"),
                fieldWithPath("first").description("indicates if the page is first page"),
                fieldWithPath("last").description("indicates if the page is last page"),
                fieldWithPath("numberOfElements").description("number of elements in the page"),
                fieldWithPath("empty").description("indicates if list of elements is empty ")
            )
        )
    )

    private fun id(prefix: String = ""): FieldDescriptor =
        fieldWithPath("${prefix}id").description("UUID of the record")

    private fun amount(prefix: String = ""): FieldDescriptor =
        fieldWithPath("${prefix}amount").description("The deposit amount")

    private fun datetime(prefix: String = ""): FieldDescriptor =
        fieldWithPath("${prefix}datetime").description("The timestamp of the deposit action performed")
}
