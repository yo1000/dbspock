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
/**
 *
 * @author yo1000
 */
class Dbspock_IntTableSpec extends Specification {
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
            int_table {
                test_int1   | test_int2 | test_int3
                r(100)      | 1000      | 10000
                r(200)      | 2000      | 20000
                r(300)      | 3000      | 30000
            }
        }

        def destination = new DriverManagerDestination(URL, USERNAME, PASSWORD)
        new DbSetup(destination,
                Operations.sequenceOf(
                        Operations.truncate('int_table'),
                        insertOps
                )
        ).launch()

        expect:
        def statement = destination.connection.createStatement()
        ResultSet resultSet = statement.executeQuery(
                'SELECT test_int1, test_int2, test_int3 FROM int_table ORDER BY test_int1')

        assert resultSet.next()
        assert 100 == resultSet.getInt('test_int1')
        assert 1000 == resultSet.getInt('test_int2')
        assert 10000 == resultSet.getInt('test_int3')

        assert resultSet.next()
        assert 200 == resultSet.getInt('test_int1')
        assert 2000 == resultSet.getInt('test_int2')
        assert 20000 == resultSet.getInt('test_int3')

        assert resultSet.next()
        assert 300 == resultSet.getInt('test_int1')
        assert 3000 == resultSet.getInt('test_int2')
        assert 30000 == resultSet.getInt('test_int3')
    }

    def "DbSetup integration test with rotate"() {
        setup:
        def insertOps = DbspockOperations.insertIntoWithRotate {
            int_table {
                test_int1   | 100   | 200   | 300
                test_int2   | 1000  | 2000  | 3000
                test_int3   | 10000 | 20000 | 30000
            }
        }

        def destination = new DriverManagerDestination(URL, USERNAME, PASSWORD)
        new DbSetup(destination,
                Operations.sequenceOf(
                        Operations.truncate('int_table'),
                        insertOps
                )
        ).launch()

        expect:
        def statement = destination.connection.createStatement()
        ResultSet resultSet = statement.executeQuery(
                'SELECT test_int1, test_int2, test_int3 FROM int_table ORDER BY test_int1')

        assert resultSet.next()
        assert 100 == resultSet.getInt('test_int1')
        assert 1000 == resultSet.getInt('test_int2')
        assert 10000 == resultSet.getInt('test_int3')

        assert resultSet.next()
        assert 200 == resultSet.getInt('test_int1')
        assert 2000 == resultSet.getInt('test_int2')
        assert 20000 == resultSet.getInt('test_int3')

        assert resultSet.next()
        assert 300 == resultSet.getInt('test_int1')
        assert 3000 == resultSet.getInt('test_int2')
        assert 30000 == resultSet.getInt('test_int3')
    }

    def "DBUnit integration test"() {
        setup:
        def dataSet = DbspockLoaders.loadDataSet {
            int_table {
                test_int1   | test_int2 | test_int3
                r(100)      | 1000      | 10000
                r(200)      | 2000      | 20000
                r(300)      | 3000      | 30000
            }
        }

        def databaseTester = new JdbcDatabaseTester(Driver.class.getName(), URL, USERNAME, PASSWORD)
        databaseTester.setUpOperation = DatabaseOperation.CLEAN_INSERT

        databaseTester.dataSet = dataSet
        databaseTester.onSetup()

        expect:
        def statement = databaseTester.connection.connection.createStatement()
        ResultSet resultSet = statement.executeQuery(
                'SELECT test_int1, test_int2, test_int3 FROM int_table ORDER BY test_int1')

        assert resultSet.next()
        assert 100 == resultSet.getInt('test_int1')
        assert 1000 == resultSet.getInt('test_int2')
        assert 10000 == resultSet.getInt('test_int3')

        assert resultSet.next()
        assert 200 == resultSet.getInt('test_int1')
        assert 2000 == resultSet.getInt('test_int2')
        assert 20000 == resultSet.getInt('test_int3')

        assert resultSet.next()
        assert 300 == resultSet.getInt('test_int1')
        assert 3000 == resultSet.getInt('test_int2')
        assert 30000 == resultSet.getInt('test_int3')
    }

    def "DBUnit integration test with rotate"() {
        setup:
        def dataSet = DbspockLoaders.loadDataSetWithRotate {
            int_table {
                test_int1   | 100   | 200   | 300
                test_int2   | 1000  | 2000  | 3000
                test_int3   | 10000 | 20000 | 30000
            }
        }

        def databaseTester = new JdbcDatabaseTester(Driver.class.getName(), URL, USERNAME, PASSWORD)
        databaseTester.setUpOperation = DatabaseOperation.CLEAN_INSERT

        databaseTester.dataSet = dataSet
        databaseTester.onSetup()

        expect:
        def statement = databaseTester.connection.connection.createStatement()
        ResultSet resultSet = statement.executeQuery(
                'SELECT test_int1, test_int2, test_int3 FROM int_table ORDER BY test_int1')

        assert resultSet.next()
        assert 100 == resultSet.getInt('test_int1')
        assert 1000 == resultSet.getInt('test_int2')
        assert 10000 == resultSet.getInt('test_int3')

        assert resultSet.next()
        assert 200 == resultSet.getInt('test_int1')
        assert 2000 == resultSet.getInt('test_int2')
        assert 20000 == resultSet.getInt('test_int3')

        assert resultSet.next()
        assert 300 == resultSet.getInt('test_int1')
        assert 3000 == resultSet.getInt('test_int2')
        assert 30000 == resultSet.getInt('test_int3')
    }
}