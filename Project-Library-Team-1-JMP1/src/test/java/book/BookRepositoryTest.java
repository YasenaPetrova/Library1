package book;

import enums.BookAccess;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;


public class BookRepositoryTest{
    BookRepository bookRepository;

    @Before
    public void setUp() throws SQLException {
        bookRepository = new BookRepository();
    }

    @Test
    public void testSearchBookByNameEmpty() {
        ArrayList<Book> books = bookRepository.searchBookByName("");
        Assert.assertNotNull(books);
    }

    @Test
    public void testSearchBookByName() {
        ArrayList<Book> books = bookRepository.searchBookByName("Ulysses");
        System.out.println(books.get(0).toString());
        Assert.assertEquals("Ulysses", books.get(0).getTitle());
    }

    @Test
    public void testGetBookByAuthorEmpty() {
        ArrayList<Book> books = bookRepository.searchBookByAuthor("");
        Assert.assertNotNull(books);
    }

    @Test
    public void testGetBookByAuthor() {
        ArrayList<Book> books = bookRepository.searchBookByAuthor("Charles Dickens");
        for (Book book : books) {
            System.out.println(book.toString());
        }
    }

    @Test
    public void testGetAuthorById() {
        String authorName = bookRepository.getAuthor(10);

        System.out.println(authorName);
        Assert.assertEquals("Ernest Hemingway", authorName);
    }

    @Test
    public void testGetAuthorID() {
        int id = bookRepository.getAuthorId("George Orwell");

        System.out.println(id);
        Assert.assertEquals(6, id);
    }

    @Test
    public void testGetGenreById() {
        String genreName = bookRepository.getGenre(3);

        System.out.println(genreName);
        Assert.assertEquals("Children's", genreName);
    }

    @Test
    public void testGetLanguageById() {
        String languageName = bookRepository.getLanguage(4);

        System.out.println(languageName);
        Assert.assertEquals("Bulgarian", languageName);
    }

    @Test
    public void testRateBook() throws SQLException {
        bookRepository.rateBook(2, 17, 2.1);
    }

    @Test
    public void testRatingExistsInUserRating() {
        Assert.assertTrue(bookRepository.ratingExistsInUserRating(2, 17));
        Assert.assertFalse(bookRepository.ratingExistsInUserRating(5, 17));
    }

    @Test
    public void testAuthorExistsInGeneralLibrary(){
        Assert.assertTrue(bookRepository.authorExistsInGeneralLibrary("Serina Middleton"));
        Assert.assertFalse(bookRepository.authorExistsInGeneralLibrary("Christina Ablanska"));
    }

    @Test
    public void testGetBookByID() throws SQLException {
        Book book = bookRepository.getBookByID(7);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
    }

    @Test
    public void testGetAuthorId() {
        int id = bookRepository.getAuthorId("Edgar Allan Poe");
        System.out.println(id);
        Assert.assertEquals(18, id);
    }

    @Test
    public void testGetGenre() {
        String genre = bookRepository.getGenre(5);
        System.out.println(genre);
        Assert.assertEquals("Science Fiction", genre);
    }

    @Test
    public void testGetLanguage() {
        String lang = bookRepository.getLanguage(1);
        System.out.println(lang);
        Assert.assertEquals("English", lang);
    }

//    public void testAddBook() {
//    }

    @Test
    public void testTestAuthorExistsInGeneralLibrary() {
        Assert.assertTrue(bookRepository.authorExistsInGeneralLibrary("Edgar Allan Poe"));
        Assert.assertFalse(bookRepository.authorExistsInGeneralLibrary("Hrisi"));
    }

    @Test
    public void testGetBookNameByBookID() {
        String bookName = bookRepository.getBookNameByBookID(12);
        System.out.println(bookName);
        Assert.assertEquals("Jane Eyre", bookName);
    }

    @Test
    public void testGetAuthorByBookID() {
        String authorName = bookRepository.getAuthorByBookID(12);
        System.out.println(authorName);
        Assert.assertEquals("Emily Brontë", authorName);
    }

    @Test
    public void testGetISBNByBookID() {
        String ISBN = bookRepository.getISBNByBookID(12);
        System.out.println(ISBN);
        Assert.assertEquals("92438152379", ISBN);
    }

    @Test
    public void testGetGenreByBookID() {
        String genre = bookRepository.getGenreByBookID(12);
        System.out.println(genre);
        Assert.assertEquals("Novel", genre);
    }

    @Test
    public void testGetLanguageByBookID() {
        String lang = bookRepository.getLanguageByBookID(12);
        System.out.println(lang);
        Assert.assertEquals("English", lang);
    }

    @Test
    public void testGetRatingByBookID() {
        double rating = bookRepository.getRatingByBookID(12);
        System.out.println(rating);
        Assert.assertEquals(5.0, rating, 0.0003f);
    }

    @Test
    public void testGetAccessByBookID() {
        BookAccess access = bookRepository.getAccessByBookID(12);
        System.out.println(access);
        Assert.assertEquals(BookAccess.ANNOUNCED, access);
    }

    @Test
    public void testGetBookAccessByID() {
        int access = bookRepository.getBookAccessByID(BookAccess.ANNOUNCED);
        System.out.println(access);
        Assert.assertEquals(3, access);
    }

    @Test
    public void testGetBookCountFromAllLibraries() {
        int count = bookRepository.getBookCountFromAllLibraries(6, 1);
        System.out.println(count);
        Assert.assertEquals(4, count);
    }

    @Test
    public void testBookExistsInLibrary() {
        Assert.assertTrue(bookRepository.bookExistsInLibrary(6, 1));
        Assert.assertFalse(bookRepository.bookExistsInLibrary(7, 1));
    }

    @Test
    public void testGetAverageRatingFromDB() {
        Assert.assertEquals(3.8, bookRepository.getAverageRatingFromDB(1), 0.0003f);
    }

    @Test
    public void testAddRatingToUSerRatingTable() {
        bookRepository.addRatingToUserRatingTable(7, 6, 3.35);
    }

    @Test
    public void testSetRatingInBookTable() {
        bookRepository.setRatingByBookIDInBookTable(7, 2.89);
    }

}