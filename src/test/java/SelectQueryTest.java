import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.example.Parsing.Parser;
import org.example.Storage.Storage;
import org.example.Storage.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SelectQueryTest {
    
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
    void testSelectAll() {
        String sqlQuery = "select * from employees";
        Parser parser = new Parser(sqlQuery);
        parser.parse();
        assertEquals(5, Storage.hashMap.get("employees").rows.size());
    }

    @Test
    void testSelectId() {
        String sqlQuery = "select id from employees";
        Parser parser = new Parser(sqlQuery);
        parser.parse();
        assertEquals(5, Storage.hashMap.get("employees").rows.size());
    }

    @Test
    void testSyntax1() {
        String sqlQuery = "selectid from employees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void testSyntax2() {
        String sqlQuery = "selectidfromemployees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void testSyntax3() {
        String sqlQuery = "select   id from employees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void testSyntax4() {
        String sqlQuery = "selectid from employees   ";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void testSyntax5() {
        String sqlQuery = "select id frrom employees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void testSyntax6() {
        String sqlQuery = "select idd from employees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void testSyntax7() {
        String sqlQuery = "selettct idd from employees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void testSyntax8() {
        String sqlQuery = "select id from employeees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse, "Expected parse() to throw syntax error");
    }

    @Test
    void testSelectMultipleColumns() {
        String sqlQuery = "select id,name from employees";
        Parser parser = new Parser(sqlQuery);
        parser.parse();  // Assuming this prints or stores selected results
        assertEquals(5, Storage.hashMap.get("employees").rows.size());
    }

    @Test
    void testSelectCaseInsensitive() {
        String sqlQuery = "SELECT name,salary FROM employees";
        Parser parser = new Parser(sqlQuery);
        parser.parse();
        assertEquals(5, Storage.hashMap.get("employees").rows.size());
    }

    @Test
    void testMissingFrom() {
        String sqlQuery = "select id name salary employees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void testTrailingCommaInColumns() {
        String sqlQuery = "select id, name, from employees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }


    @Test
    void testEmptySelectList() {
        String sqlQuery = "select from employees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }


    @Test
    void testSelectNonExistentColumn() {
        String sqlQuery = "select bonus from employees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void testSelectWithExtraWhitespace() {
        String sqlQuery = "select    id   ,   name    from    employees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void brokenQueries1() {
        String sqlQuery = "select";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void brokenQueries2() {
        String sqlQuery = "select * fr";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

}
