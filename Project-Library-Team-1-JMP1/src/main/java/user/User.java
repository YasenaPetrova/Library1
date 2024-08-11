package user;

import dbConnection.DBConnectionManager;
import enums.Role;
import library.Library;
import user.repository.IUserRepository;
import user.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private String phoneNumber;
    private Role role;
    private IUserRepository userRepository;

    public User() {
//        this.userRepository = new UserRepository();
    }

    public User(int userID, String fn, String ln, String mail, String pn, String password, Role role, IUserRepository userRepository) {
        this.userID = userID;
        this.firstName = fn;
        this.lastName = ln;
        this.email = mail;
        this.phoneNumber = pn;
        this.userName = (this.firstName != null ? this.firstName : "") + ' ' + (this.lastName != null ? this.lastName : "");
        this.password = password;
        this.role = role;
        this.userRepository = userRepository;
    }

    public User(int userID, String fn, String ln, String mail, String pn, String userName, String password, Role role, IUserRepository userRepository) {
        this.userID = userID;
        this.firstName = fn;
        this.lastName = ln;
        this.email = mail;
        this.phoneNumber = pn;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.userRepository = userRepository;
    }

    public User(User other) {
        this.userID = other.userID;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.email = other.email;
        this.phoneNumber = other.phoneNumber;
        this.role = other.role;
        this.userRepository = other.userRepository;

        this.userName = other.userName;
        this.password = other.password;
    }

    public User(int userID, Role role, IUserRepository userRepository) {
        this.userID = userID;
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.phoneNumber = "";
        this.userName = "";
        this.password = "";
        this.role = role;
        this.userRepository = userRepository;
    }

    public static User getUserFromDB(String userName, String encPassword) throws SQLException {
        int userID = getUserIDFromDB(userName);
        String firstName = "";
        String lastName = "";
        String email = "";
        String phoneNumber = "";
        Role role = getRoleFromDB(userID);

        IUserRepository userRepository = new UserRepository();

        try {
            String selectQuery = "select * from user where UserID = ?";
            PreparedStatement selectStatement = userRepository.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(userID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                return new User();
            } else {
                firstName = resultSet.getString("FirstName");
                lastName = resultSet.getString("LastName");
                email = resultSet.getString("email");
                phoneNumber = resultSet.getString("phoneNumber");
            }

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return new User(userID, firstName, lastName, email, phoneNumber, userName, encPassword, role, userRepository);

    }

    public static User getUserFromDBByUserID(int userID) throws SQLException {
        String firstName = "";
        String lastName = "";
        String email = "";
        String phoneNumber = "";
        String userName = getUserNameFromDB(userID);
        String encPassword = getPasswordFromDB(userID);
        Role role = getRoleFromDB(userID);

        IUserRepository userRepository = new UserRepository();

        try {
            String selectQuery = "select * from user where UserID = ?";
            PreparedStatement selectStatement = userRepository.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(userID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                return new User();
            } else {
                firstName = resultSet.getString("FirstName");
                lastName = resultSet.getString("LastName");
                email = resultSet.getString("email");
                phoneNumber = resultSet.getString("phoneNumber");
            }

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return new User(userID, firstName, lastName, email, phoneNumber, userName, encPassword, role, userRepository);

    }

    public static String getUserNameFromDB(int userID) {
        String userName = "";
        try {
            IUserRepository userRepository = new UserRepository();
            String selectQuery = "select userName from credentials c \n" +
                    "where userID = ?";
            PreparedStatement selectStatement = userRepository.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(userID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                userName = resultSet.getString("userName");
            }

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return userName;
    }

    public static String getPasswordFromDB(int userID) {
        String encPassword = "";
        try {
            IUserRepository userRepository = new UserRepository();
            String selectQuery = "select password from credentials c \n" +
                    "where userID = ?";
            PreparedStatement selectStatement = userRepository.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(userID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                encPassword = resultSet.getString("password");
            }

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return encPassword;
    }

    public static Role getRoleFromDB(int userID) throws SQLException {
        int roleID = 0;
        IUserRepository userRepository = new UserRepository();

        try {
            String selectQuery = "select userRoleID from userRole ur where UserID = ?";
            PreparedStatement selectStatement = userRepository.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, String.valueOf(userID));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                roleID = resultSet.getInt("userRoleID");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return getRole(roleID);
    }


    public static Role getRole(int roleID) {
        Role role = Role.READER;

        switch (roleID) {
            case 1:
                break;
            case 2:
                role = Role.AUTHOR;
                break;
            case 3:
                role = Role.ADMIN;
                break;
        }

        return role;
    }

    public static int getUserIDFromDB(String userName) throws SQLException {
        int userID = -1;
        IUserRepository userRepository = new UserRepository();

        try {
            String selectQuery = "SELECT userID from credentials c where username = ?";
            PreparedStatement selectStatement = userRepository.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, userName);
            ResultSet resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                return -1;
            } else {
                userID = resultSet.getInt("userID");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return userID;
    }

    public int getUserID() {
        return userID;
    }

    public IUserRepository getUserRepository() {
        return userRepository;
    }

    public static boolean checkCredentials(String userName, String pass) {
        DBConnectionManager connection = DBConnectionManager.getInstance();
        String encryptedPassword = UserRepository.encryptPassword(pass);
        String dbPassword = null;
        boolean result = false;

        try {
            String selectQuery = "select password from credentials c where username = ? ";
            PreparedStatement selectStatement = connection.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, userName);
            ResultSet resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                return false;
            } else {
                dbPassword = resultSet.getString("password");
            }

            result = encryptedPassword.equals(dbPassword);

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return result;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("| ")
                .append(String.format("%-10d | %-40s | %-50s | %-40s | %-40s | %-50s | %-10s |"
                        , userID, firstName.concat(" " + lastName), email, phoneNumber, userName, password, role));

        return result.toString();
    }

    public Role getRole() {
        return this.role;
    }

    protected Library getUserLibrary() throws SQLException {
        return userRepository.getUserLibrary(userID);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isLocked() {
        return userRepository.isLocked(userID);
    }

    // Create Account

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }


//    private static boolean isValidAddress(String street, String city, String state, String country, short postalCode) {
//        return !(street.isEmpty() || city.isEmpty() || state.isEmpty() || country.isEmpty() || postalCode <= 0);
//    }
//
//    private static Address initializeAddress(Scanner scanner) {
//        System.out.print("-> Street: ");
//        String street = scanner.nextLine();
//        System.out.print("-> City: ");
//        String city = scanner.nextLine();
//        System.out.print("-> State: ");
//        String state = scanner.nextLine();
//        System.out.print("-> Country: ");
//        String country = scanner.nextLine();
//        System.out.print("-> Postal Code: ");
//        short postalCode = Short.parseShort(scanner.nextLine());
//
//        while (!isValidAddress(street, city, state, country, postalCode)) {
//            System.out.println("Invalid address, please enter again:");
//            street = scanner.nextLine();
//            city = scanner.nextLine();
//            state = scanner.nextLine();
//            country = scanner.nextLine();
//            postalCode = Short.parseShort(scanner.nextLine());
//        }
//
//        return new Address(street, city, state, country, postalCode);
//    }



    public void setUserID(int userID) {
        this.userID = userID;
    }





    public boolean userExistsInGeneralDB() {
        int userCount = 0;
        try {
            String selectQuery = "SELECT count(*) FROM user WHERE UserID = ?";
            PreparedStatement selectStatement = userRepository.getConnection().prepareStatement(selectQuery);
            selectStatement.setInt(1, userID);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                userCount = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return userCount == 1;
    }

    public void insertUser() {
        if (userExistsInGeneralDB()) {
            return;
        }
        try {
            String insertQuery = "INSERT INTO user (FirstName, LastName, Email, PhoneNumber, isLocked) VALUES ( ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = userRepository.getConnection().prepareStatement(insertQuery);
            insertStatement.setString(1, getFirstName());
            insertStatement.setString(2, getLastName());
            insertStatement.setString(3, getEmail());
            insertStatement.setString(4, getPhoneNumber());
            insertStatement.setString(5, "0");
            insertStatement.executeUpdate();
            setUserID(getUserIDFromDB(this));

//            String insertQueryRole = "INSERT INTO academy.userRole (UserID, UserRoleID) VALUES(?, ?)";
//            PreparedStatement insertStatementRole = userRepository.getConnection().prepareStatement(insertQueryRole);
//            insertStatementRole.setString(1, String.valueOf(getUserID()));
//            insertStatementRole.setString(2, String.valueOf(getUserRole(getRole())));
//            insertStatementRole.executeUpdate();

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
    }

    public void insertCredentials() throws SQLException {
        String insertQuery = "INSERT INTO credentials (UserID, Username, Password) VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = userRepository.getConnection().prepareStatement(insertQuery)) {
            insertStatement.setInt(1, getUserIDFromDB(this));
            insertStatement.setString(2, getFirstName() + getLastName());
            insertStatement.setString(3, UserRepository.encryptPassword(getPassword()));
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
            throw e;
        }
    }

    public void insertIntoLibrary(String libraryName) throws SQLException {
        String insertLibrary = "INSERT INTO library (LibraryName) VALUES (?)";
        try {
            PreparedStatement insertStatement = userRepository.getConnection().prepareStatement(insertLibrary);
            insertStatement.setString(1, libraryName);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
            throw e;
        }

    }

    public void insertUserLibrary(int userID, int libraryID) throws SQLException {
        String insertQuery = "INSERT INTO academy.userLibrary (LibraryID, UserID) VALUES (?, ?)";
        try (PreparedStatement insertStatement = userRepository.getConnection().prepareStatement(insertQuery)) {
            insertStatement.setInt(1, libraryID);
            insertStatement.setInt(2, userID);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
            throw e;
        }
    }

    public static int getUserIDFromDB(User user) {
        int ID = 0;
        try {
            String selectQuery = "select userID from user u where u.FirstName = ? and u.LastName = ? and u.email = ?";
            PreparedStatement selectStatement = user.userRepository.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, user.getFirstName());
            selectStatement.setString(2, user.getLastName());
            selectStatement.setString(3, user.getEmail());

            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                ID = resultSet.getInt("userID");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return ID;

    }

    public int getLibraryIDFromDB(String LibraryName) {
        int ID = 0;
        try {
            String selectQuery = "select LibraryID from library l where l.LibraryName = ?";
            PreparedStatement selectStatement = userRepository.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, LibraryName);

            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                ID = resultSet.getInt("LibraryID");
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return ID;
    }

    private int getUserRole(Role role) {
        switch (role) {
            case READER:
                return 1;
            case AUTHOR:
                return 2;
            case ADMIN:
                return 3;
        }
        return 0;
    }
    //трябва да попълня 2.Library (Name) , 1.Credentials (UserID,Username, password - UserRepository.encryptPassword(user.getPassword()), UserLibrary (INSERT INTO academy.userLibrary
    //(LibraryID, UserID)
    //VALUES(?, ?,);)

    public void printUserData() {
        System.out.println("User Information:");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("User Name: " + userName);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Role: " + role);
    }

    public void insertIntoAuthor() {
        int userID = getUserIDFromDB(this);
        try {
            String insertQuery = "INSERT INTO academy.author\n" +
                    "(UserID)\n" +
                    "VALUES(?)";
            PreparedStatement insertStatement = userRepository.getConnection().prepareStatement(insertQuery);
            insertStatement.setInt(1, userID);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
    }

    public void insertIntoUserRole() {
        int userID = getUserIDFromDB(this);
        int role = getUserRole(this.getRole());
        try {
            String insertQuery = "INSERT INTO academy.userRole\n" +
                    "(UserID, UserRoleID)\n" +
                    "VALUES(?, ?)";
            PreparedStatement insertStatement = userRepository.getConnection().prepareStatement(insertQuery);
            insertStatement.setInt(1, userID);
            insertStatement.setInt(2, role);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
    }


}


