package com.yo1000.dbspock

/**
 *
 * @author yo1000
 */
class Tables {
    private List<Table> tables

    @Override
    Object invokeMethod(String name, Object args) {
        if (!(args instanceof Object[]) || args.size() <= 0) {
            return super.invokeMethod(name, args)
        }

        def arg = args[0]
        if (!(arg instanceof Closure)) {
            return super.invokeMethod(name, args)
        }

        if (tables == null) {
            tables = new ArrayList<>()
        }

        def table = new Table(name)

        Closure cl = (Closure) arg
        cl.delegate = table
        cl.run()

        tables.add(table)
    }

    def getTables() {
        return tables
    }
}

class Table {
    private String table
    private Col col
    private List<Row> rows

    Table(table) {
        Object.metaClass.or = { x ->
            if (!(delegate instanceof List)) {
                return [delegate, x]
            }
            delegate << x
        }

        this.table = table
    }

    def col(List cols) {
        col = new Col(cols)
        return col
    }

    def row(List items) {
        if (rows == null) {
            rows = new ArrayList<>()
        }
        def row = new Row(items)
        this.rows.add(row)
        return row
    }

    def getTable() {
        return table
    }

    def getCol() {
        return col
    }

    def getRows() {
        return rows
    }
}

class Col {
    List<String> values;

    Col(List values) {
        this.values = values
    }

    List getValues() {
        return values
    }
}

class Row {
    List<Object> values;

    Row(List values) {
        this.values = values
    }

    List getValues() {
        return values
    }
}