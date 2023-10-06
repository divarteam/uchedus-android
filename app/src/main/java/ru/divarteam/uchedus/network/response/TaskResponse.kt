package ru.divarteam.uchedus.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TaskResponse(
    @SerializedName("answer") val answer: String?,
    @SerializedName("question") val question: String?,
    @SerializedName("bonus") val bonus: Int?,
    @SerializedName("photo_filename") val photoFileName: String?,
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("created") val created: String?,
    @SerializedName("is_correct") val isCorrect: Boolean?,
)