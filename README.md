# MySQL-Java
A simple program to connect to a remove MySQL server and get all Table Data.

When inserting, updating or deleting be careful to use the correct SQL Syntax in the values Strings (e.g: escape `'` on varchars or dates)

`Database.deleteInstance()` allows you to pass `null` as condition, if so the program **WILL** delete all of the Data from that Table.
