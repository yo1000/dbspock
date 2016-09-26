package com.yo1000.dbspock.dbsetup

import com.ninja_squad.dbsetup.operation.Operation
import spock.lang.Specification

/**
 *
 * @author yo1000
 */
class DbspockOperationsSpec extends Specification {
    def "Create DataSet instance"() {
        setup:
        def ops = DbspockOperations.insertInto {
            table1 {
                col "col1"  | "col2"  | "col3"
                row "rowA1" | 'rowA2' | 'rowA3'
                row "rowB1" | 'rowB2' | 'rowB3'
            }

            table2 {
                col "strcol" | "intcol" | "datecol"
                row "str1"   | 100      | '2016-09-26 18:46:01'
                row "str2"   | 200      | '2016-09-26 18:46:02'
            }
        }

        expect:
        assert ops instanceof Operation
    }
}