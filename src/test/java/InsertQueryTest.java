import org.example.Parsing.Parser;
import org.example.Storage.Storage;
import org.example.Storage.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InsertQueryTest {

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
    void testValidInsertWithoutSpacesInBrackets() {
        String sqlQuery = "insert into employees values(6,John,300000)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);

    }

    @Test
    void testInsertFailsWithSpaceInsideBrackets() {
        String sqlQuery = "insert into employees values(6, John, 300000)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void testInsertFailsWithSpaceBeforeOpeningBracket() {
        String sqlQuery = "insert into employees values (6,John,300000)";
        Parser parser = new Parser(sqlQuery);
        parser.parse();
        assertEquals("John", Storage.hashMap.get("employees").rows.get(5).get(1));
    }

    @Test
    void testInsertFailsWithTrailingSpaceInKeyword() {
        String sqlQuery = "insert into employees values(6,John,300000)"; // space before values
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void testInsertWithMissingValuesKeyword() {
        String sqlQuery = "insert into employees(6,John,300000)"; // missing `values`
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void testInsertEmptyValues() {
        String sqlQuery = "insert into employees values()";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }


    @Test
    void testInsertWithExtraValues() {
        String sqlQuery = "insert into employees values(7,Alice,400000,Extra)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void testInsertWithMissingValues() {
        String sqlQuery = "insert into employees values(8)";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }
}
