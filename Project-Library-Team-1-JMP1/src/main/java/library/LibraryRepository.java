package library;

import book.Book;
import dbConnection.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LibraryRepository implements ILibraryRepository {

    private final Connection connection;

    public LibraryRepository() throws SQLException {
        DBConnectionManager instance = DBConnectionManager.getInstance();
        this.connection = instance.getConnection();
    }

    @Override
    public void sortLibraryByAuthor() {

    }

    @Override
    public void sortLibraryByGenre() {

    }

    @Override
    public void sortLibraryByName() {

    }

    @Override
    public int getAvailableLibraryID() {
        int availableID = 0;

        try {
            String selectQuery = "select max(libraryID) from library";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                availableID = resultSet.getInt("max(libraryID)") + 1;
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return availableID;
    }

    @Override
    public int getLibraryIDFromDB(int userID) {
        int libraryID = 0;

        try {
            String selectQuery = "select LibraryID from userLibrary ul where UserID = ?";
            PreparedStatement selectStatement = this.connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, userID);
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
    public int addToLibrary(Book book, int userID, boolean isRead) {
        int rowsInserted = 0;
        int bookID = book.getBookID();
        int libraryID = getLibraryIDFromDB(userID); //TODO: change method to getLibraryIDFromDB()
        try {
            if (book.bookExistsInLibrary(libraryID)) {
                return 0;
            } else {
                String insertQuery = "INSERT into bookLibrary (BookID,LibraryID,ReadFlag,DateRead) values (?, ?, ?, ?)";
                PreparedStatement insertStatement = this.connection.prepareStatement(insertQuery);
                insertStatement.setString(1, String.valueOf(bookID));
                insertStatement.setString(2, String.valueOf(libraryID));
                insertStatement.setBoolean(3, isRead);
                insertStatement.setString(4, String.valueOf(LocalDateTime.now()));
                rowsInserted = insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return rowsInserted;
    }
}
