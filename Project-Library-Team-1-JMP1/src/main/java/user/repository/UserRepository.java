package user.repository;

import book.Book;
import book.BookRepository;
import dbConnection.DBConnectionManager;
import enums.BookAccess;
import library.Library;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.String.valueOf;

public class UserRepository implements IUserRepository {

    private final Connection connection;

    public UserRepository() throws SQLException {
        DBConnectionManager instance = DBConnectionManager.getInstance();
        this.connection = instance.getConnection();
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean userExistsInGeneralDB(int userID) {
        int userCount = 0;

        try {
            String selectQuery = "select count(*) from user u where UserID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, valueOf(userID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                userCount = resultSet.getInt("count(*)");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return userCount == 1;
    }

    @Override
    public int getUserLibraryID(int userID) {
        int libraryID = -1;
        try {
            String selectQuery = "select ul.LibraryID from user u join userLibrary ul on u.UserID = ul.UserID where u.UserID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, valueOf(userID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                libraryID = resultSet.getInt("LibraryID");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return libraryID;
    }

    @Override
    public Library getUserLibrary(int userID) {
        Library library = null;
        int libraryID = -1;
        String libraryName = "";
        HashMap<Book, LocalDate> books = new HashMap<>();

        try {
            String selectQuery = "select ul.LibraryID, l.LibraryName from userLibrary ul " +
                    "join library l on ul.LibraryID = l.libraryID " +
                    "join user u on ul.UserID = u.UserID where u.UserID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, valueOf(userID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                libraryID = resultSet.getInt("LibraryID");
                libraryName = resultSet.getString("LibraryName");
            }

            String selectBooks = "select * from bookLibrary bl where bl.LibraryID = ?";
            PreparedStatement selectBooksStatement = this.connection.prepareStatement(selectBooks);
            selectBooksStatement.setString(1, valueOf(libraryID));
            ResultSet resultSetBooks = selectBooksStatement.executeQuery();

            if (resultSetBooks.next()) {
                int bookID = resultSetBooks.getInt("BookID");
                boolean readFlag = resultSetBooks.getBoolean("ReadFlag");
                LocalDate date = resultSetBooks.getDate("DateRead").toLocalDate();

                Book book = new Book(bookID, readFlag);
                books.put(book, date);
            }
            library = new Library(libraryID, libraryName, books);
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return library;
    }

    @Override
    public ArrayList<Book> getUserLibraryListSortedBy(int userID, String sortBy) {
        ArrayList<Book> books = new ArrayList<>();
        int libraryID = getUserLibraryID(userID);

        try {
              String selectBooks = "select b.BookID, b.Title, concat(u.FirstName, \" \", u.LastName) as AuthorName, b.ISBN, l.languageName, g.genreName, b.rating, b.AccessID, bl.ReadFlag, bl.DateRead from book b\n" +
                      "join bookLibrary bl on b.BookID = bl.BookID \n" +
                      "join language l on b.LanguageID = l.languageID\n" +
                      "join genre g on b.GenreID = g.genreID \n" +
                      "join author a on b.AuthorID = a.authorID \n" +
                      "join user u on a.UserID = u.UserID\n" +
                      "where bl.LibraryID = ?\n" +
                      "order by " + sortBy;
            PreparedStatement selectBooksStatement = this.connection.prepareStatement(selectBooks);
            selectBooksStatement.setString(1, valueOf(libraryID));
            ResultSet resultSetBooks = selectBooksStatement.executeQuery();

            while (resultSetBooks.next()) {
                int bookID = resultSetBooks.getInt("BookID");
                String title = resultSetBooks.getString("Title");
                String author = resultSetBooks.getString("AuthorName");
                String ISBN = resultSetBooks.getString("ISBN");
                String genre = resultSetBooks.getString("genreName");
                String language = resultSetBooks.getString("languageName");
                double rating = resultSetBooks.getDouble("rating");
                int accessID = resultSetBooks.getInt("AccessID");
                BookAccess access = BookRepository.getTheBookAccess(accessID);
                boolean readFlag = resultSetBooks.getBoolean("ReadFlag");
                LocalDate date = resultSetBooks.getDate("DateRead").toLocalDate();

                Book book = new Book(bookID, title, author, ISBN, genre, language, rating, access, readFlag);
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return books;
    }


    public static String encryptPassword(String password) {
        String encryptedpassword = null;
        try
        {
            /* MessageDigest instance for MD5. */
            MessageDigest m = MessageDigest.getInstance("MD5");

            /* Add plain-text password bytes to digest using MD5 update() method. */
            m.update(password.getBytes());

            /* Convert the hash value into bytes */
            byte[] bytes = m.digest();

            /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            /* Complete hashed password in hexadecimal format */
            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        /* Display the unencrypted and encrypted passwords. */
//        System.out.println("Plain-text password: " + password);
//        System.out.println("Encrypted password using MD5: " + encryptedpassword);

        return encryptedpassword;
    }

    @Override
    public boolean isLocked(int userID) {
        boolean result = false;
        try {
            String selectQuery = "select isLocked from user\n" +
                    "where UserID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, valueOf(userID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getBoolean("isLocked");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return result;
    }
}
