package user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.repository.IUserRepository;
import user.repository.UserRepository;

import java.sql.SQLException;

public class UserAdminTest {
    UserAdmin userAdmin;
    IUserRepository userRepository;

    @Before
    public void userSetUp() throws SQLException {
        userRepository = new UserRepository();
        userAdmin = new UserAdmin(1, userRepository);
    }

    @Test
    public void testTestLockAccount() {
        Assert.assertTrue(userAdmin.lockAccount(21));
    }

    @Test
    public void testUnlockAccount() {
        Assert.assertTrue(userAdmin.unlockAccount(21));
    }

    @Test
    public void testPrintUserDetails() throws SQLException {
        userAdmin.printUserDetails(2);
    }
}
