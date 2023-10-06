package ru.divarteam.uchedus.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serial

@Keep
data class AnswerTaskRequest(
    @SerializedName("task_int_id") val taskIntId: Int,
    @SerializedName("answer") val answer: String
)