package ru.divarteam.uchedus.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreateCourseRequest(
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
)