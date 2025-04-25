import org.example.Parsing.Parser;
import org.example.Storage.Storage;
import org.example.Storage.Table;
import org.example.Util.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpdateQueryTest {

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
    void testValidUpdateSingleColumn() {
        String query = "update employees set name=UpdatedName where id=3";
        Parser parser = new Parser(query);
        parser.parse();
        assertEquals("UpdatedName", Storage.hashMap.get("employees").rows.get(2).get(1));
    }

    @Test
    void testValidUpdateMultipleColumns() {
        String query = "update employees set id=10,salary=500000 where id=2";
        Parser parser = new Parser(query);
        parser.parse();
        assertEquals("10", Storage.hashMap.get("employees").rows.get(1).get(0));
        assertEquals("500000", Storage.hashMap.get("employees").rows.get(1).get(2));
    }

    @Test
    void testUpdateInvalidSyntax_Spaces() {
        String query = "update employees set salary = 123 where id = 2"; // spaces not allowed
        Parser parser = new Parser(query);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void testUpdateMissingWhereClause() {
        String query = "update employees set salary=999999"; // no `where`
        Parser parser = new Parser(query);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void testUpdateNonExistentColumn() {
        String query = "update employees set age=30 where id=1"; // `age` not in schema
        Parser parser = new Parser(query);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void testUpdateNoMatchingRow() {
        String query = "update employees set salary=0 where id=100"; // no row with id=100
        Parser parser = new Parser(query);
        parser.parse();
        // All rows should remain unchanged
        assertEquals("150000", Storage.hashMap.get("employees").rows.get(0).get(2));
        assertEquals(5, Storage.hashMap.get("employees").rows.size());
    }

    @Test
    void brokenQueries1() {
        String sqlQuery = "update";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    void brokenQueries2() {
        String sqlQuery = "update employees";
        Parser parser = new Parser(sqlQuery);
        assertThrows(RuntimeException.class, parser::parse);
    }
}
