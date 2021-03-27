package database;

import java.sql.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
	// write your code here
        try {
            Database d = new Database("jdbc:mysql://test.com:112233/DB_NAME", "johnsmith", "verySecurePasswordJohn1");

            String[] row = d.selectRow(d.getTable(4), 15000);
            System.out.println(Arrays.toString(row));
            d.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
