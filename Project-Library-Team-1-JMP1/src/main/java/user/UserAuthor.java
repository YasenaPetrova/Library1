package user;

import book.Book;
import book.IBookRepository;
import console.menu.Menu;
import enums.Role;
import library.Library;
import user.interfaces.Author;
import user.repository.IUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserAuthor extends User implements Author {
    private final IBookRepository bookRepository;
    private Library library;


    public UserAuthor(int userID, IUserRepository userRepository, IBookRepository bookRepository) throws SQLException {
        super(userID, Role.READER, userRepository);
//        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.library = userRepository.getUserLibrary(userID);
    }


    @Override
    public void uploadBook(Book book) {
        try {
            int authorID = getAuthorIDFromDB();
            int genreID = book.getGenreID();
            int languageID = book.getLanguageID();
            int accessID = book.getBookAccessID();

            String insertQueryBook = "INSERT INTO academy.book\n" +
                    "(Title, ISBN, AuthorID, GenreID, rating, AccessID, LanguageID)\n" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatementBook = book.getBookRepository().getConnection().prepareStatement(insertQueryBook);
            insertStatementBook.setString(1, book.getTitle());
            insertStatementBook.setString(2, book.getISBN());
            insertStatementBook.setString(3, String.valueOf(authorID));
            insertStatementBook.setString(4, String.valueOf(genreID));
            insertStatementBook.setString(5, "0.0");
            insertStatementBook.setString(6, String.valueOf(accessID));
            insertStatementBook.setString(7, String.valueOf(languageID));
            insertStatementBook.executeUpdate();

            int bookID = book.getBookIDFromDB();

            String insertQueryBookLibrary = "INSERT INTO academy.bookLibrary\n" +
                    "(LibraryID, BookID, ReadFlag, DateRead)\n" +
                    "VALUES(?, ?, ?, ?)";
            PreparedStatement insertStatementLibrary = book.getBookRepository().getConnection().prepareStatement(insertQueryBookLibrary);
            insertStatementLibrary.setString(1, String.valueOf(this.library.getLibraryID()));
            insertStatementLibrary.setString(2, String.valueOf(bookID));
            insertStatementLibrary.setString(3, "1");
            insertStatementLibrary.setString(4, String.valueOf(LocalDateTime.now()));
            insertStatementLibrary.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

    }

    @Override
    public void showMyLibrary() {
        Menu.printAuthorLibraryHeader();

        int userID = super.getUserID();
        int libraryID = library.getLibraryID();
        ArrayList<Book> books = super.getUserRepository().getUserLibraryListSortedBy(userID, "b.Title");

        for (Book book : books) {
            System.out.print(book.toStringAuthor());
            System.out.printf(" %10d |\n", book.getCountFromLibraries(libraryID));
        }

        Menu.printAuthorLibraryFooter();
    }

    public String getName() {
        return super.getFirstName() + " " + super.getLastName();
    }

    int getAuthorIDFromDB() {
        int authorID = 0;
        int userID = super.getUserID();
        try {
            String selectQuery = "select authorID from author where UserID = ?";
            PreparedStatement selectStatement = bookRepository.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(userID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                authorID = resultSet.getInt("authorID");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return authorID;
    }

}
