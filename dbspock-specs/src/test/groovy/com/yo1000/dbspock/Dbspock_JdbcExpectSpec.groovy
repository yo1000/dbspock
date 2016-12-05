package com.yo1000.dbspock

import com.ninja_squad.dbsetup.DbSetup
import com.ninja_squad.dbsetup.Operations
import com.ninja_squad.dbsetup.destination.Destination
import com.ninja_squad.dbsetup.destination.DriverManagerDestination
import com.yo1000.dbspock.dbsetup.DbspockOperations
import com.yo1000.dbspock.jdbc.DbspockExpectations
import org.flywaydb.core.Flyway
import spock.lang.Shared
import spock.lang.Specification
/**
 *
 * @author yo1000
 */
class Dbspock_JdbcExpectSpec extends Specification {
    def static final URL = 'jdbc:h2:file:./build/testdb'
    def static final USERNAME = 'sa'
    def static final PASSWORD = ''

    @Shared
    Destination destination = new DriverManagerDestination(URL, USERNAME, PASSWORD)

    def setup() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(URL, USERNAME, PASSWORD);
        flyway.migrate();

        def insertOps = DbspockOperations.insertInto {
            test_table {
                test_int | test_str | test_date
                100      | 'test1'  | '2016-09-26 23:20:01.0'
                200      | 'test2'  | '2016-09-26 23:20:02.0'
                300      | 'test3'  | '2016-09-26 23:20:03.0'
                400      | "test'"  | '2016-09-26 23:20:04.0'
            }
        }

        new DbSetup(destination,
                Operations.sequenceOf(
                        Operations.truncate('test_table'),
                        insertOps
                )
        ).launch()
    }

    def "Jdbc expect matches test"() {
        expect:
        def data = {
            test_table {
                test_int | test_str | test_date
                100      | 'test1'  | '2016-09-26 23:20:01.0'
                200      | 'test2'  | '2016-09-26 23:20:02.0'
                300      | 'test3'  | '2016-09-26 23:20:03.0'
                400      | "test'"  | '2016-09-26 23:20:04.0'
            }

            demo_table {
                test_dec1 | test_dec2
                100.1     | 1000.01
                200.2     | 2000.02
                200.3     | 2000.03
            }
        }

        DbspockExpectations.matches(destination.connection, data)
    }

    def "Jdbc expect containsAll test"() {
        expect:
        def data = {
            test_table {
                test_int | test_str | test_date
                100      | 'test1'  | '2016-09-26 23:20:01.0'
                200      | 'test2'  | '2016-09-26 23:20:02.0'
                400      | "test'"  | '2016-09-26 23:20:04.0'
            }

            demo_table {
                test_dec1 | test_dec2
                100.1     | 1000.01
                200.2     | 2000.02
            }
        }

        DbspockExpectations.containsAll(destination.connection, data)
    }

    def "Jdbc expect containsAny test"() {
        expect:
        def data = {
            test_table {
                test_int | test_str | test_date
                100      | 'test1'  | '2016-09-26 23:20:01.0'
                200      | 'test2'  | '2016-09-26 23:20:02.0'
                9000     | 'test9'  | '2016-09-26 23:20:09.0'
                400      | "test'"  | '2016-09-26 23:20:04.0'
            }

            demo_table {
                test_dec1 | test_dec2
                100.1     | 1000.01
                200.2     | 2000.02
                9000.9    | 2000.02
            }
        }

        DbspockExpectations.containsAny(destination.connection, data)
    }


    def "Jdbc expect containsNone test"() {
        expect:
        def data = {
            test_table {
                test_int | test_str | test_date
                9000     | 'test9'  | '2016-09-26 23:20:09.0'
            }

            demo_table {
                test_dec1 | test_dec2
                9000.9    | 2000.02
            }
        }

        DbspockExpectations.containsNone(destination.connection, data)
    }
}