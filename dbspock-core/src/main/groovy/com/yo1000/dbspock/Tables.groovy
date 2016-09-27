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
    private String name
    private Col col
    private List<Row> rows

    Table(name) {
        Object.metaClass.or = { x ->
            if (!(delegate instanceof List)) {
                return [delegate, x]
            }
            delegate << x
        }

        this.name = name
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

    def getName() {
        return name
    }

    def getCol() {
        return col
    }

    def getRows() {
        return rows
    }
}

class Col {
    List<String> names;

    Col(List names) {
        this.names = names
    }

    List getNames() {
        return names
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
