package com.android.infinum.models.responses

import com.android.infinum.models.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse (
    @SerialName("user") val user: User
)