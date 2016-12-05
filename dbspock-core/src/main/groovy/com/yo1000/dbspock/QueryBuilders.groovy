package com.yo1000.dbspock

/**
 *
 * @author yo1000
 */
class QueryBuilders {
    static String expectationCount(Table table) {
        def query = "select count(*) as cnt from ${table.name}"

        table.rows.eachWithIndex { row, i ->
            if (i == 0) query += " where"
            if (i > 0) query += " or"

            query += " ("
            row.eachWithIndex { item, j ->
                def column = table.columns[j].name
                def value = row[j].value
                def quote = "${value instanceof Number ? "" : "'"}"
                query += "${j > 0 ? ' and' : ''} ${column}=${quote}${value}${quote}"
            }
            query += " )"
        }

        return query
    }
}
