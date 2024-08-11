package console.menu;

import enums.Role;
import user.User;
import user.repository.IUserRepository;
import user.repository.UserRepository;

import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    public void logInMenu() {
        System.out.println("    )                                    ");
        System.out.println(" ( /(                               )    ");
        System.out.println(" )\\())     (  (  (  (      ) (   ( /(    ");
        System.out.println("((_)\\  (   )\\))( )\\))(  ( /( )(  )\\()|   ");
        System.out.println(" _((_) )\\ ((_))\\((_)()\\ )(_)|()\\(_))/)\\  ");
        System.out.println("| || |((_) (()(_)(()((_|(_)_ ((_) |_((_) ");
        System.out.println("| __ / _ \\/ _` |\\ V  V / _` | '_|  _(_-< ");
        System.out.println("|_||_\\___/\\__, | \\_/\\_/\\__,_|_|  \\__/__/ ");
        System.out.println("          |___/                          ");
        System.out.println("|                                       |");
        System.out.println("|          L  I  B  R  A  R  Y          |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
        System.out.println("|                 Log in                |");
        System.out.println("|             Create Account            |");
        System.out.println("|                                       |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
    }


//    public User readCredentials() throws SQLException {
//        Scanner scan = new Scanner(System.in);
//        printLogInHeader();
//        System.out.println();
//
//        System.out.printf("  User name: ");
//        String userName = scan.nextLine();
//
//        String password = PasswordField.readPassword("  Password:");;
//        System.out.println("The password entered is: "+password);
//
////        String userPass = convertCharArrayToString(password);
//        printLogInFooter();

//        if (User.checkCredentials(userName, userPass)){
//            System.out.println("Success");
//        } else {
//            System.out.println("Log in failed");
//        }
//        while (!User.checkCredentials(userName, pass)) {
//            printLogInHeader();
//            System.out.println();
//            userName = console.readLine("  User name: ");
//            pass = console.readPassword("  Password: ");
//            printLogInFooter();
//        }

//        return new User();
//    }


    // Using class Console and then Terminal
//    public User readCredentials() throws SQLException {
//        Console console = System.console();
//        printLogInHeader();
//        System.out.println();
//        String userName = console.readLine("  User name: ");
//        char[] pass = console.readPassword("  Password: ");
//        String userPass = convertCharArrayToString(pass);
//        printLogInFooter();
//
//        if (User.checkCredentials(userName, userPass)){
//            System.out.println("Success");
//        } else {
//            System.out.println("Log in failed");
//        }
////        while (!User.checkCredentials(userName, pass)) {
////            printLogInHeader();
////            System.out.println();
////            userName = console.readLine("  User name: ");
////            pass = console.readPassword("  Password: ");
////            printLogInFooter();
////        }
//
//        return new User();
//    }


    public User readCredentials() throws SQLException {
        Scanner scan = new Scanner(System.in);

        printLogInHeader();
        System.out.println();
        System.out.print("  UserName: ");
        String userName = scan.nextLine();
        System.out.print("  Password: ");
        String pass = scan.nextLine();
        System.out.println();
//        String userPass = convertCharArrayToString(pass);
        printLogInFooter();

        while (!User.checkCredentials(userName, pass)) {
            printLogInHeader();
            System.out.println();
            System.out.println("  Invalid userName or password!\n  Please try again!");
            System.out.print("  UserName: ");
            userName = scan.nextLine();
            System.out.print("  Password: ");
            pass = scan.nextLine();
            System.out.println();
        }

        return User.getUserFromDB(userName, UserRepository.encryptPassword(pass));
    }

    public static User createAccount(Scanner scanner) throws SQLException {
        User user = new User();
        System.out.println("Please, insert the following information:");
        String firstName = initializeName(scanner, "First");
        String lastName = initializeName(scanner, "Last");
        System.out.print("-> Email: ");
        String email = scanner.nextLine();
        System.out.print("-> Phone Number: ");
        String phoneNumber = scanner.nextLine();
        String password = initializePassword(scanner);

        System.out.println("Select the role:");
        System.out.println("1. Reader");
        System.out.println("2. Author");
        System.out.print("Your choice: ");
        int choice = getChoice(scanner);

        Role role;
        switch (choice) {
            case 1:
                role = Role.READER;
                break;
            case 2:
                role = Role.AUTHOR;
                break;
            default:
                System.out.println("Invalid choice. Setting role to Reader.");
                role = Role.READER;
                break;
        }

        IUserRepository userRepository = new UserRepository();
        user = new User(0, firstName, lastName, email, phoneNumber, password, role, userRepository);
        int userID = User.getUserIDFromDB(user);
        if (!user.getUserRepository().userExistsInGeneralDB(userID)) {
            user.insertUser();
            user.insertCredentials();
            user.insertIntoUserRole();
            if (user.getRole() == Role.AUTHOR || user.getRole() == Role.READER) {
                user.insertIntoLibrary(user.getFirstName() + user.getLastName());
                user.insertUserLibrary(User.getUserIDFromDB(user), user.getLibraryIDFromDB(user.getFirstName() + user.getLastName()));
            }
            if (user.getRole() == Role.AUTHOR) {
                user.insertIntoAuthor();
            }
        }
        userID = User.getUserIDFromDB(user);
        user.setUserID(userID);
        return user;
    }

    private static int getChoice(Scanner scan) {
        String choice;
        do {
            choice = scan.nextLine();
            if (choice.matches("\\d")) {
                break;
            } else {
                System.out.println("  Invalid Input!");
            }
        } while (!(scan.hasNextInt()) || !choice.matches("\\d"));
        return Integer.parseInt(choice);
    }

    private static String initializePassword(Scanner scanner) {
        System.out.print("-> Password: ");
        String password = scanner.next();
        scanner.nextLine();
        while (!isValidPassword(password)) {
            System.out.print("Invalid password, please enter again: ");
            password = scanner.next();
            scanner.nextLine();
        }
        return password;
    }

    private static boolean isValidPassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$");
    }

    private static String initializeName(Scanner scanner, String str) {
        System.out.print("-> Enter " + str + " Name: ");
        String name = scanner.nextLine();
        while (!isValidName(name)) {
            System.out.print("Invalid name, please enter again: ");
            name = scanner.nextLine();
        }
        return capitalLetter(name);
    }

    private static String capitalLetter(String str) {
        if (str == null || str.isEmpty()) {
            System.out.println(" ------ error [empty string] ------\n");
            return str;
        }

        char firstChar = Character.toUpperCase(str.charAt(0));
        StringBuilder result = new StringBuilder().append(firstChar);
        boolean makeNextCapital = false;
        for (int i = 1; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            if (Character.isLetter(currentChar)) {
                if (makeNextCapital) {
                    result.append(Character.toUpperCase(currentChar));
                    makeNextCapital = false;
                } else {
                    result.append(Character.toLowerCase(currentChar));
                }
            } else if (currentChar == ' ') {
                makeNextCapital = true;
            }
        }
        return result.toString();
    }

    private static boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.matches("[A-Za-z]+");
    }


    private String convertCharArrayToString(char[] pass) {
        return new String(pass);
    }

    public void printLogInHeader() {
        System.out.println("|                                       |");
        System.out.println("|               L O G I N               |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
    }


    public void printLogInFooter() {
        System.out.println("|                                       |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
    }

    public void printReaderMenu() {
        System.out.println("|                                       |");
        System.out.println("|              R E A D E R              |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
        System.out.println("| 1. Search book                        |");
        System.out.println("| 2. Add book to library                |");
        System.out.println("| 3. Show last read                     |");
        System.out.println("| 4. Sort library                       |");
        System.out.println("| 5. Rate book                          |");
        System.out.println("| 6. Exit                               |");
        System.out.println("|                                       |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
    }

    public void printAuthorMenu() {
        System.out.println("|                                       |");
        System.out.println("|              A U T H O R              |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
        System.out.println("| 1. Upload book                        |");
        System.out.println("| 2. Show library                       |");
        System.out.println("| 3. Update book access                 |");
        System.out.println("| 4. Modify book details                |");
        System.out.println("| 5. Exit                               |");
        System.out.println("|                                       |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
    }

    public void printAdminMenu() {
        System.out.println("|                                       |");
        System.out.println("|               A D M I N               |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
        System.out.println("| 1. Lock account                       |");
        System.out.println("| 2. Unlock account                     |");
        System.out.println("| 3. User details                       |");
        System.out.println("| 4. Exit                               |");
        System.out.println("|                                       |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
    }

    public void printReaderSearchBookMenu() {
        System.out.println("|                                       |");
        System.out.println("|            SEARCH BOOK BY             |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
        System.out.println("| 1. Name                               |");
        System.out.println("| 2. Author                             |");
        System.out.println("| 3. Exit                               |");
        System.out.println("|                                       |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
    }

    public void printReaderSortLibraryMenu() {
        System.out.println("|                                       |");
        System.out.println("|            SORT LIBRARY BY            |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
        System.out.println("| 1. Author                             |");
        System.out.println("| 2. Title                              |");
        System.out.println("| 3. Genre                              |");
        System.out.println("| 4. Exit                               |");
        System.out.println("|                                       |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
    }

    public void printAddBookDetailsHeader() {
        System.out.println("|                                       |");
        System.out.println("|            ADD BOOK DETAILS           |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
    }

    public void printAccessOptions() {
        System.out.println("|                                       |");
        System.out.println("|             ACCESS OPTIONS            |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
        System.out.println("| Available                             |");
        System.out.println("| Staged                                |");
        System.out.println("| Announced                             |");
        System.out.println("|                                       |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
    }


    public void printAddBookDetailsFooter() {
        System.out.println("|                                       |");
        System.out.println("*****************************************");
        System.out.println("|                                       |");
    }

    public static void printAuthorLibraryHeader() {
        System.out.printf("| %-50s | %-40s | %-30s | %-20s | %10s | %10s | %10s |\n", "TITLE", "AUTHOR", "GENRE", "LANGUAGE", "RATING", "ACCESS", "#LISTING");
        System.out.println("************************************************************************************************************************************************************************************************");
    }

    public static void printAuthorLibraryFooter() {
        System.out.println("************************************************************************************************************************************************************************************************");
        System.out.println("|                                                                                                                                                                                              |");
    }

    public static void printReaderLibraryHeader() {
        System.out.printf("| %-50s | %-40s | %-30s | %-20s | %10s |\n", "TITLE", "AUTHOR", "GENRE", "LANGUAGE", "RATING");
        System.out.println("**********************************************************************************************************************************************************************");
    }

    public static void printReaderLibraryFooter() {
        System.out.println("**********************************************************************************************************************************************************************");
        System.out.println("|                                                                                                                                                                    |");
    }

    public static void printReaderLastRead() {
        System.out.println("|                                                                         LAST READ                                                                                  |");
        System.out.println("**********************************************************************************************************************************************************************");
        System.out.println("|                                                                                                                                                                    |");
    }

    public static void printSortedBy(String sortedBy) {
        System.out.print("|                                                                  SORTED BY ");
        System.out.print(sortedBy + " ".repeat(88 - sortedBy.length()) + "|\n");
        System.out.println("**********************************************************************************************************************************************************************");
        System.out.println("|                                                                                                                                                                    |");
    }

    public static void printAdminUserHeader() {
        System.out.printf("| %-10s | %-40s | %-50s | %-40s | %-40s | %-50s | %-10s | %-10s |\n", "User ID", "Name", "Email", "Phone number", "userName", "Password", "Role", "isLocked");
        System.out.println("*".repeat(275));
    }

    public static void printAdminUserFooter() {
        System.out.println("*".repeat(275));
        System.out.println("|" + " ".repeat(273) + "|");
    }
}
