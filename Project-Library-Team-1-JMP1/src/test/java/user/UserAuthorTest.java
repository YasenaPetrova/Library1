package user;

import book.Book;
import book.BookRepository;
import enums.BookAccess;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.repository.UserRepository;

import java.sql.SQLException;

public class UserAuthorTest {
    UserAuthor author;
    BookRepository bookRepository;
    UserRepository userRepository;

    @Before
    public void initialSetup() throws SQLException {
        bookRepository = new BookRepository();
        userRepository = new UserRepository();
        author = new UserAuthor(17, userRepository, bookRepository);
    }

    @Test
    public void testShowMyLibrary() {
        author.showMyLibrary();
    }

    @Test
    public void testUploadBook() throws SQLException {
        Book book = new Book(0, "Test 1", author.getName(), "12383355543345", "Current Affairs and Politics", "French", 0, BookAccess.STAGED, true);
        author.uploadBook(book);
        author.showMyLibrary();
    }

    @Test
    public void testGetAuthorIDFromDB() {
        int authorID = author.getAuthorIDFromDB();
        Assert.assertEquals(14, authorID);
    }
}
