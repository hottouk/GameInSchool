package com.hottouk.gameinschool.view.student

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentRadarChartBinding

class RadarChartFragment : Fragment() {

    val viewModel: StudentViewModel by activityViewModels()

    private var mBinding: FragmentRadarChartBinding? = null
    val binding get() = mBinding!!

    private lateinit var chart: RadarChart
    private val chartData = ArrayList<RadarEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentRadarChartBinding.inflate(inflater, container, false)
        chart = binding.radarChart
        binding.closeBtn.setOnClickListener {
            viewModel.radarModeOff()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchSelectedStudent().observe(viewLifecycleOwner) { student ->
            viewModel.fetchStudentPetList(student.userId).observe(viewLifecycleOwner) { pets ->
                chartData.clear()
                student.getCharacterAbility(pets)
                chartData.add(RadarEntry(student.academicAbility.toFloat()))
                chartData.add(RadarEntry(student.career.toFloat()))
                chartData.add(RadarEntry(student.leadership.toFloat()))
                chartData.add(RadarEntry(student.sincerity.toFloat()))
                chartData.add(RadarEntry(student.cooperation.toFloat()))
                makeChart()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        chartData.clear()
        mBinding = null
    }

    private fun makeChart() {
        val dataSet = RadarDataSet(chartData, "DATA")
        dataSet.color = R.color.teal_700
        val data = RadarData()
        data.addDataSet(dataSet)
        val labels = arrayOf("학업력", "진로", "리더십", "성실성", "협동성")
        val xAxis: XAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        chart.data = data
    }
}