package book;

import enums.BookAccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IBookRepository {
    ArrayList<Book> searchBookByName(String bookName);

    ArrayList<Book> searchBookByAuthor(String authorName);

    String getAuthor(int authorID);

    int getAuthorId(String authorName);

    String getGenre(int genreID);

    String getLanguage(int languageID);

    boolean bookExistsInGeneralLibrary(String bookName);

    boolean authorExistsInGeneralLibrary(String authorName);

    String getBookNameByBookID(int bookID);

    String getAuthorByBookID(int bookID);

    String getISBNByBookID(int bookID);

    String getGenreByBookID(int bookID);

    String getLanguageByBookID(int bookID);

    Double getRatingByBookID(int bookID);

    BookAccess getAccessByBookID(int bookID);

    void rateBook(int bookID, int userID, double newRating) throws SQLException;

    Book getBookByID(int bookID) throws SQLException;

    Connection getConnection();

    int getBookAccessByID(BookAccess access);

    int getGenreID(String genre);

    int getLanguageID(String language);

    int getBookCountFromAllLibraries(int libraryID, int bookID);

    boolean bookExistsInLibrary(int libraryID, int bookID);

    void setBookAccessInDB(int bookID, BookAccess access);

    boolean ratingExistsInUserRating(int bookID, int userID);
    int getBookIDbyName(String bookName);

    void updateBookTitle(int bookID, String newTitle);

    void updateBookGenre(int bookID, String newGenre);

    void updateBookLanguage(int bookID, String newLanguage);
}
