package net.kotlincook.voting.gui

import com.vaadin.flow.component.charts.Chart
import com.vaadin.flow.component.charts.model.*
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import net.kotlincook.voting.gui.Voting.Companion.RADIO_IRR
import net.kotlincook.voting.gui.Voting.Companion.RADIO_NO
import net.kotlincook.voting.gui.Voting.Companion.RADIO_YES
import net.kotlincook.voting.model.Attitude
import net.kotlincook.voting.model.voteModel

@Route("chart")
@StyleSheet("frontend://chart.css")
class Chart : VerticalLayout() {

    init {
        val chart = Chart()
        val configuration = chart.getConfiguration()
        configuration.setTitle("Crefo-Vote: Abstimmungsergebnis")
        configuration.setSubTitle("Powered by <a href=\"http://kotlincook.de/\">KotlinCook</a>")
        chart.getConfiguration().getChart().setType(ChartType.BAR)

        voteModel.votesPerOption.forEach {
            configuration.addSeries(ListSeries(it.key.descripion,
                    it.value[Attitude.YES], it.value[Attitude.IRR], it.value[Attitude.NO]))
        }

        val x = XAxis()
        x.setCategories(RADIO_YES, RADIO_IRR, RADIO_NO)
        configuration.addxAxis(x)

        val y = YAxis()
        y.setMin(0)
        val yTitle = AxisTitle()
        yTitle.setText("Anzahl der Stimmen")
        yTitle.setAlign(VerticalAlign.HIGH)
        y.setTitle(yTitle)
        configuration.addyAxis(y)

//        val tooltip = Tooltip()
//        tooltip.setValueSuffix(" millions")
//        configuration.setTooltip(tooltip)

        val plotOptions = PlotOptionsBar()
        val dataLabels = DataLabels()
        dataLabels.setEnabled(true)
        plotOptions.setDataLabels(dataLabels)
        configuration.setPlotOptions(plotOptions)
        add(chart)
        className = "main"
    }
}
