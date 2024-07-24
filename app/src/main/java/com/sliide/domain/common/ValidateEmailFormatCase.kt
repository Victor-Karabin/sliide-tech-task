package com.sliide.domain.common

internal interface ValidateEmailFormatCase {

    operator fun invoke(email: String): Result<Unit>
}
