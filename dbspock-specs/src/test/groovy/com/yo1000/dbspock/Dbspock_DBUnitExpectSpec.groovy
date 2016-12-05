package com.yo1000.dbspock

import com.yo1000.dbspock.dbunit.DbspockExpectations
import com.yo1000.dbspock.dbunit.DbspockLoaders
import org.dbunit.IDatabaseTester
import org.dbunit.JdbcDatabaseTester
import org.dbunit.operation.DatabaseOperation
import org.flywaydb.core.Flyway
import org.h2.Driver
import spock.lang.Shared
import spock.lang.Specification
/**
 *
 * @author yo1000
 */
class Dbspock_DBUnitExpectSpec extends Specification {
    def static final URL = 'jdbc:h2:file:./build/testdb'
    def static final USERNAME = 'sa'
    def static final PASSWORD = ''

    @Shared
    IDatabaseTester databaseTester

    def setup() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(URL, USERNAME, PASSWORD);
        flyway.migrate();

        def tables = Tables.asList {
            test_table {
                test_int   | test_str   | test_date
                100        | 'test1'    | '2016-09-26 23:20:01.0'
                200        | 'test2'    | '2016-09-26 23:20:02.0'
                300        | 'test3'    | '2016-09-26 23:20:03.0'
            }

            demo_table {
                test_dec1  | test_dec2
                100.1      | 1000.01
                200.2      | 2000.02
                200.3      | 2000.03
            }
        }

        def dataSet = DbspockLoaders.loadDataSet(tables)
        databaseTester = new JdbcDatabaseTester(Driver.class.getName(), URL, USERNAME, PASSWORD)
        databaseTester.setUpOperation = DatabaseOperation.CLEAN_INSERT

        databaseTester.dataSet = dataSet
        databaseTester.onSetup()
    }

    def "DBUnit expect matches test"() {
        expect:
        def data = {
            test_table {
                test_int   | test_str   | test_date
                100        | 'test1'    | '2016-09-26 23:20:01.0'
                200        | 'test2'    | '2016-09-26 23:20:02.0'
                300        | 'test3'    | '2016-09-26 23:20:03.0'
            }

            demo_table {
                test_dec1  | test_dec2
                100.1      | 1000.01
                200.2      | 2000.02
                200.3      | 2000.03
            }
        }

        DbspockExpectations.matches(databaseTester.connection, data)
    }

    def "DBUnit expect containsAll test"() {
        expect:
        def data = {
            test_table {
                test_int | test_str | test_date
                100      | 'test1'  | '2016-09-26 23:20:01.0'
                200      | 'test2'  | '2016-09-26 23:20:02.0'
            }

            demo_table {
                test_dec1 | test_dec2
                100.1     | 1000.01
                200.2     | 2000.02
            }
        }

        DbspockExpectations.containsAll(databaseTester.connection, data)
    }

    def "DBUnit expect containsAny test"() {
        expect:
        def data = {
            test_table {
                test_int | test_str | test_date
                100      | 'test1'  | '2016-09-26 23:20:01.0'
                200      | 'test2'  | '2016-09-26 23:20:02.0'
                9000     | 'test2'  | '2016-09-26 23:20:02.0'
            }

            demo_table {
                test_dec1 | test_dec2
                100.1     | 1000.01
                200.2     | 2000.02
                9000.9    | 2000.02
            }
        }

        DbspockExpectations.containsAny(databaseTester.connection, data)
    }


    def "DBUnit expect containsNone test"() {
        expect:
        def data = {
            test_table {
                test_int | test_str | test_date
                9000     | 'test2'  | '2016-09-26 23:20:02.0'
            }

            demo_table {
                test_dec1 | test_dec2
                9000.9    | 2000.02
            }
        }

        DbspockExpectations.containsNone(databaseTester.connection, data)
    }
}