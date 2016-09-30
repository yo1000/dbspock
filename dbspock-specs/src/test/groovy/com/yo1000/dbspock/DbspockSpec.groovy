package com.yo1000.dbspock

import com.ninja_squad.dbsetup.DbSetup
import com.ninja_squad.dbsetup.Operations
import com.ninja_squad.dbsetup.destination.DriverManagerDestination
import com.yo1000.dbspock.dbsetup.DbspockOperations
import com.yo1000.dbspock.dbunit.DbspockLoaders
import org.dbunit.JdbcDatabaseTester
import org.dbunit.operation.DatabaseOperation
import org.flywaydb.core.Flyway
import org.h2.Driver
import spock.lang.Specification

import java.sql.ResultSet
import java.text.SimpleDateFormat
/**
 *
 * @author yo1000
 */
class DbspockSpec extends Specification {
    def static final URL = 'jdbc:h2:file:./build/testdb'
    def static final USERNAME = 'sa'
    def static final PASSWORD = ''

    def setup() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(URL, USERNAME, PASSWORD);
        flyway.migrate();
    }

    def "DbSetup integration test"() {
        setup:
        def insertOps = DbspockOperations.insertInto {
            test_table {
                test_int   | test_str   | test_date
                100        | 'test1'    | '2016-09-26 23:20:01.0'
                200        | 'test2'    | '2016-09-26 23:20:02.0'
                300        | 'test3'    | '2016-09-26 23:20:03.0'
            }
        }

        def destination = new DriverManagerDestination(URL, USERNAME, PASSWORD)
        new DbSetup(destination,
                Operations.sequenceOf(
                        Operations.truncate('test_table'),
                        insertOps
                )
        ).launch()

        expect:
        def statement = destination.connection.createStatement()
        ResultSet resultSet = statement.executeQuery(
                'SELECT test_int, test_str, test_date FROM test_table ORDER BY test_int')

        assert resultSet.next()
        assert 100 == resultSet.getInt('test_int')
        assert 'test1' == resultSet.getString('test_str')
        assert '2016-09-26 23:20:01.0' == new SimpleDateFormat('yyyy-MM-dd HH:mm:ss.S')
                .format(resultSet.getTimestamp('test_date'))

        assert resultSet.next()
        assert 200 == resultSet.getInt('test_int')
        assert 'test2' == resultSet.getString('test_str')
        assert '2016-09-26 23:20:02.0' == new SimpleDateFormat('yyyy-MM-dd HH:mm:ss.S')
                .format(resultSet.getTimestamp('test_date'))

        assert resultSet.next()
        assert 300 == resultSet.getInt('test_int')
        assert 'test3' == resultSet.getString('test_str')
        assert '2016-09-26 23:20:03.0' == new SimpleDateFormat('yyyy-MM-dd HH:mm:ss.S')
                .format(resultSet.getTimestamp('test_date'))
    }

    def "DBUnit integration test"() {
        setup:
        def dataSet = DbspockLoaders.loadDataSet {
            test_table {
                test_int   | test_str   | test_date
                100        | 'test1'    | '2016-09-26 23:20:01.0'
                200        | 'test2'    | '2016-09-26 23:20:02.0'
                300        | 'test3'    | '2016-09-26 23:20:03.0'
            }
        }

        def databaseTester = new JdbcDatabaseTester(Driver.class.getName(), URL, USERNAME, PASSWORD)
        databaseTester.setUpOperation = DatabaseOperation.CLEAN_INSERT

        databaseTester.dataSet = dataSet
        databaseTester.onSetup()

        expect:
        def statement = databaseTester.connection.connection.createStatement()
        ResultSet resultSet = statement.executeQuery(
                'SELECT test_int, test_str, test_date FROM test_table ORDER BY test_int')

        assert resultSet.next()
        assert 100 == resultSet.getInt('test_int')
        assert 'test1' == resultSet.getString('test_str')
        assert '2016-09-26 23:20:01.0' == new SimpleDateFormat('yyyy-MM-dd HH:mm:ss.S')
                .format(resultSet.getTimestamp('test_date'))

        assert resultSet.next()
        assert 200 == resultSet.getInt('test_int')
        assert 'test2' == resultSet.getString('test_str')
        assert '2016-09-26 23:20:02.0' == new SimpleDateFormat('yyyy-MM-dd HH:mm:ss.S')
                .format(resultSet.getTimestamp('test_date'))

        assert resultSet.next()
        assert 300 == resultSet.getInt('test_int')
        assert 'test3' == resultSet.getString('test_str')
        assert '2016-09-26 23:20:03.0' == new SimpleDateFormat('yyyy-MM-dd HH:mm:ss.S')
                .format(resultSet.getTimestamp('test_date'))
    }
}