# dbspock
Using Spock with DBUnit.

# Usage

pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>com.yo1000</groupId>
        <artifactId>dbspock</artifactId>
        <version>0.1.1-RELEASE</version>
    </dependency>
</dependencies>
    
<repositories>
    <repository>
        <id>com.yo1000</id>
        <name>yo1000 maven repository</name>
        <url>http://yo1000.github.io/maven/</url>
    </repository>
</repositories>
```

Spec class

```groovy
class RepositorySpec extends Specification {
    def "DBSpockTest"() {
        setup:
        def tester = new DataSourceDatabaseTester(dataSource)

        def data = {
            _cols_ 'SHOP_ID' | 'SHOP_NAME'     | 'SHOP_CREATED' | 'SHOP_MODIFIED'
            shop   'SP-1'    | 'BURGER KING'   | '2015-04-01'   | '2015-04-01'
            shop   'SP-2'    | 'RANDYS DONUTS' | '2015-04-01'   | '2015-04-01'

            _cols_   'CSTM_ID' | 'CSTM_NAME'    | 'CSTM_SEX' | 'CSTM_CREATED' | 'CSTM_MODIFIED'
            customer 'CS1X'    | 'Tony Stark'   | '1'        | '2015-04-01'   | '2015-04-01'
            customer 'CS2X'    | 'PEPPER Potts' | '2'        | '2015-04-01'   | '2015-04-01'
            
            build()
        }
        data.delegate = new SpockLikeFlatXmlBuilder()

        tester.dataSet = new FlatXmlDataSet(new StringReader(data.call()))
        tester.onSetup()
        
        expect:
        // Something with DB access.
        
        where:
        // Setting parameters by Spock.
    }
}
```
