package io.github.thieunguyenhung.coinwallet.v2.reader.controller

import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.COIN_WALLET_DOCUMENTATION_TAG_V2
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.POST_HISTORY_PATH
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.REQUEST_MAPPING_API_PATH_V2
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.restassured3.RestDocumentationFilter

object CoinWalletReaderControllerDocHelper {
    val POST_HISTORY: RestDocumentationFilter = RestAssuredRestDocumentationWrapper.document(
        identifier = "my-wallet$REQUEST_MAPPING_API_PATH_V2$POST_HISTORY_PATH",
        resourceDetails = RestAssuredRestDocumentationWrapper.resourceDetails()
            .description("API V2 to log history of wallet deposit actions within a specified timestamp range, has option to disable through application properties if required.")
            .tag(COIN_WALLET_DOCUMENTATION_TAG_V2),
        snippets = arrayOf(
            PayloadDocumentation.requestFields(
                fieldWithPath("startDatetime")
                    .description("The start timestamp of history log, in format yyyy-MM-dd'T'HH:mm:ssXXX"),
                fieldWithPath("endDatetime")
                    .description("The end timestamp of history log, in format yyyy-MM-dd'T'HH:mm:ssXXX")
            ),
            PayloadDocumentation.responseFields(
                fieldWithPath("content[].id").description("UUID of the record"),
                fieldWithPath("content[].amount").description("The deposit amount"),
                fieldWithPath("content[].datetime").description("The timestamp of the deposit action performed"),
                PayloadDocumentation.subsectionWithPath("pageable").description("pagination information"),
                fieldWithPath("totalPages").description("number of all pages"),
                fieldWithPath("totalElements").description("number of all elements"),
                fieldWithPath("size").description("size of one page"),
                fieldWithPath("number").description("number of the page"),
                PayloadDocumentation.subsectionWithPath("sort").description("sorting attributes"),
                fieldWithPath("first").description("indicates if the page is first page"),
                fieldWithPath("last").description("indicates if the page is last page"),
                fieldWithPath("numberOfElements").description("number of elements in the page"),
                fieldWithPath("empty").description("indicates if list of elements is empty ")
            )
        )
    )
}