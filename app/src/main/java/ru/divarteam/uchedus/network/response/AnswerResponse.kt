package ru.divarteam.uchedus.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AnswerResponse(
    @SerializedName("user_int_id") val userIntId: Int?,
    @SerializedName("task_int_id") val taskIntId: Int?,
    @SerializedName("user_answer") val userAnswer: String?,
    @SerializedName("done") val done: Boolean?,
    @SerializedName("done_at") val doneAt: String?,
    @SerializedName("is_correct") val isCorrect: Boolean?,
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("created") val created: String?
)