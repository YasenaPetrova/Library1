package user.interfaces;

import book.Book;

import java.sql.SQLException;
import java.util.ArrayList;

public interface Reader {
    ArrayList<Book> searchBookByName(String bookName) throws SQLException;

    ArrayList<Book> searchBookByAuthor(String authorName);

    boolean addToLibrary(String bookName, boolean isRead, int userID);

    void sortLibraryByAuthor();

    void sortLibraryByTitle();

    void sortLibraryByGenre();

    void rateBook(int bookID, double rating);

    void showLastRead() throws SQLException;

}
