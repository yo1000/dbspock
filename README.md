# dbspock

Expresses the data as Spockly!

# Usage

Can be used in `DbSetup` or `DBUnit`

## for DbSetup

Dependency

```xml
<dependencies>
    <dependency>
        <groupId>com.yo1000</groupId>
        <artifactId>dbspock-core</artifactId>
        <version>2.3.0.RELEASE</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.yo1000</groupId>
        <artifactId>dbspock-dbsetup</artifactId>
        <version>2.3.0.RELEASE</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.yo1000</groupId>
        <artifactId>dbspock-jdbc</artifactId>
        <version>2.3.0.RELEASE</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.ninja-squad</groupId>
        <artifactId>DbSetup</artifactId>
        <version>2.1.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Setup in spec

```groovy
setup:
def insertOps = DbspockOperations.insertInto {
    test_table {
        test_int | test_str | test_date
        100      | 'test1'  | '2016-09-26 23:20:01.0'
        200      | 'test2'  | '2016-09-26 23:20:02.0'
        300      | 'test3'  | '2016-09-26 23:20:03.0'
    }
}

def destination = new DriverManagerDestination(URL, USERNAME, PASSWORD)
new DbSetup(destination,
        Operations.sequenceOf(
                Operations.truncate('test_table'),
                insertOps
        )
).launch()
```

When using a rotate support

```groovy
setup:
def insertOps = DbspockOperations.insertIntoWithRotate {
    test_table {
        test_int  | 100                     | 200                     | 300
        test_str  | 'test1'                 | 'test2'                 | 'test3'
        test_date | '2016-09-26 23:20:01.0' | '2016-09-26 23:20:02.0' | '2016-09-26 23:20:03.0'
    }
}

def destination = new DriverManagerDestination(URL, USERNAME, PASSWORD)
new DbSetup(destination,
        Operations.sequenceOf(
                Operations.truncate('test_table'),
                insertOps
        )
).launch()
```

When using a expectation support.

```groovy
setup:
def insertOps = DbspockOperations.insertInto {
    test_table {
        test_int | test_str | test_date
        100      | 'test1'  | '2016-09-26 23:20:01.0'
        200      | 'test2'  | '2016-09-26 23:20:02.0'
        300      | 'test3'  | '2016-09-26 23:20:03.0'
    }
}

def destination = new DriverManagerDestination(URL, USERNAME, PASSWORD)
new DbSetup(destination,
        Operations.sequenceOf(
                Operations.truncate('test_table'),
                insertOps
        )
).launch()

expect:
DbspockExpectations.matches(destination.connection, {
    test_table {
        test_int | test_str | test_date
        100      | 'test1'  | '2016-09-26 23:20:01.0'
        200      | 'test2'  | '2016-09-26 23:20:02.0'
        300      | 'test3'  | '2016-09-26 23:20:03.0'
    }
})

DbspockExpectations.containsAll(destination.connection, {
    test_table {
        test_int | test_str | test_date
        100      | 'test1'  | '2016-09-26 23:20:01.0'
        300      | 'test3'  | '2016-09-26 23:20:03.0'
    }
})

DbspockExpectations.containsAny(destination.connection, {
    test_table {
        test_int | test_str | test_date
        100      | 'test1'  | '2016-09-26 23:20:01.0'
        900      | 'test9'  | '2020-09-26 23:20:03.0'
    }
})

DbspockExpectations.containsNone(destination.connection, {
    test_table {
        test_int | test_str | test_date
        900      | 'test9'  | '2020-09-26 23:20:03.0'
    }
})
```

## for DBUnit

Dependency

```xml
<dependencies>
    <dependency>
        <groupId>com.yo1000</groupId>
        <artifactId>dbspock-core</artifactId>
        <version>2.3.0.RELEASE</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.yo1000</groupId>
        <artifactId>dbspock-dbunit</artifactId>
        <version>2.3.0.RELEASE</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.dbunit</groupId>
        <artifactId>dbunit</artifactId>
        <version>2.5.3</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Setup in spec

```groovy
setup:
def dataSet = DbspockLoaders.loadDataSet {
    test_table {
        test_int | test_str | test_date
        100      | 'test1'  | '2016-09-26 23:20:01.0'
        200      | 'test2'  | '2016-09-26 23:20:02.0'
        300      | 'test3'  | '2016-09-26 23:20:03.0'
    }
}

def databaseTester = new JdbcDatabaseTester(Driver.class.getName(), URL, USERNAME, PASSWORD)
databaseTester.setUpOperation = DatabaseOperation.CLEAN_INSERT

databaseTester.dataSet = dataSet
databaseTester.onSetup()
```

When using a rotate support

```groovy
setup:
def dataSet = DbspockLoaders.loadDataSetWithRotate {
    test_table {
        test_int  | 100                     | 200                     | 300
        test_str  | 'test1'                 | 'test2'                 | 'test3'
        test_date | '2016-09-26 23:20:01.0' | '2016-09-26 23:20:02.0' | '2016-09-26 23:20:03.0'
    }
}

def databaseTester = new JdbcDatabaseTester(Driver.class.getName(), URL, USERNAME, PASSWORD)
databaseTester.setUpOperation = DatabaseOperation.CLEAN_INSERT

databaseTester.dataSet = dataSet
databaseTester.onSetup()
```

When using a expectation support.

```groovy
setup:
def dataSet = DbspockLoaders.loadDataSet {
    test_table {
        test_int | test_str | test_date
        100      | 'test1'  | '2016-09-26 23:20:01.0'
        200      | 'test2'  | '2016-09-26 23:20:02.0'
        300      | 'test3'  | '2016-09-26 23:20:03.0'
    }
}

def databaseTester = new JdbcDatabaseTester(Driver.class.getName(), URL, USERNAME, PASSWORD)
databaseTester.setUpOperation = DatabaseOperation.CLEAN_INSERT

databaseTester.dataSet = dataSet
databaseTester.onSetup()

expect:
DbspockExpectations.matches(databaseTester.connection, {
    test_table {
        test_int | test_str | test_date
        100      | 'test1'  | '2016-09-26 23:20:01.0'
        200      | 'test2'  | '2016-09-26 23:20:02.0'
        300      | 'test3'  | '2016-09-26 23:20:03.0'
    }
})

DbspockExpectations.containsAll(databaseTester.connection, {
    test_table {
        test_int | test_str | test_date
        100      | 'test1'  | '2016-09-26 23:20:01.0'
        300      | 'test3'  | '2016-09-26 23:20:03.0'
    }
})

DbspockExpectations.containsAny(databaseTester.connection, {
    test_table {
        test_int | test_str | test_date
        100      | 'test1'  | '2016-09-26 23:20:01.0'
        900      | 'test9'  | '2020-09-26 23:20:03.0'
    }
})

DbspockExpectations.containsNone(databaseTester.connection, {
    test_table {
        test_int | test_str | test_date
        900      | 'test9'  | '2020-09-26 23:20:03.0'
    }
})
```
