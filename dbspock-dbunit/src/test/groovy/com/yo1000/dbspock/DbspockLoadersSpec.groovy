package com.yo1000.dbspock

import com.yo1000.dbspock.dbunit.DbspockLoaders
import org.dbunit.dataset.IDataSet
import spock.lang.Specification

/**
 *
 * @author yo1000
 */
class DbspockLoadersSpec extends Specification {
    def "Create Operation instance"() {
        setup:
        def ops = DbspockLoaders.loadDataSet {
            table1 {
                col1    | col2    | col3
                "rowA1" | 'rowA2' | 'rowA3'
                "rowB1" | 'rowB2' | 'rowB3'
            }

            table2 {
                strcol   | intcol   | datecol
                "str1"   | 100      | '2016-09-26 18:46:01'
                "str2"   | 200      | '2016-09-26 18:46:02'
            }
        }

        expect:
        assert ops instanceof IDataSet
    }

    def "Create Operation instance with rotate"() {
        setup:
        def ops = DbspockLoaders.loadDataSetWithRotate {
            table1 {
                col1 | 'rowA1' | 'rowB1'
                col2 | 'rowA2' | 'rowB2'
                col3 | 'rowA3' | 'rowB3'
            }

            table2 {
                strcol  | 'str1'                | 'str2'
                intcol  | 100                   | 200
                datecol | '2016-09-26 18:46:01' | '2016-09-26 18:46:02'
            }
        }

        expect:
        assert ops instanceof IDataSet
    }
}