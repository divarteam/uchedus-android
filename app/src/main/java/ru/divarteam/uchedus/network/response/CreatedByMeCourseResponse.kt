package ru.divarteam.uchedus.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreatedByMeCourseResponse(
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("author_int_id") val authorIntId: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("task_int_ids") val taskIntIds: List<Int>?,
    @SerializedName("created") val created: String?,
    @SerializedName("users_amount") val usersAmount: Int?,
    @SerializedName("users_done_all_amount") val usersDoneAllAmount: Int?,
    @SerializedName("users_undone_all_amount") val usersUndoneAllAmount: Int?,
    @SerializedName("all_coins") val allCoins: Int?,
    @SerializedName("tasks_amount") val tasksAmount: Int?,
    @SerializedName("correct_tasks") val correctTasks: Int?,
    @SerializedName("incorrect_tasks") val incorrectTasks: Int?,
)