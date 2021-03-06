package com.yo1000.dbspock

import spock.lang.Specification

/**
 *
 * @author yo1000
 */
class RotateTableSpec extends Specification {
    def "Translate Closure to Table"() {
        setup:
        def data = {
            table1 {
                c1 | 'r1c1' | 'r2c1'
                c2 | 'r1c2' | 'r2c2'
                c3 | 'r1c3' | 'r2c3'
            }

            table2 {
                cStr  | 'str1'                | 'str2'
                cInt  | 100                   | 200
                cDate | '2016-09-26 18:46:01' | '2016-09-26 18:46:02'
            }

            table3 {
                cInt | 100    | 200
                cStr | 'str1' | 'str2'
            }

            table4 {
                cNull | null   | null
                cStr  | 'str1' | 'str2'
            }
        }

        data.delegate = new RotateTables()
        data.call()

        def tables = data.rotate().tables as List<Table>

        tables.each {
            println "<<TABLE>>"
            println "table name: ${it.name}"
            println "<<COLUMNS>>"
            it.columns.each {
                println "column name: ${it.name}"
            }
            println "<<ROWS>>"
            it.rows.each {
                println "<<ROW>>"
                it.value.each {
                    println "row item value: ${it}"
                }
            }
        }

        expect:
        assert tables[0].name == 'table1'
        assert tables[0].columns[0].name == 'c1'
        assert tables[0].columns[1].name == 'c2'
        assert tables[0].columns[2].name == 'c3'
        assert tables[0].rows[0].value[0] == 'r1c1'
        assert tables[0].rows[0].value[1] == 'r1c2'
        assert tables[0].rows[0].value[2] == 'r1c3'
        assert tables[0].rows[1].value[0] == 'r2c1'
        assert tables[0].rows[1].value[1] == 'r2c2'
        assert tables[0].rows[1].value[2] == 'r2c3'

        assert tables[1].name == 'table2'
        assert tables[1].columns[0].name == 'cStr'
        assert tables[1].columns[1].name == 'cInt'
        assert tables[1].columns[2].name == 'cDate'
        assert tables[1].rows[0].value[0] == 'str1'
        assert tables[1].rows[0].value[1] == 100
        assert tables[1].rows[0].value[2] == '2016-09-26 18:46:01'
        assert tables[1].rows[1].value[0] == 'str2'
        assert tables[1].rows[1].value[1] == 200
        assert tables[1].rows[1].value[2] == '2016-09-26 18:46:02'

        assert tables[2].name == 'table3'
        assert tables[2].columns[0].name == 'cInt'
        assert tables[2].columns[1].name == 'cStr'
        assert tables[2].rows[0].value[0] == 100
        assert tables[2].rows[0].value[1] == 'str1'
        assert tables[2].rows[1].value[0] == 200
        assert tables[2].rows[1].value[1] == 'str2'

        assert tables[3].name == 'table4'
        assert tables[3].columns[0].name == 'cNull'
        assert tables[3].columns[1].name == 'cStr'
        assert tables[3].rows[0].value[0].is(null)
        assert tables[3].rows[0].value[1] == 'str1'
        assert tables[3].rows[1].value[0].is(null)
        assert tables[3].rows[1].value[1] == 'str2'
    }
}
