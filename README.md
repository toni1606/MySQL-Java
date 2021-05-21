# MySQL-Java
A simple program to connect to a remove MySQL server and get all Table Data.

When inserting, updating or deleting be careful to use the correct SQL Syntax in the values Strings (e.g: escape `'` on varchars or dates)

`Database.deleteInstance()` allows you to pass `null` as condition, if so the program **WILL** delete all of the Data from that Table.

Example Program:
```
package database;

import java.sql.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try {
            Database d = new Database("jdbc:mysql://domain.com:3306/table_name", "username", "password");

            for(Table t : d.getTables()) {
                System.out.println(t);
            }

            String[][] val = d.selectAll(d.getTable(0));
            for(String[] str: val) {
                for(String s : str) {
                    System.out.print(s + " ");
                }
                System.out.println();
            }

            d.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}

```
