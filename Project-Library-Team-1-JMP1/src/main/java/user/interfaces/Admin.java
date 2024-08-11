package user.interfaces;

import java.sql.SQLException;

public interface Admin {
    boolean lockAccount(int userID) throws SQLException;
    boolean unlockAccount(int userID) throws SQLException;
    public void printUserDetails(int userID) throws SQLException;
}
