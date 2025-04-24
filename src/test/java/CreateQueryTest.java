import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.example.Parsing.Parser;
import org.example.Storage.Storage;
import org.example.Storage.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateQueryTest {
    
    @BeforeEach
    void setup() {
        Table table = new Table("employees", Arrays.asList("id", "name", "salary"), new ArrayList<>());
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
        String sqlQuery = "create table employee (id,age,name,salary)";
        Parser parser = new Parser(sqlQuery);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        parser.parse();
        String output = outContent.toString().trim();
        assertEquals("Inserted 4 columns into table employee", output);
    }


    @Test
    void syntaxError1() {
        String sqlQuery = "create tabble employee (id,age,name,salary)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError2() {
        String sqlQuery = "create tabble employee (id,age,name,salary)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError3() {
        String sqlQuery = "creaete table employee (id,age,name,salary)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError4() {
        String sqlQuery = "create tabble employssee (id,age,name,salary)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError5() {
        String sqlQuery = "create tabble employee (id,age,,,name,salary)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError6() {
        String sqlQuery = "create tabble employee (id,age,,,name,salary   )";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError7() {
        String sqlQuery = "create tabble employee ((id,age,,,name,salary)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError8() {
        String sqlQuery = "create tabble employee (id,age,name,salary)))";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError9() {
        String sqlQuery = "create tableemployee (id,age,name,salary)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError10() {
        String sqlQuery = "createtable employee (id,age,name,salary)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void syntaxError11() {
        String sqlQuery = "create insert tableemployee (id,age,name,salary)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void createTableOneColumn() {
        String sqlQuery = "create table singlecol (id)";
        Parser parser = new Parser(sqlQuery);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        parser.parse();
        String output = outContent.toString().trim();
        assertEquals("Inserted 1 columns into table singlecol", output);
    }
    
    @Test
    void createTableWithExtraSpaces() {
        String sqlQuery = "  create   table   spaced_table   (  id  ,  age , name )  ";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void createTableWithUppercase() {
        String sqlQuery = "CREATE TABLE CITY (ID,AGE,NAME)";
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
        String sqlQuery = "create table comma_fail (id, age,)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void createTableUnclosedParenthesis() {
        String sqlQuery = "create table unclosed (id, age, name";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void createTableDoubleComma() {
        String sqlQuery = "create table broken (id,,age,name)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void createTableMissingKeyword() {
        String sqlQuery = "create employee (id, age, name)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void createTableWithInvalidColumnNames() {
        String sqlQuery = "create table test ($id, name, age)";
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
