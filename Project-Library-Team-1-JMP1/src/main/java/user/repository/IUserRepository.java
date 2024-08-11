package user.repository;

import book.Book;
import library.Library;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IUserRepository {
    boolean userExistsInGeneralDB(int userID);
    int getUserLibraryID(int userID);

    Library getUserLibrary(int userID) throws SQLException;
    ArrayList<Book> getUserLibraryListSortedBy(int userID, String sortBy);

    static String encryptPassword(String string) {
        return null;
    }

    Connection getConnection();

    boolean isLocked(int userID);
}
