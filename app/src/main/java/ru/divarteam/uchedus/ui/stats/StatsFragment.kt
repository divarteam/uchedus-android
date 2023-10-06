package ru.divarteam.uchedus.ui.stats

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.faskn.lib.Arc
import com.faskn.lib.Slice
import com.faskn.lib.buildChart
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.uchedus.R
import ru.divarteam.uchedus.data.model.AccountRole
import ru.divarteam.uchedus.databinding.FragmentStatsBinding

@AndroidEntryPoint
class StatsFragment : Fragment() {

    lateinit var binding: FragmentStatsBinding
    val statsViewModel: StatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        statsViewModel.loadProfile(-1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentStatsBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        statsViewModel.currentProfile.observe(viewLifecycleOwner) {
            val leftTasksAmount = (it.leftTasksAmount ?: 0).toFloat()
            val correctTasksAmount = (it.correctTasksAmount ?: 0).toFloat()
            val tasksAmount = (it.tasksAmount ?: 1).toFloat()

            val leftCoursesAmount = (it.leftCoursesAmount ?: 0).toFloat()
            val doneCoursesAmount = (it.doneCoursesAmount ?: 0).toFloat()
            val coursesAmount = (it.coursesAmount ?: 1).toFloat()

            if (tasksAmount > 0) {
                when (statsViewModel.accountRole) {
                    AccountRole.STUDENT -> {
                        binding.tasksPieChart.apply {
                            setPieChart(buildChart {
                                slices {
                                    arrayListOf(
                                        Slice(
                                            dataPoint = correctTasksAmount,
                                            color = R.color.light_green_600,
                                            name = "Выполненных заданий"
                                        ),
                                        Slice(
                                            dataPoint = (tasksAmount - leftTasksAmount - correctTasksAmount),
                                            color = R.color.red_600,
                                            name = "Неправильно решённых заданий"
                                        ),
                                        Slice(
                                            dataPoint = leftTasksAmount,
                                            color = R.color.yellow_800,
                                            name = "Нерешённых заданий"
                                        ),
                                    )
                                }
                                sliceStartPoint { 30f }

                            })

                            showLegend(binding.tasksLegendLayout, StatsLegendAdapter())
                        }

                        binding.coursesPieChart.apply {
                            setPieChart(buildChart {
                                slices {
                                    arrayListOf(
                                        Slice(
                                            dataPoint = doneCoursesAmount,
                                            color = R.color.light_green_600,
                                            name = "Пройденных курсов"
                                        ),
                                        Slice(
                                            dataPoint = (coursesAmount - leftCoursesAmount - doneCoursesAmount),
                                            color = R.color.red_600,
                                            name = "Проваленных курсов"
                                        ),
                                        Slice(
                                            dataPoint = leftCoursesAmount,
                                            color = R.color.yellow_800,
                                            name = "Курсов в процессе прохождения"
                                        ),
                                    )
                                }
                                sliceStartPoint { 30f }

                            })

                            showLegend(binding.coursesLegendLayout, StatsLegendAdapter())
                        }
                    }

                    AccountRole.TEACHER -> TODO()
                    AccountRole.ADMIN -> TODO()
                }
                binding.noCourses.visibility = View.GONE
            } else
                binding.noCourses.visibility = View.VISIBLE

        }
    }

}