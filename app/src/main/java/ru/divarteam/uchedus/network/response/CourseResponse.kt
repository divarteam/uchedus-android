package ru.divarteam.uchedus.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CourseResponse(
    @SerializedName("author_int_id") val authorIntId: Int?,
    @SerializedName("description") val description: String?,
    @SerializedName("task_int_ids") val taskIntIds: List<Int>?,
    @SerializedName("title") val title: String?,
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("created") val created: String?,
    @SerializedName("is_my") val isMy: Boolean?,
    @SerializedName("is_created_by_me") val isCreatedByMe: Boolean?,
    @SerializedName("amount_of_tasks") val amountOfTasks: Int?,
    @SerializedName("amount_of_bonuses") val amountOfBonuses: Int?,
    @SerializedName("amount_of_users") val amountOfUsers: Int?,
    @SerializedName("amount_of_completed_tasks") val amountOfCompletedTasks: Int?,
    @SerializedName("author") val author: UserResponse?,
    @SerializedName("tasks") val tasks: List<TaskResponse>?,
)