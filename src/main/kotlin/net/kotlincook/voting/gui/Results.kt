package net.kotlincook.voting.gui

import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import net.kotlincook.voting.model.Attitude
import net.kotlincook.voting.model.Attitude.*
import net.kotlincook.voting.model.voteModel

@Route("results")
@StyleSheet("frontend://results.css")
class Results() : VerticalLayout() {

    data class TableRow(var optionDescription: String,
                        var cntYes: Int, var cntIrr: Int, var cntNo:Int)

    init {
        val tableData = voteModel.votesPerOption.map {
            TableRow(it.key.descripion, it.value[YES]!!, it.value[IRR]!!, it.value[NO]!!)
        }

        val table = Grid<TableRow>()
        table.setItems(tableData)
        table.addColumn(TableRow::optionDescription).setHeader("Option")
        table.addColumn(TableRow::cntYes).setHeader("Ja-Stimmen")
        table.addColumn(TableRow::cntIrr).setHeader("Irrelevant")
        table.addColumn(TableRow::cntNo).setHeader("Nein-Stimmen")

        add(H1("Abstimmungsergebnis"))
        add(table)
        className = "main"
    }
}