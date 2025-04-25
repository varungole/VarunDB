import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.Parsing.Parser;
import org.example.Storage.Storage;
import org.example.Storage.Table;
import org.example.Util.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateQueryTest {
    
    @BeforeEach
    void setup() {
        Table table = new Table("employees", Arrays.asList("id", "name", "salary"),Arrays.asList(ColumnType.INTEGER, ColumnType.STRING, ColumnType.INTEGER),new ArrayList<>());
        table.rows.add(Arrays.asList("1", "Varun", "150000"));
        table.rows.add(Arrays.asList("2", "Ashish", "175000"));
        table.rows.add(Arrays.asList("3", "Dmytro", "200000"));
        table.rows.add(Arrays.asList("4", "Pavel", "350000"));
        table.rows.add(Arrays.asList("5", "Jeetendra", "250000"));
        Storage.hashMap.clear();
        Storage.hashMap.put("employees", table);
    }

    @Test
    void createTableBasic() {
        String sqlQuery = "create table employee (id int,age int,name string,salary int)";
        Parser parser = new Parser(sqlQuery);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        parser.parse();
        String output = outContent.toString().trim();
        assertEquals("Inserted 4 columns into table employee", output);
    }


    @Test
    void syntaxError1() {
        String sqlQuery = "create tabble employee (id int,age int,name string,salary int)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError2() {
        String sqlQuery = "create tabble employee (id int,age int,name string,salary int)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError3() {
        String sqlQuery = "creaete table employee (id int,age int,name string,salary int)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError4() {
        String sqlQuery = "create tabble employssee (id int,age int,name string,salary int)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError5() {
        String sqlQuery = "create tabble employee (id int,age int,,,name string,salary int)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError6() {
        String sqlQuery = "create tabble employee (id int,age int,,,name int,salary int   )";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError7() {
        String sqlQuery = "create tabble employee ((id int,age int,,,name int,salary int)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError8() {
        String sqlQuery = "create tabble employee (id int,age int,name string,salary int)))";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError9() {
        String sqlQuery = "create tableemployee (id int,age int,name string,salary int)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError10() {
        String sqlQuery = "createtable employee (id int,age int,name string,salary int)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError11() {
        String sqlQuery = "create insert tableemployee (id int,age int,name string,salary int)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void createTableOneColumn() {
        String sqlQuery = "create table singlecol (id int)";
        Parser parser = new Parser(sqlQuery);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        parser.parse();
        String output = outContent.toString().trim();
        assertEquals("Inserted 1 columns into table singlecol", output);
    }
    
    @Test
    void createTableWithExtraSpaces() {
        String sqlQuery = "  create   table   spaced_table   (  id int  ,  age int , name string )  ";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void createTableWithUppercase() {
        String sqlQuery = "CREATE TABLE CITY (ID INT,AGE INT,NAME STRING)";
        Parser parser = new Parser(sqlQuery);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        parser.parse();
        String output = outContent.toString().trim();
        assertEquals("Inserted 3 columns into table CITY", output);
    }
    
    @Test
    void createTableEmptyColumns() {
        String sqlQuery = "create table empty ()";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void createTableTrailingComma() {
        String sqlQuery = "create table comma_fail (id int, age int,)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void createTableUnclosedParenthesis() {
        String sqlQuery = "create table unclosed (id int, age int, name int";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void createTableDoubleComma() {
        String sqlQuery = "create table broken (id int,,age int,name string)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void createTableMissingKeyword() {
        String sqlQuery = "create employee (id int, age int, name string)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void createTableWithInvalidColumnNames() {
        String sqlQuery = "create table test ($id int, name string, age int)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void broken1() {
        String sqlQuery = "create employee (id";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void broken2() {
        String sqlQuery = "create ta                    ";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }
}
