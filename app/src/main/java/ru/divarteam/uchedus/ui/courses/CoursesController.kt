package ru.divarteam.uchedus.ui.courses

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.epoxy.stickyheader.StickyHeaderCallbacks
import ru.divarteam.uchedus.data.model.AccountRole
import ru.divarteam.uchedus.epoxy.CourseSearchEpoxyModel
import ru.divarteam.uchedus.epoxy.course
import ru.divarteam.uchedus.epoxy.courseSearch
import ru.divarteam.uchedus.network.response.CourseResponse

class CoursesController(
    private val checkCourseFragment: (CourseResponse) -> Unit,
    private val joinCourse: (CourseResponse) -> Unit,
    private val handleQuery: (String) -> Unit
) : TypedEpoxyController<List<CourseResponse>>(), StickyHeaderCallbacks {

    override fun buildModels(data: List<CourseResponse>?) {
        courseSearch {
            id(-1)
            handleQuery(this@CoursesController.handleQuery)
        }
        data?.forEachIndexed { index, courseResponse ->
            course {
                id(index)
                courseResponse(courseResponse)
                joinCourse(this@CoursesController.joinCourse)
                checkCourseFragment(this@CoursesController.checkCourseFragment)
            }
        }
    }

    override fun setupStickyHeaderView(stickyHeader: View) {
        super<TypedEpoxyController>.setupStickyHeaderView(stickyHeader)
        stickyHeader.elevation = 15000f
    }

    override fun teardownStickyHeaderView(stickyHeader: View) {
        super<TypedEpoxyController>.teardownStickyHeaderView(stickyHeader)
        stickyHeader.elevation = 0f
    }

    override fun isStickyHeader(position: Int) =
        position == 0
}