package book;

import enums.BookAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Book {
    private int bookID;
    private String title;
    private String author;
    private String ISBN;
    private String genre;
    private String language;
    private double rating;
    private BookAccess access;
    private boolean isRead;

    private final IBookRepository bookRepository;

    public IBookRepository getBookRepository() {
        return bookRepository;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getLanguage() {
        return language;
    }

    public Book(String bookName, boolean isRead) throws SQLException {
        this.bookRepository = new BookRepository();
        this.bookID = bookRepository.getBookIDbyName(bookName);
        this.title = bookName;
        this.author = bookRepository.getAuthorByBookID(this.bookID);
        this.ISBN = bookRepository.getISBNByBookID(this.bookID);
        this.genre = bookRepository.getGenreByBookID(this.bookID);
        this.language = bookRepository.getLanguageByBookID(this.bookID);
        this.rating = bookRepository.getRatingByBookID(this.bookID);
        this.access = BookAccess.ANNOUNCED;
        this.isRead = isRead;
    }
    public Book() throws SQLException {
        this.bookID = 0;
        this.title = "";
        this.author = "";
        this.ISBN = "";
        this.genre = "";
        this.language = "";
        this.rating = 0.0;
        this.access = BookAccess.ANNOUNCED;
        this.isRead = false;
        this.bookRepository = new BookRepository();
    }

    public Book(int bookID, String title, String author, String ISBN, String genre, String language, double rating, BookAccess access, boolean isRead) throws SQLException {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.genre = genre;
        this.language = language;
        this.rating = rating;
        this.access = access;
        this.isRead = isRead;
        this.bookRepository = new BookRepository();
    }

    public Book (int bookID, boolean isRead) throws SQLException {
        this.bookID = bookID;
        this.bookRepository = new BookRepository();
        this.title = bookRepository.getBookNameByBookID(bookID);
        this.author = bookRepository.getAuthorByBookID(bookID);
        this.ISBN = bookRepository.getISBNByBookID(bookID);
        this.genre = bookRepository.getGenreByBookID(bookID);
        this.language = bookRepository.getLanguageByBookID(bookID);
        this.rating = bookRepository.getRatingByBookID(bookID);
        this.access = bookRepository.getAccessByBookID(bookID);
        this.isRead = isRead;
    }

    public String getGenre() {
        return genre;
    }

    public String getAuthor() {
        return author;
    }

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String isBookRead = isRead ? "Read" : "Not read";
        result.append("| ")
              .append(String.format("%-10s | %-50s | %-40s | %-30s | %-20s | %5.02f | %-15s |", bookID, title, author, genre, language, rating, isBookRead));

        return result.toString();
    }

    public String toStringAuthor() {
        String access = convertAccess(this.access);
        StringBuilder result = new StringBuilder();
        result.append("| ")
                .append(String.format("%-50s | %-40s | %-30s | %-20s | %10.02f | %-10s |", title, author, genre, language, rating, access));

        return result.toString();
    }

    public String toStringReader() {
        StringBuilder result = new StringBuilder();
        result.append("| ")
                .append(String.format("%-50s | %-40s | %-30s | %-20s | %10.02f |", title, author, genre, language, rating));

        return result.toString();
    }

    private String convertAccess(BookAccess access) {
        String result = "";
        switch (access) {
            case AVAILABLE:
                result = "AVAILABLE";
                break;
            case STAGED:
                result = "STAGED";
                break;
            case ANNOUNCED:
                result = "ANNOUNCED";
                break;
        }
        return result;
    }

    public String getTitle() {
        return title;
    }

    public BookAccess getAccess() {
        return access;
    }

    public int getBookID() {
        return bookID;
    }

    public int getBookAccessID() {
        return bookRepository.getBookAccessByID(this.access);
    }

    public int getGenreID() {
        return bookRepository.getGenreID(this.genre);
    }

    public int getLanguageID() {
        return bookRepository.getLanguageID(this.language);
    }

    public int getAuthorId() {
        return bookRepository.getAuthorId(this.author);
    }

    public int getBookIDFromDB() {
        int bookID = -1;

        try {
            String selectQuery = "select b.BookID  from book b\n" +
                    "where b.Title = ?\n" +
                    "and b.ISBN = ?";
            PreparedStatement selectStatement = bookRepository.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, title);
            selectStatement.setString(2, ISBN);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                bookID = resultSet.getInt("BookID");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return bookID;
    }

    public int getCountFromLibraries(int libraryID) {
        return bookRepository.getBookCountFromAllLibraries(libraryID, bookID);
    }

    public boolean bookExistsInLibrary(int libraryID) {
        return bookRepository.bookExistsInLibrary(libraryID, this.bookID);
    }

    public void setAccessINDB(BookAccess access) {
        this.access = access;
        this.bookRepository.setBookAccessInDB(this.bookID, access);
    }
}