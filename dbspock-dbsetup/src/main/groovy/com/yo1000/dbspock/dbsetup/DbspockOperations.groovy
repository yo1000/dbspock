package com.yo1000.dbspock.dbsetup

import com.ninja_squad.dbsetup.Operations
import com.ninja_squad.dbsetup.operation.Operation
import com.yo1000.dbspock.Table
import com.yo1000.dbspock.Tables
/**
 *
 * @author yo1000
 */
class DbspockOperations {
    public static Operation insertInto(Closure data) {
        data.delegate = new Tables()
        data.call()

        return insertInto(data.tables as List<Table>)
    }

    protected static Operation insertInto(List<Table> tables) {
        def opsList = []

        tables.each {
            def ops = Operations.insertInto(it.name)
            def table = it
            int rowSize = table.rows.rows.size()

            (0..<rowSize).each {
                def row = ops.row()
                int rowIndex = it
                int colSize = table.columns.columns.size()

                (0..<colSize).each {
                    int colIndex = it

                    row.column(
                            table.columns.columns[colIndex].name,
                            table.rows.rows[rowIndex].items[colIndex].value
                    )
                }

                row.end()
            }

            opsList << ops.build()
        }

        return Operations.sequenceOf(opsList)
    }
}
