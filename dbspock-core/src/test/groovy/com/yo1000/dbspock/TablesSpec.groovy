package com.yo1000.dbspock

import spock.lang.Specification

/**
 *
 * @author yo1000
 */
class TablesSpec extends Specification {
    def "operation"() {
        setup:
        def data = {
            table1 {
                col 'c1'   | 'c2'   | 'c3'
                row 'r1c1' | 'r1c2' | 'r1c3'
                row 'r2c1' | 'r2c2' | 'r2c3'
            }

            table2 {
                col 'cStr' | 'cInt' | 'cDate'
                row 'str1' | 100    | '2016-09-26 18:46:01'
                row 'str2' | 200    | '2016-09-26 18:46:02'
            }
        }

        data.delegate = new Tables()
        data.call()

        expect:
        assert data.tables instanceof List<Table>

        def tables = data.tables as List<Table>

        assert tables[0].table == 'table1'
        assert tables[0].col.values[0] == 'c1'
        assert tables[0].col.values[1] == 'c2'
        assert tables[0].col.values[2] == 'c3'
        assert tables[0].rows[0].values[0] == 'r1c1'
        assert tables[0].rows[0].values[1] == 'r1c2'
        assert tables[0].rows[0].values[2] == 'r1c3'
        assert tables[0].rows[1].values[0] == 'r2c1'
        assert tables[0].rows[1].values[1] == 'r2c2'
        assert tables[0].rows[1].values[2] == 'r2c3'

        assert tables[1].table == 'table2'
        assert tables[1].col.values[0] == 'cStr'
        assert tables[1].col.values[1] == 'cInt'
        assert tables[1].col.values[2] == 'cDate'
        assert tables[1].rows[0].values[0] == 'str1'
        assert tables[1].rows[0].values[1] == 100
        assert tables[1].rows[0].values[2] == '2016-09-26 18:46:01'
        assert tables[1].rows[1].values[0] == 'str2'
        assert tables[1].rows[1].values[1] == 200
        assert tables[1].rows[1].values[2] == '2016-09-26 18:46:02'
    }
}
