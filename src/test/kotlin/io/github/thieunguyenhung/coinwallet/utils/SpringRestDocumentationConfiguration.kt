package io.github.thieunguyenhung.coinwallet.utils

import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation

const val HOST_PLACEHOLDER = "HOST"
const val PORT_PLACEHOLDER = 12321
const val HTTPS = "https"

class SpringRestDocumentationConfiguration {
    companion object {
        fun prepareDocumentationSpecification(restDocumentation: RestDocumentationContextProvider): RequestSpecification =
            RequestSpecBuilder().addFilter(
                RestAssuredRestDocumentation
                    .documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withResponseDefaults(
                        Preprocessors.modifyUris().host(HOST_PLACEHOLDER).port(PORT_PLACEHOLDER).scheme(HTTPS)
                    )
                    .withRequestDefaults(
                        Preprocessors.modifyUris().host(HOST_PLACEHOLDER).port(PORT_PLACEHOLDER).scheme(HTTPS)
                    )
            ).build()
    }
}
