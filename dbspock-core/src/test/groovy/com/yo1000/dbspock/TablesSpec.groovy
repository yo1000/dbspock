package com.yo1000.dbspock

import spock.lang.Specification

/**
 *
 * @author yo1000
 */
class TablesSpec extends Specification {
    def "Translate Closure to Table"() {
        setup:
        def data = {
            table1 {
                c1     | c2     | c3
                'r1c1' | 'r1c2' | 'r1c3'
                'r2c1' | 'r2c2' | 'r2c3'
            }

            table2 {
                cStr   | cInt   | cDate
                'str1' | 100    | '2016-09-26 18:46:01'
                'str2' | 200    | '2016-09-26 18:46:02'
            }

            table3 {
                cInt | cStr
                100  | 'str1'
                200  | 'str2'
            }

            table4 {
                cNull | cStr
                null  | 'str1'
                null  | 'str2'
            }

            tableInt {
                i1  | i2
                100 | 1000
                200 | 2000
            }

            tableDec {
                d1    | d2
                100.1 | 1000.01
                200.2 | 2000.02
            }

            tableIntDec {
                i1  | d2
                100 | 1000.01
                200 | 2000.02
            }

            tableBool {
                b1    | b2
                true  | true
                false | false
                true  | false
            }

            tableIntBool {
                i1   | b2
                100  | true
                200  | false
                300  | false
            }
        }

        data.delegate = new Tables()
        data.call()

        def tables = data.tables as List<Table>

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

        assert tables[4].name == 'tableInt'
        assert tables[4].columns[0].name == 'i1'
        assert tables[4].columns[1].name == 'i2'
        assert tables[4].rows[0].value[0] == 100
        assert tables[4].rows[0].value[1] == 1000
        assert tables[4].rows[1].value[0] == 200
        assert tables[4].rows[1].value[1] == 2000

        assert tables[5].name == 'tableDec'
        assert tables[5].columns[0].name == 'd1'
        assert tables[5].columns[1].name == 'd2'
        assert tables[5].rows[0].value[0] == 100.1
        assert tables[5].rows[0].value[1] == 1000.01
        assert tables[5].rows[1].value[0] == 200.2
        assert tables[5].rows[1].value[1] == 2000.02

        assert tables[6].name == 'tableIntDec'
        assert tables[6].columns[0].name == 'i1'
        assert tables[6].columns[1].name == 'd2'
        assert tables[6].rows[0].value[0] == 100
        assert tables[6].rows[0].value[1] == 1000.01
        assert tables[6].rows[1].value[0] == 200
        assert tables[6].rows[1].value[1] == 2000.02

        assert tables[7].name == 'tableBool'
        assert tables[7].columns[0].name == 'b1'
        assert tables[7].columns[1].name == 'b2'
        assert tables[7].rows[0].value[0] == true
        assert tables[7].rows[0].value[1] == true
        assert tables[7].rows[1].value[0] == false
        assert tables[7].rows[1].value[1] == false
        assert tables[7].rows[2].value[0] == true
        assert tables[7].rows[2].value[1] == false

        assert tables[8].name == 'tableIntBool'
        assert tables[8].columns[0].name == 'i1'
        assert tables[8].columns[1].name == 'b2'
        assert tables[8].rows[0].value[0] == 100
        assert tables[8].rows[0].value[1] == true
        assert tables[8].rows[1].value[0] == 200
        assert tables[8].rows[1].value[1] == false
        assert tables[8].rows[2].value[0] == 300
        assert tables[8].rows[2].value[1] == false
    }
}
