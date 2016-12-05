package com.yo1000.dbspock
/**
 *
 * @author yo1000
 */
class TableParser {
    protected static ThreadLocal<Table> threadLocal = new ThreadLocal<>()

    /**
     *
     * @param self Row value
     * @param operand Row
     */
    static or(self, operand) {
        Row row = new Item(self) | operand

        if (threadLocal.get().rows == null) {
            threadLocal.get().rows = new Rows()
        }
        threadLocal.get().rows << row
        return row
    }

    /**
     *
     * @param self Row value
     * @param operand Row
     */
    static or(Number self, Number operand) {
        Row row = new Item(self) | operand

        if (threadLocal.get().rows == null) {
            threadLocal.get().rows = new Rows()
        }
        threadLocal.get().rows << row
        return row
    }

    /**
     *
     * @param self Row value
     * @param operand Row
     */
    static or(Boolean self, Boolean operand) {
        Row row = new Item(self) | operand

        if (threadLocal.get().rows == null) {
            threadLocal.get().rows = new Rows()
        }
        threadLocal.get().rows << row
        return row
    }

    /**
     *
     * @param name Column name
     * @return Column
     */
    def propertyMissing(String name) {
        Columns columns = threadLocal.get().columns
        if (columns == null) {
            columns = new Columns()
        }

        Column column = new Column(name)

        columns << column
        threadLocal.get().columns = columns
        return column
    }

    static row(Object o) {
        Row row = new Row(new Item(o))

        if (threadLocal.get().rows == null) {
            threadLocal.get().rows = new Rows()
        }

        threadLocal.get().rows << row
        return row
    }

    static r(o) {
        return row(o)
    }
}

class Tables {
    private List<Table> tables = []

    Tables() {}

    Tables(Table... tables) {
        tables.each {
            this.tables << it
        }
    }

    @Override
    Object invokeMethod(String name, Object args) {
        if (args == null) {
            throw new NullPointerException()
        }

        if (!(args instanceof Object[]) || !(args[0] instanceof Closure)) {
            throw new IllegalArgumentException()
        }

        Closure data = args[0] as Closure
        TableParser.threadLocal.set(new Table(name))
        use (TableParser) {
            data.delegate = new TableParser()
            data.call()
        }
        tables << TableParser.threadLocal.get()
    }

    List<Table> getTables() {
        return tables
    }

    static List<Table> asList(Closure data) {
        data.delegate = new Tables()
        data.call()
        return data.tables
    }
}

class Table {
    private String name
    private Columns columns
    private Rows rows

    Table(name) {
        this.name = name
    }

    String getName() {
        return name
    }

    Columns getColumns() {
        return columns
    }

    void setColumns(Columns columns) {
        this.columns = columns
    }

    Rows getRows() {
        return rows
    }

    void setRows(Rows rows) {
        this.rows = rows
    }

    @Override
    public String toString() {
        def columnsBuilder = new StringBuilder()
        columns.each {
            if (columnsBuilder.length() > 0)
                columnsBuilder.append(", ")
            columnsBuilder.append("'${it.name}'")
        }

        def rowsBuilder = new StringBuilder()
        rows.each {
            if (rowsBuilder.length() > 0)
                rowsBuilder.append(", ")

            rowsBuilder.append("{")
            def rowBuilder = new StringBuilder()
            it.each {
                if (rowBuilder.length() > 0)
                    rowBuilder.append(", ")
                rowBuilder.append(it.value)
            }
            rowsBuilder.append(rowBuilder.toString())
            rowsBuilder.append("}")
        }

        return new StringBuilder("Table{")
                .append("name='${name}', ")
                .append("columns=[")
                .append(columnsBuilder.toString())
                .append("], ")
                .append("rows=[")
                .append(rowsBuilder.toString())
                .append("]")
                .append("}")
                .toString()
    }
}

class Column {
    private String name

    Column(String name) {
        this.name = name
    }

    Columns or(Column column) {
        return new Columns(this, column)
    }

    String getName() {
        return name
    }
}

class Columns extends ArrayList<Column> {
    Columns(Column... columns) {
        columns.each {
            this << it
        }
    }

    Columns or(Column column) {
        this << column
        return this
    }
}

class Item {
    private Object value

    Item(Object value) {
        this.value = value
    }

    Row or(Object value) {
        if (value instanceof Item) {
            return new Row(this, value)
        }
        return new Row(this, new Item(value))
    }

    Object getValue() {
        return value
    }
}

class Rows extends ArrayList<Row> {
    Rows(Row... rows) {
        rows.each {
            this << it
        }
    }
}

class Row extends ArrayList<Item> {
    Row(Item... items) {
        items.each {
            this << it
        }
    }

    Row or(Object value) {
        if (value instanceof Item) {
            return this << value
        }
        this << new Item(value)
        return this
    }
}
