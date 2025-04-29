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

public class SelectQueryTest {
    
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

    @Test
    void orderBy1() {
        String sqlQuery = "select * from employees order by salary";
        Parser parser = new Parser(sqlQuery);
        parser.parse();  // Assuming this prints or stores selected results
        assertEquals(5, Storage.hashMap.get("employees").rows.size());
    }


    @Test
    void orderBy2() {
        String sqlQuery = "select salary from employees order by salary";
        Parser parser = new Parser(sqlQuery);
        parser.parse();  // Assuming this prints or stores selected results
        assertEquals(5, Storage.hashMap.get("employees").rows.size());
    }

    @Test
    void orderBy3() {
        String sqlQuery = "select nothing from employees order by salary";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void orderBy4() {
        String sqlQuery = "select nothing from employees order salary";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void orderBy5() {
        String sqlQuery = "select nothing from employees order by";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void where1() {
        String sqlQuery = "select * from employees where salary > 200000 order by name";
        Parser parser = new Parser(sqlQuery);
        parser.parse();
        System.out.println(Storage.hashMap.get("employees"));

    }

    @Test
    void where2() {
        List<String> out = runAndCapture(
                "select * from employees where salary > 200000"
        );
        System.out.println(out);
        assertEquals(3, out.size());

    }

    @Test
    void orderByUnknownColumn() {
        String sql = "select * from employees order by height";
        Parser parser = new Parser(sql);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void whereUnknownColumn() {
        String sql = "select * from employees where height > 30";
        Parser parser = new Parser(sql);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void whereUnsupportedOperator() {
        String sql = "select * from employees where age <> 30";
        Parser parser = new Parser(sql);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void selectSpecificFieldsOrderByName() {
        String sql = "select name,salary from employees order by name";
        List<String> out = runAndCapture(sql);
        assertEquals(5, out.size());
    }

    @Test
    void selectSpecificFieldsWhereAndOrder() {
        String sql = "select name,salary from employees where salary > 200000 order by name";
        List<String> out = runAndCapture(sql);
        assertEquals(3, out.size());
    }

    @Test
    void missingFromKeywordThrows() {
        String sql = "select * employees order by salary";
        Parser parser = new Parser(sql);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void mixedCaseKeywords() {
        String sql = "SeLeCt * FrOm employees OrDeR bY salary";
        Parser parser = new Parser(sql);
        parser.parse();
        assertEquals(5, Storage.hashMap.get("employees").rows.size());
    }

    private List<String> runAndCapture(String sql) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(buf));
        try {
            new Parser(sql).parse();
        } finally {
            System.setOut(oldOut);
        }
        return buf.toString().lines()
                .map(String::trim)
                .filter(l -> !l.isEmpty())
                .toList();
    }


}
