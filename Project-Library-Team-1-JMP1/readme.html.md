Hogwarts Library
================

**Java Mastery Project #2**

* * *

### Contributors

*   Christina Ablanska
*   Martin Doychev
*   Branimir Borisov

### Contacts

*   [Slack group](https//codeacademyjp-pvq8900.slack.com/archives/C06R43Z5FTM)
*   [Viber group](https//codeacademyjp-pvq8900.slack.com/archives/C06R43Z5FTM)

Overview
--------

Dear reader, please welcome to Hogwarts library of Java and SQL DB!

The project represents a completely operational library developed to support 3 types of users Readers, Authors and Admins and managing their select choice of books and functionalities. The library is stored in a mysql database run on our local machines through Docker. You may find a complete list of all necessary files for running it on your local computer below in the context links.

* * *

### Technical details

*   JDBC
*   Unit tests
*   MySQL + Docker
*   Maven (with added dependency for mysql connection, version 8.0.33)

* * *

### Functionality

*   Reader
    *   Create account (username and password)
    *   Search book (by title, by author)
    *   Add book to their own library
    *   Show last read
    *   Sort library (by title, by author, by genre)
    *   Rate book
*   Author
    *   Create account (username and password)
    *   Upload book
    *   Change book access
    *   Show their own library
    *   Show statistics on a book
*   Admin
    *   Lock account
    *   Unlock account
    *   Show user details

* * *

### Java class diagram

[![Java class diagram](https://github.com/Code-Academy-BG/Project-Library-Team-1-JMP1/blob/main/AdditinalFiles/Images/JavaClassDiagram.png)](https://github.com/Code-Academy-BG/Project-Library-Team-1-JMP1/blob/main/AdditinalFiles/Images/JavaClassDiagram.png)

* * *

### SQL DB Topography

[![SQL diagram](https://github.com/Code-Academy-BG/Project-Library-Team-1-JMP1/blob/main/AdditinalFiles/Images/SQL_Topography.png)](https://github.com/Code-Academy-BG/Project-Library-Team-1-JMP1/blob/main/AdditinalFiles/Images/SQL_Topography.png)

* * *

### Additional files

*   [DDL Schema](https://github.com/Code-Academy-BG/Project-Library-Team-1-JMP1/tree/main/AdditinalFiles/Database/DDL%20Schema)
*   [Database tables](https://github.com/Code-Academy-BG/Project-Library-Team-1-JMP1/tree/main/AdditinalFiles/Database)
*   [Images](https://github.com/Code-Academy-BG/Project-Library-Team-1-JMP1/tree/main/AdditinalFiles/Images)
