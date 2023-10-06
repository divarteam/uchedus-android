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
import ru.divarteam.uchedus.data.model.AccountRole
import ru.divarteam.uchedus.network.response.CourseResponse
import ru.divarteam.uchedus.util.KotlinHolder
import ru.divarteam.uchedus.util.fromDefaultFormatToRuFormatString
import ru.divarteam.uchedus.util.getColorFromAttr

@EpoxyModelClass
abstract class CourseEpoxyModel : EpoxyModelWithHolder<CourseEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var checkCourseFragment: (CourseResponse) -> Unit

    @EpoxyAttribute
    lateinit var joinCourse: (CourseResponse) -> Unit

    @EpoxyAttribute
    lateinit var courseResponse: CourseResponse

    @EpoxyAttribute
    lateinit var accountRole: AccountRole

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
        holder.courseCoins.setText("${courseResponse.amountOfBonuses}")

        holder.courseFigure.backgroundTintList = ColorStateList.valueOf(
            when {
                courseResponse.isMy == true ->
                    ContextCompat.getColor(holder.courseFigure.context, R.color.blue_600)

                courseResponse.isCreatedByMe == true ->
                    ContextCompat.getColor(holder.courseFigure.context, R.color.yellow_800)

                else ->
                    holder.courseFigure.context.getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)
            })

        holder.joinCourse.setOnClickListener {
            joinCourse(courseResponse)
        }

        holder.checkCourse.setOnClickListener {
            checkCourseFragment(courseResponse)
        }

        holder.joinCourse.visibility =
            if (courseResponse.isMy ?: false)
                View.GONE
            else
                View.VISIBLE
    }

    override fun getDefaultLayout() = R.layout.item_course

    inner class Holder : KotlinHolder() {
        val courseTitle by bind<TextView>(R.id.course_title_text)
        val courseDescription by bind<TextView>(R.id.course_description)
        val courseDatetime by bind<TextView>(R.id.course_datetime)
        val courseAuthor by bind<TextView>(R.id.course_author)
        val courseCoins by bind<TextView>(R.id.coins_count)
        val courseTasks by bind<TextView>(R.id.course_tasks)
        val courseTasksAll by bind<TextView>(R.id.course_tasks_all)
        val checkCourse by bind<Button>(R.id.check_course)
        val studentsCount by bind<TextView>(R.id.students_count)
        val joinCourse by bind<Button>(R.id.join_course)
        val courseFigure by bind<View>(R.id.course_figure)

    }
}