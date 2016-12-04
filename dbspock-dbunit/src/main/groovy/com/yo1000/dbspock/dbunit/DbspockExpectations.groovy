package com.yo1000.dbspock.dbunit

import com.yo1000.dbspock.QueryBuilders
import com.yo1000.dbspock.Table
import com.yo1000.dbspock.Tables
import org.dbunit.database.IDatabaseConnection

/**
 *
 * @author yo1000
 */
class DbspockExpectations {
    static containsAll(IDatabaseConnection connection, Closure data) {
        assertToCompare(connection, Tables.asList(data), { result, table, message ->
            assert result.getValue(0, 'cnt') >= table.rows.size() : message
        })
    }

    static containsAny(IDatabaseConnection connection, Closure data) {
        assertToCompare(connection, Tables.asList(data), { result, table, message ->
            assert result.getValue(0, 'cnt') >= 1 : message
        })
    }

    static containsNone(IDatabaseConnection connection, Closure data) {
        assertToCompare(connection, Tables.asList(data), { result, table, message ->
            assert result.getValue(0, 'cnt') == 0 : message
        })
    }

    static matches(IDatabaseConnection connection, Closure data) {
        assertToCompare(connection, Tables.asList(data), { result, table, message ->
            assert result.getValue(0, 'cnt') == table.rows.size() : message
        })
    }

    protected static assertToCompare(IDatabaseConnection connection, List<Table> tables, Closure closure) {
        tables.each { table ->
            def query = QueryBuilders.expectationCount(table)
            def result = connection.createQueryTable(table.name, query)
            closure.call(result, table, "Query details> ${query}")
        }
    }
}
