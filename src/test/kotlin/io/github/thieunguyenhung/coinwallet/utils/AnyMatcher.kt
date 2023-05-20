package io.github.thieunguyenhung.coinwallet.utils

import org.mockito.ArgumentMatchers

@Suppress("UNUSED_PARAMETER")
class AnyMatcher {
    companion object {
        fun <T> any(type: Class<T>): T = ArgumentMatchers.any()
    }
}
