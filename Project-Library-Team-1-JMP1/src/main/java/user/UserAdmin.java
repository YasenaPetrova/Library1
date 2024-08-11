package user;

import console.menu.Menu;
import enums.Role;
import user.interfaces.Admin;
import user.repository.IUserRepository;
import user.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.String.valueOf;

public class UserAdmin extends User implements Admin {

    public UserAdmin() throws SQLException {
    }

    public UserAdmin(int userID, IUserRepository userRepository) {
        super(userID, Role.ADMIN, userRepository);
    }



    @Override
    public IUserRepository getUserRepository() {
        return super.getUserRepository();
    }

    @Override
    public boolean lockAccount(int userID) {
        boolean result = false;

        try {
            if (!super.getUserRepository().userExistsInGeneralDB(userID)) {
                return false;
            } else {
                boolean isLocked;

                String selectQuery = "select isLocked from user where userID = ?";
                PreparedStatement selectStatement = super.getUserRepository().getConnection().prepareStatement(selectQuery);
                selectStatement.setString(1, valueOf(userID));
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    isLocked = resultSet.getBoolean("isLocked");
                    if (isLocked) {
                        return true;
                    } else {
                        result = lock(userID);
                    }
                } else return false;
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return result;
    }

    @Override
    public boolean unlockAccount(int userID) {
        boolean result = false;

        try {
            if (!super.getUserRepository().userExistsInGeneralDB(userID)) {
                return false;
            } else {
                boolean isLocked;

                String selectQuery = "select isLocked from user where userID = ?";
                PreparedStatement selectStatement = super.getUserRepository().getConnection().prepareStatement(selectQuery);
                selectStatement.setString(1, valueOf(userID));
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    isLocked = resultSet.getBoolean("isLocked");
                    if (!isLocked) {
                        return true;
                    } else {
                        result = unlock(userID);
                    }
                } else return false;
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return result;
    }

    private boolean lock(int userID) {
        boolean result = false;

        try {
                String updateQuery = "update user set isLocked = 1 where userID = ?";
                PreparedStatement updateStatement = super.getUserRepository().getConnection().prepareStatement(updateQuery);
                updateStatement.setString(1, valueOf(userID));
                updateStatement.executeUpdate();
                result = true;

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return result;
    }

    private boolean unlock(int userID) {
        boolean result = false;

        try {
            String updateQuery = "update user set isLocked = 0 where userID = ?";
            PreparedStatement updateStatement = super.getUserRepository().getConnection().prepareStatement(updateQuery);
            updateStatement.setString(1, valueOf(userID));
            updateStatement.executeUpdate();
            result = true;

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return result;
    }

    @Override
    public void printUserDetails(int userID) throws SQLException {
        User user = User.getUserFromDBByUserID(userID);
        Menu.printAdminUserHeader();
        System.out.print(user.toString());
        System.out.printf(" %-10s |\n", user.isLocked());
        Menu.printAdminUserFooter();
    }
}
