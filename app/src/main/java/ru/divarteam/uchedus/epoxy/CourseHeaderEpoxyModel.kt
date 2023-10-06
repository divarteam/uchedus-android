package ru.divarteam.uchedus.epoxy

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.divarteam.uchedus.R
import ru.divarteam.uchedus.network.response.CourseResponse
import ru.divarteam.uchedus.util.KotlinHolder
import ru.divarteam.uchedus.util.fromDefaultFormatToRuFormatString

@EpoxyModelClass
abstract class CourseHeaderEpoxyModel : EpoxyModelWithHolder<CourseHeaderEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var courseResponse: CourseResponse

    @EpoxyAttribute
    lateinit var joinCourse: (CourseResponse) -> Unit

    @SuppressLint("SetTextI18n")
    override fun bind(holder: Holder) {
        holder.courseAuthor.setText(
            "Автор: ${
                (courseResponse.author?.fio ?: "").split(" ").let {
                    if (it.size < 3)
                        it.subList(0, it.size)
                    else
                        "${it[0]} ${it[1].get(0)}. ${it[2].get(0)}."
                }
            }"
        )

        holder.courseDatetime.setText(
            "Создан: ${courseResponse.created?.fromDefaultFormatToRuFormatString()}"
        )

        holder.courseTitle.setText(courseResponse.title)
        holder.courseDescription.setText(courseResponse.description)
        holder.courseTasks.setText("${courseResponse.amountOfCompletedTasks}")
        holder.courseTasksAll.setText("/${courseResponse.amountOfTasks}")
        holder.studentsCount.setText("${courseResponse.amountOfUsers}")
        holder.coinsCount.setText("${courseResponse.amountOfBonuses}")

        holder.joinCourse.setOnClickListener {
            joinCourse(courseResponse)
        }

        holder.joinCourse.visibility =
            if (courseResponse.isMy ?: false)
                View.GONE
            else
                View.VISIBLE
    }

    override fun getDefaultLayout() = R.layout.item_course_header

    inner class Holder : KotlinHolder() {
        val courseTitle by bind<TextView>(R.id.course_title)
        val courseDescription by bind<TextView>(R.id.course_description)
        val courseDatetime by bind<TextView>(R.id.course_datetime)
        val courseAuthor by bind<TextView>(R.id.course_author)
        val courseTasks by bind<TextView>(R.id.course_tasks)
        val courseTasksAll by bind<TextView>(R.id.course_tasks_all)
        val studentsCount by bind<TextView>(R.id.students_count)
        val coinsCount by bind<TextView>(R.id.coins_count)
        val joinCourse by bind<Button>(R.id.join_course)
    }
}