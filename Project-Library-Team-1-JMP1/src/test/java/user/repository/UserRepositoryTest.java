package user.repository;

import book.BookRepository;
import library.Library;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.UserReader;

import java.sql.SQLException;

public class UserRepositoryTest {
    UserReader user;
    BookRepository bookRepository;
    UserRepository userRepository;

    @Before
    public void initialSetUp() throws SQLException {
        userRepository = new UserRepository();
        bookRepository = new BookRepository();
        user = new UserReader(3, userRepository, bookRepository);

    }

    @Test
    public void testUserExistsInGeneralDBTrue() {
        Assert.assertTrue(userRepository.userExistsInGeneralDB(3));
    }

    @Test
    public void testUserExistsInGeneralDBFalse() {
        Assert.assertFalse(userRepository.userExistsInGeneralDB(300));
    }

    @Test
    public void testGetUserLibraryID() {
        int libraryID = userRepository.getUserLibraryID(3);
        System.out.println(libraryID);
        Assert.assertEquals(1, libraryID);
    }

    @Test
    public void testEncryptPassword() {
        String pass = UserRepository.encryptPassword("luvBooks()12");
        Assert.assertEquals("935bc67f194f917accd27ed14d1c18af", pass);
    }

    @Test
    public void testGetUserLibrary() {
        Library library = userRepository.getUserLibrary(6);
        Assert.assertNotNull(library);
    }
}








