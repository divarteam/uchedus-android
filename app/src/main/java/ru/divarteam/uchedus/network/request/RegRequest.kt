package ru.divarteam.uchedus.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RegRequest (
    @SerializedName("mail") val mail: String?,
    @SerializedName("code") val code: Int?,
    @SerializedName("role") val role: String?
)