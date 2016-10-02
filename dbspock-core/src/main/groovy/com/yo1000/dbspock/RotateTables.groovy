package com.yo1000.dbspock
/**
 *
 * @author yo1000
 */
class RotateTableParser {
    protected static ThreadLocal<RotateTable> threadLocal = new ThreadLocal<>()

    /**
     *
     * @param name Column name
     * @return Column
     */
    def propertyMissing(String name) {
        ColumnAndItems columnAndItems = new ColumnAndItems(new Column(name))

        threadLocal.get().columnAndItemsList << columnAndItems
        return columnAndItems
    }
}

class RotateTables {
    private List<RotateTable> tables = []

    @Override
    Object invokeMethod(String name, Object args) {
        if (args == null) {
            throw new NullPointerException()
        }

        if (!(args instanceof Object[]) || !(args[0] instanceof Closure)) {
            throw new IllegalArgumentException()
        }

        Closure data = args[0] as Closure
        RotateTableParser.threadLocal.set(new RotateTable(name))
        use (RotateTableParser) {
            data.delegate = new RotateTableParser()
            data.call()
        }
        tables << RotateTableParser.threadLocal.get()
    }

    Tables rotate() {
        List<Table> tables = []

        this.tables.each {
            tables << it.rotate()
        }

        return new Tables(tables as Table[])
    }

    List<RotateTable> getTables() {
        return tables
    }
}

class RotateTable {
    private String name
    private List<ColumnAndItems> columnAndItemsList = []

    RotateTable(name) {
        this.name = name
    }

    Table rotate() {
        def initialized = false
        def table = new Table(name)
        table.columns = new Columns()
        table.rows = new Rows()

        columnAndItemsList.each {
            table.columns | it.column

            def items = it.items
            def itemSize = it.items.size()

            (0..<itemSize).each {
                if (!initialized) {
                    table.rows << new Row()
                }

                table.rows[it] | items[it]
            }

            initialized = true
        }

        return table
    }

    String getName() {
        return name
    }

    List<ColumnAndItems> getColumnAndItemsList() {
        return columnAndItemsList
    }
}

class ColumnAndItems {
    private Column column
    private List<Item> items = []

    ColumnAndItems(Column column) {
        this.column = column
    }

    ColumnAndItems or(Object value) {
        if (value instanceof Item) {
            items << value
            return this
        }
        items << new Item(value)
        return this
    }


    Column getColumn() {
        return column
    }

    List<Item> getItems() {
        return items
    }
}
