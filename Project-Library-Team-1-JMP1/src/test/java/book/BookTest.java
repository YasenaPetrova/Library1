package book;

import enums.BookAccess;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class BookTest {
    Book book1, book2, book3;
    IBookRepository bookRepository;

    @Before
    public void initialSetup() throws SQLException {
        // int bookID, String title, String author, String ISBN, String genre, String language, double rating, BookAccess access, boolean isRead
        book1 = new Book(3, "The Great Gatsby", "Albert Camus", "57462559716", "Literary Fiction", "English", 0.0, BookAccess.AVAILABLE, true);
        book2 = new Book(29, true);
        book3 = new Book("The Great Gatsby", true);
        bookRepository = new BookRepository();
    }

    @Test
    public void testTestToString() {
        System.out.println(book1.toString());
        System.out.println(book2.toString());
        System.out.println(book3.toString());
    }

    @Test
    public void testToStringAuthor() {
        System.out.println(book1.toStringAuthor());
        System.out.println(book2.toStringAuthor());
        System.out.println(book3.toStringAuthor());
    }

    @Test
    public void testGetBookAccessID() {
        int access = book1.getBookAccessID();
        Assert.assertEquals(1, access);
    }

    @Test
    public void testGetGenreID() {
        int genreID = book1.getGenreID();
        Assert.assertEquals(4, genreID);
    }

    @Test
    public void testGetLanguageID() {
        int langID = book1.getLanguageID();
        Assert.assertEquals(1, langID);
    }

    @Test
    public void testGetAuthorId() {
        int authorID = book1.getAuthorId();
        Assert.assertEquals(3, authorID);
    }

    @Test
    public void testGetBookIDFromDB() {
        int bookID = book1.getBookIDFromDB();
        Assert.assertEquals(3, bookID);
    }



    public void testGetCountFromLibraries() {
    }
}
