package net.kotlincook.voting.gui

import com.vaadin.external.org.slf4j.LoggerFactory
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import net.kotlincook.voting.model.Attitude.*
import net.kotlincook.voting.model.ModelSingleton

@Route("results")
@StyleSheet("frontend://results.css")
class Results() : VerticalLayout() {

    val logger = LoggerFactory.getLogger(Results::class.java);

    data class TableRow(var optionDescription: String,
                        var cntYes: Int, var cntIrr: Int, var cntNo:Int)

    init {
        logger.info("Voting result:")
        val tableData = ModelSingleton.votesPerOption.map {
            logger.info("{}", it)
            TableRow(it.key.descripion, it.value[YES]!!, it.value[OK]!!, it.value[NO]!!)
        }

        val table = Grid<TableRow>()
        table.height = "350px"
        table.setItems(tableData)
        table.addColumn(TableRow::optionDescription).setHeader("Option")
        table.addColumn(TableRow::cntYes).setHeader("Ja-Stimmen")
        table.addColumn(TableRow::cntIrr).setHeader("Ok-Stimmen")
        table.addColumn(TableRow::cntNo).setHeader("Veto-Stimmen")
        table.columns[0].width = "600px"
        table.columns[1].width = "130px"
        table.columns[2].width = "130px"
        table.columns[3].width = "130px"
        table.width = "1000px"

        add(H1("Anonymous Voting App"))
        add(H3("Abstimmungsergebnis"))
        add(table)
    }
}