package ru.divarteam.uchedus.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateMeRequest(
    @SerializedName("fio") val fio: String?,
    @SerializedName("division") val division: String?
)