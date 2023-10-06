package ru.divarteam.uchedus.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserResponse(
    @SerializedName("roles") val roles: List<String>?,
    @SerializedName("mail") val mail: String?,
    @SerializedName("tokens") val tokens: List<String>?,
    @SerializedName("tg_id") val telegramId: String?,
    @SerializedName("course_int_ids") val courseIntIds: List<Int>?,
    @SerializedName("division") val division: String?,
    @SerializedName("fio") val fio: String?,
    @SerializedName("coins") val coins: Int?,
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("created") val created: String?,
    @SerializedName("current_token") val token: String?,
    @SerializedName("my_products") val myProducts: List<ProductResponse>?,
    @SerializedName("courses_created_by_me") val coursesCreatedByMe: List<CreatedByMeCourseResponse>?,
    @SerializedName("my_courses") val myCourses: List<CourseResponse>?,
    @SerializedName("left_tasks_amount") val leftTasksAmount: Int?,
    @SerializedName("correct_tasks_amount") val correctTasksAmount: Int?,
    @SerializedName("tasks_amount") val tasksAmount: Int?,
    @SerializedName("left_courses_amount") val leftCoursesAmount: Int?,
    @SerializedName("done_courses_amount") val doneCoursesAmount: Int?,
    @SerializedName("courses_amount") val coursesAmount: Int?,
)