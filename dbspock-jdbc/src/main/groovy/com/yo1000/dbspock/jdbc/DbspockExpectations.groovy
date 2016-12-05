package com.yo1000.dbspock.jdbc

import com.yo1000.dbspock.QueryBuilders
import com.yo1000.dbspock.Table
import com.yo1000.dbspock.Tables

import java.sql.Connection
/**
 *
 * @author yo1000
 */
class DbspockExpectations {
    static containsAll(Connection connection, Closure data) {
        assertToCompare(connection, Tables.asList(data), { result, table, message ->
            assert result.next()
            assert result.getInt('cnt') >= table.rows.size() : message
        })
    }

    static containsAny(Connection connection, Closure data) {
        assertToCompare(connection, Tables.asList(data), { result, table, message ->
            assert result.next()
            assert result.getInt('cnt') >= 1 : message
        })
    }

    static containsNone(Connection connection, Closure data) {
        assertToCompare(connection, Tables.asList(data), { result, table, message ->
            assert result.next()
            assert result.getInt('cnt') == 0 : message
        })
    }

    static matches(Connection connection, Closure data) {
        assertToCompare(connection, Tables.asList(data), { result, table, message ->
            assert result.next()
            assert result.getInt('cnt') == table.rows.size() : message
        })
    }

    protected static assertToCompare(Connection connection, List<Table> tables, Closure closure) {
        tables.each { table ->
            def query = QueryBuilders.expectationCount(table)
            def result = connection.createStatement().executeQuery(query)
            closure.call(result, table, "Query details> ${query}")
        }
    }
}
