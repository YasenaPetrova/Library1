package book;

import dbConnection.DBConnectionManager;
import enums.BookAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookRepository implements IBookRepository {
    private final Connection connection;

    @Override
    public Connection getConnection() {
        return connection;
    }

    public BookRepository() throws SQLException {
        DBConnectionManager instance = DBConnectionManager.getInstance();
        this.connection = instance.getConnection();
    }

    @Override
    public ArrayList<Book> searchBookByName(String bookName) {
        ArrayList<Book> result = new ArrayList<>();
        int bookID;
        String title;
        String author;
        String ISBN;
        String genre;
        String language;
        double rating;
        BookAccess access;

        try {
            String selectQuery = "SELECT * FROM book where Title like ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, bookName);
            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                bookID = resultSet.getInt("BookID");
                title = resultSet.getString("Title");
                author = getAuthor(resultSet.getInt("AuthorID"));
                ISBN = resultSet.getString("ISBN");
                genre = getGenre(resultSet.getInt("GenreID"));
                language = getLanguage(resultSet.getInt("LanguageID"));
                rating = resultSet.getDouble("rating");
                access = getBookAccess(resultSet.getInt("AccessID"));

                Book book = new Book(bookID, title, author, ISBN, genre, language, rating, access, false);
                result.add(book);
            }

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return result;
    }

    @Override
    public Book getBookByID(int bookID) throws SQLException {
        String title = "";
        String author = "";
        String ISBN = "";
        String genre = "";
        String language = "";
        double rating = 0.0;
        BookAccess access = BookAccess.AVAILABLE;

        try {
            String selectQuery = "SELECT * FROM book where BookID like ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(bookID));
            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                bookID = resultSet.getInt("BookID");
                title = resultSet.getString("Title");
                author = getAuthor(resultSet.getInt("AuthorID"));
                ISBN = resultSet.getString("ISBN");
                genre = getGenre(resultSet.getInt("GenreID"));
                language = getLanguage(resultSet.getInt("LanguageID"));
                rating = resultSet.getDouble("rating");
                access = getBookAccess(resultSet.getInt("AccessID"));
            }

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return new Book(bookID, title, author, ISBN, genre, language, rating, access, false);
    }

    @Override
    public ArrayList<Book> searchBookByAuthor(String authorName) {
        ArrayList<Book> result = new ArrayList<>();
        int bookID;
        String title;
        String author;
        String ISBN;
        String genre;
        String language;
        double rating;
        BookAccess access;

        try {
            String selectQuery = "select * from book b where b.AuthorID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            int authorID = getAuthorId(authorName);
            selectStatement.setString(1, String.valueOf(authorID));
            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                bookID = resultSet.getInt("BookID");
                title = resultSet.getString("Title");
                author = getAuthor(resultSet.getInt("AuthorID"));
                ISBN = resultSet.getString("ISBN");
                genre = getGenre(resultSet.getInt("GenreID"));
                language = getLanguage(resultSet.getInt("LanguageID"));
                rating = resultSet.getDouble("rating");
                access = getBookAccess(resultSet.getInt("AccessID"));

                Book book = new Book(bookID, title, author, ISBN, genre, language, rating, access, false);
                result.add(book);
            }

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return result;
    }

    private BookAccess getBookAccess(int access) {
        BookAccess bookAccess = BookAccess.AVAILABLE;
        switch (access) {
            case 2:
                bookAccess = BookAccess.STAGED;
                break;
            case 3:
                bookAccess = BookAccess.ANNOUNCED;
                break;
        }
        return bookAccess;
    }


    public static BookAccess getTheBookAccess(int access) {
        BookAccess bookAccess = BookAccess.AVAILABLE;
        switch (access) {
            case 2:
                bookAccess = BookAccess.STAGED;
                break;
            case 3:
                bookAccess = BookAccess.ANNOUNCED;
                break;
        }
        return bookAccess;
    }

    @Override
    public String getAuthor(int authorID) {
        String authorName = "";

        try {
            String selectQuery = "select u.FirstName , u.LastName from user u join author a on u.UserID = a.UserID where a.authorID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(authorID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                authorName = resultSet.getString("FirstName") + " " + resultSet.getString("LastName");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return authorName;
    }

    @Override
    public int getAuthorId(String authorName) {
        int authorID = 0;

        try {
            String selectQuery = "select a.authorID  from author a join user u on a.UserID = u.UserID where CONCAT(u.FirstName, \" \", u.LastName)  like ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, authorName);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                authorID = resultSet.getInt("authorID");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return authorID;
    }

    @Override
    public String getGenre(int genreID) {
        String genreName = "";

        try {
            String selectQuery = "select g.genreName from genre g where genreID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(genreID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                genreName = resultSet.getString("genreName");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return genreName;
    }

    @Override
    public String getLanguage(int languageID) {
        String languageName = "";

        try {
            String selectQuery = "select l.languageName from language l where l.languageID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(languageID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                languageName = resultSet.getString("languageName");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return languageName;
    }

    @Override
    public boolean bookExistsInGeneralLibrary(String bookName) {
        int bookCount = 0;

        try {
            String selectQuery = "select count(*) from book b where Title like ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(bookName));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                bookCount = resultSet.getInt("count(*)");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return bookCount > 0;
    }

    @Override
    public boolean authorExistsInGeneralLibrary(String authorName) {
        int authorCount = 0;

        try {
            String selectQuery = "select count(*) from user u join\n" +
                    "author a on u.UserID = a.UserID\n" +
                    "where concat(u.FirstName, \" \", u.LastName) like ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(authorName));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                authorCount = resultSet.getInt("count(*)");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return authorCount > 0;
    }

    public String getBookNameByBookID(int bookID) {
        String bookName = "";

        try {
            String selectQuery = "select Title from book b where BookID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(bookID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                bookName = resultSet.getString("Title");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return bookName;
    }

    public String getAuthorByBookID(int bookID) {
        String authorName = "";

        try {
            String selectQuery = "select AuthorID from book b where BookID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(bookID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int authorID = resultSet.getInt("AuthorID");
                authorName = getAuthor(authorID);
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return authorName;
    }

    public String getISBNByBookID(int bookID) {
        String bookISBN = "";

        try {
            String selectQuery = "select ISBN from book b where BookID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(bookID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                bookISBN = resultSet.getString("ISBN");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return bookISBN;
    }

    public String getGenreByBookID(int bookID) {
        String bookGenre = "";

        try {
            String selectQuery = "select GenreID from book b where BookID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(bookID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int genreID = resultSet.getInt("GenreID");
                bookGenre = getGenre(genreID);
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return bookGenre;
    }

    public String getLanguageByBookID(int bookID) {
        String bookLanguage = "";

        try {
            String selectQuery = "select LanguageID from book b where BookID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(bookID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int languageID = resultSet.getInt("LanguageID");
                bookLanguage = getLanguage(languageID);
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return bookLanguage;
    }

    public Double getRatingByBookID(int bookID) {
        Double bookRating = 0.0;

        try {
            String selectQuery = "select rating from book b where BookID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(bookID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                bookRating = resultSet.getDouble("rating");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return bookRating;
    }

    public BookAccess getAccessByBookID(int bookID) {
        BookAccess bookAccess = BookAccess.AVAILABLE;

        try {
            String selectQuery = "select AccessID from book b where BookID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(bookID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int accessID = resultSet.getInt("AccessID");
                bookAccess = getBookAccess(accessID);
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return bookAccess;
    }

    @Override
    public void rateBook(int bookID, int userID, double rating) throws SQLException {
        boolean added = addRatingToUserRatingTable(bookID, userID, rating);
        if (added) {
            double updatedRating = getAverageRatingFromDB(bookID);
            setRatingByBookIDInBookTable(bookID, updatedRating);
        }
    }

    @Override
    public boolean ratingExistsInUserRating(int bookID, int userID) {
        int countRows = 0;
        try {
            String selectQuery = "select count(*) as records from UserRating ur \n" +
                    "where bookID = ?\n" +
                    "and userID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(bookID));
            selectStatement.setString(2, String.valueOf(userID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                countRows = resultSet.getInt("records");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return countRows > 0;
    }

    public boolean addRatingToUserRatingTable(int bookID, int userID, double rating) {
        int rowsInserted = 0;
        try {
            String insertQuery = "INSERT INTO academy.UserRating\n" +
                    "(bookID, userID, rating)\n" +
                    "VALUES(?, ?, ?)";
            PreparedStatement insertStatement = this.connection.prepareStatement(insertQuery);
            insertStatement.setString(1, String.valueOf(bookID));
            insertStatement.setString(2, String.valueOf(userID));
            insertStatement.setString(3, String.valueOf(rating));
            rowsInserted = insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return rowsInserted > 0;
    }

    public double getAverageRatingFromDB(int bookID) {
        double rating = 0.0;
        try {
            String selectQuery = "select Rating from bookRating where bookID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(bookID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                rating = resultSet.getDouble("Rating");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return rating;
    }

    public void setRatingByBookIDInBookTable(int bookID, double rating) {
        try {
            String updateQuery = "UPDATE book SET rating = ? WHERE BookID = ?";
            PreparedStatement updateStatement = this.connection.prepareStatement(updateQuery);
            updateStatement.setString(1, String.valueOf(rating));
            updateStatement.setString(2, String.valueOf(bookID));
            int rowsUpdated = updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
    }

    @Override
    public int getBookAccessByID(BookAccess access) {
        int bookAccess = -1;
        switch (access) {
            case AVAILABLE:
                bookAccess = 1;
                break;
            case STAGED:
                bookAccess = 2;
                break;
            case ANNOUNCED:
                bookAccess = 3;
                break;
        }
        return bookAccess;
    }

    @Override
    public void setBookAccessInDB(int bookID, BookAccess access) {
        int bookAccess = getBookAccessByID(access);

        try {
            String selectQuery = "UPDATE academy.book\n" +
                    "SET AccessID= ?\n" +
                    "WHERE BookID=?";
            PreparedStatement updateStatement = this.connection.prepareStatement(selectQuery);
            updateStatement.setString(1, String.valueOf(bookAccess));
            updateStatement.setString(2, String.valueOf(bookID));
            updateStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

    }

    @Override
    public int getGenreID(String genre) {
        int genreID = -1;

        try {
            String selectQuery = "select g.genreID from genre g \n" +
                    "where g.genreName like ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, genre);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                genreID = resultSet.getInt("genreID");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return genreID;
    }

    @Override
    public int getLanguageID(String language) {
        int languageID = -1;

        try {
            String selectQuery = "select l.languageID  from language l \n" +
                    "where l.languageName like ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, language);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                languageID = resultSet.getInt("languageID");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return languageID;
    }


    /**
     * @param libraryID
     * @param bookID
     * @return book count of this bookId appearing in all libraries EXCEPT the given one
     */
    @Override
    public int getBookCountFromAllLibraries(int libraryID, int bookID) {
        int count = 0;
        try {
            String selectQuery = "select count(*) as countAppearances from bookLibrary bl\n" +
                    "where bl.LibraryID != ?\n" +
                    "and bl.BookID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(libraryID));
            selectStatement.setString(2, String.valueOf(bookID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("countAppearances");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return count;
    }

    @Override
    public boolean bookExistsInLibrary(int libraryID, int bookID) {
        int count = 0;
        try {
            String selectQuery = "select count(*) as countAppearances from bookLibrary bl\n" +
                    "where bl.LibraryID = ?\n" +
                    "and bl.BookID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(libraryID));
            selectStatement.setString(2, String.valueOf(bookID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("countAppearances");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return count > 0;
    }

    @Override
    public int getBookIDbyName(String bookName) {
        int bookID = 0;
        try {
            String selectQuery = "select bookID from book b \n" +
                    "where b.Title = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setString(1, bookName);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                bookID = resultSet.getInt("bookID");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return bookID;
    }
@Override
    public void updateBookTitle(int bookID, String newTitle) {
        try {
            String updateQuery = "UPDATE book SET Title = ? WHERE BookID=?";
            PreparedStatement updateStatement = this.connection.prepareStatement(updateQuery);
            updateStatement.setString(1, newTitle);
            updateStatement.setInt(2, bookID);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());

        }
    }
@Override
    public void updateBookGenre(int bookID, String newGenre) {
        try {
            int genreID = getGenreID(newGenre);
            String updateQuery = "UPDATE book SET GenreID = ? WHERE BookID=?";
            PreparedStatement updateStatement = this.connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, genreID);
            updateStatement.setInt(2, bookID);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());

        }
    }
@Override
    public void updateBookLanguage(int bookID, String newLanguage) {
        try {
            int languageId = getLanguageID(newLanguage);
            String updateQuery = "UPDATE book SET LanguageID = ? WHERE BookID=?";
            PreparedStatement updateStatement = this.connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, languageId);
            updateStatement.setInt(2, bookID);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());

        }
    }
}
