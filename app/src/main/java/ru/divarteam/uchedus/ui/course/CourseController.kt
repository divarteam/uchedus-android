package ru.divarteam.uchedus.ui.course

import com.airbnb.epoxy.EpoxyController
import ru.divarteam.uchedus.epoxy.courseHeader
import ru.divarteam.uchedus.epoxy.task
import ru.divarteam.uchedus.network.response.CourseResponse
import ru.divarteam.uchedus.network.response.TaskResponse

class CourseController(
    private val joinCourse: (CourseResponse) -> Unit,
    private val checkAnswer: (taskId: Int, answer: String) -> Unit,
) : EpoxyController() {

    lateinit var courseResponse: CourseResponse

    fun setCourseData(courseResponse: CourseResponse) {
        this.courseResponse = courseResponse
        println("HEY NIGGA")
        this.requestModelBuild()

    }

    override fun buildModels() {
        println("BUILD MODELS WOW")
        courseHeader {
            id(-1)
            courseResponse(this@CourseController.courseResponse)
            joinCourse(this@CourseController.joinCourse)
        }

        if (courseResponse.isMy == true)
            courseResponse.tasks.orEmpty().toList().forEachIndexed { index, taskResponse ->
                task {
                    id(index)
                    number(index + 1)
                    taskResponse(taskResponse)
                    checkAnswer(this@CourseController.checkAnswer)
                }
            }
    }
}