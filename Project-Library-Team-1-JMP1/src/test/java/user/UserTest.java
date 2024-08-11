package user;

import book.IBookRepository;
import enums.Role;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.repository.IUserRepository;
import user.repository.UserRepository;

import java.sql.SQLException;

public class UserTest {
    private User user;

    @Before
    public void userSetup() throws SQLException {
        IUserRepository userRepository = new UserRepository();
        user = new User(0,"Martin", "Doychev","email@gmail.com","+123467890", "neznam", Role.READER, userRepository);
    }
    @Test
    public void testCheckCredentials() {
        String pass = "bookLover1)";
        Assert.assertTrue(User.checkCredentials("aMckinney", pass));
    }

    @Test
    public void testGetUserID() throws SQLException {
        Assert.assertEquals(11, User.getUserIDFromDB("DanAli44"));
    }

    @Test
    public void testUserToStringAndGetUserFromDB() throws SQLException {
        User user = User.getUserFromDB("DinBuz34", "5572fbceb2107621794337fc09f12de0");
        System.out.println(user.toString());
    }

    @Test
    public void testGetRoleFromDB() throws SQLException {
        Role role = User.getRoleFromDB(17);
        Assert.assertEquals(Role.AUTHOR, role);
    }

    @Test
    public void testGetRole() {
        Role role = User.getRole(3);
        Assert.assertEquals(Role.ADMIN, role);
    }

    @Test
    public void testGetUserIDFromDB() throws SQLException {
        int id = User.getUserIDFromDB("cNieves");
        Assert.assertEquals(1, id);
    }

    @Test
    public void testGetUserFromDB() throws SQLException {
        User user = User.getUserFromDB("ArtCon37", "887e2a1473435a436ea407e4db58834c");
        System.out.println(user.toString());
    }

    // Create Account

    @Test
    public void testGetUserIDHrisi() throws SQLException {
        User user = User.getUserFromDB("ArtCon37", "887e2a1473435a436ea407e4db58834c");
        Assert.assertEquals(17, User.getUserIDFromDB(user));
    }
    @Test
    public void testInsertUser() {
        user.insertUser();
    }
    @Test
    public void testInsertIntoLibrary() throws SQLException {
        user.insertIntoLibrary("MartyTest");
    }
    @Test
    public void testInsertUserLibrary() throws SQLException{
        user.insertUserLibrary(37,37);
    }
    @Test
    public void testInsertCredentials () throws SQLException{
        user.insertCredentials();
    }

    @Test
    public void testInsertIntoAuthor() {
        user.insertIntoAuthor();
    }

    @Test
    public void testInsertIntoUserRole() {
        user.insertIntoUserRole();
    }

}
