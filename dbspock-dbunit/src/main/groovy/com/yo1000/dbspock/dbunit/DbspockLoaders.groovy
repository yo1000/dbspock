package com.yo1000.dbspock.dbunit

import com.yo1000.dbspock.Table
import com.yo1000.dbspock.Tables
import org.dbunit.dataset.IDataSet
import org.dbunit.dataset.xml.FlatXmlDataSet

/**
 *
 * @author yo1000
 */
class DbspockLoaders {
    public static IDataSet loadDataSet(Closure data) {
        data.delegate = new Tables()
        data.call()

        return loadDataSet(data.tables as List<Table>)
    }

    protected static IDataSet loadDataSet(List<Table> tables) {
        def dataSetBuilder = new StringBuilder("<dataset>")

        tables.each {
            def table = it
            def tableName = it.name
            int rowSize = table.rows.size()

            (0..<rowSize).each {
                def rowBuilder = new StringBuilder(tableName)
                int rowIndex = it
                int colSize = table.columns.size()

                (0..<colSize).each {
                    int colIndex = it
                    def col = table.columns[colIndex].name
                    def row = table.rows[rowIndex].value[colIndex]

                    if (row == null) return
                    rowBuilder.append(/ ${col}="${row}"/)
                }

                dataSetBuilder.append("<${rowBuilder.toString()}/>")
            }
        }
        dataSetBuilder.append("</dataset>")

        return new FlatXmlDataSet(new StringReader(dataSetBuilder.toString()))
    }
}
