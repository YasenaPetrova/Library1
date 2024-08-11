package user;

import book.BookRepository;
import book.Book;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.repository.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserReaderTest {
    UserReader user;
    BookRepository bookRepository;

    UserRepository userRepository;

    @Before
    public void initialSetUp() throws SQLException {
        userRepository = new UserRepository();
        bookRepository = new BookRepository();
        user = new UserReader(6, userRepository, bookRepository);
    }

    @Test
    public void testGetBookByName() {
        ArrayList<Book> books = user.searchBookByName("Ulysses");

        System.out.println(books.size());
        for (Book book : books) {
            System.out.println(book.toString());
        }
    }

    @Test
    public void testGetBookByAuthor() {
        ArrayList<Book> books = user.searchBookByAuthor("Arthur Conan Doyle");

        System.out.println(books.size());
        for (Book book : books) {
            System.out.println(book.toString());
        }
    }

    @Test
    public void testAddBookToLibraryTrue() {
        boolean result = user.addToLibrary("One Hundred Years of Solitude", true);
        Assert.assertTrue(result);
    }


    @Test
    public void testSortLibraryByAuthor() {
        user.sortLibraryByAuthor();
    }

    @Test
    public void testSortLibraryByGenre() {
        user.sortLibraryByGenre();
    }

    @Test
    public void testSortLibraryByTitle() {
        user.sortLibraryByTitle();
    }

    @Test
    public void testShowLastRead() {
        user.showLastRead();
    }

    @Test
    public void testRateBook() {
        user.rateBook(3, 4.5);
    }
}
