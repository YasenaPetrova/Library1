package console;

import book.Book;
import book.BookRepository;
import book.IBookRepository;
import console.menu.Menu;
import enums.BookAccess;
import enums.Role;
import user.User;
import user.UserAdmin;
import user.UserAuthor;
import user.UserReader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleInteraction {
    public User logIn(Scanner scan, Menu menu) {
        User user = new User();
        menu.logInMenu();
        String choice = scan.nextLine();
        choice = choice.toLowerCase();
        while (!choice.contains("log in") && !choice.contains("create account")) {
            System.out.println("Invalid input! Please try again!");
            choice = scan.nextLine();
            choice = choice.toLowerCase();
        }
        if (choice.equals("log in")) {
            try {
                user = menu.readCredentials();
            } catch (SQLException e) {
                System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
            }
        } else {
            try {
                user = Menu.createAccount(scan);
            } catch (SQLException e) {
                System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
            }
        }
        return user;
    }

    public void start(Scanner scan) throws SQLException {
        Menu menu = new Menu();
        User user = logIn(scan, menu);
        IBookRepository bookRepository = new BookRepository();

        if (user.isLocked()) {
            System.out.println("  Your account is locked!\nPlease contact Admin!");
            return;
        }

        switch (user.getRole()) {
            case READER:
                UserReader reader = new UserReader(user.getUserID(), user.getUserRepository(), bookRepository);
                interactWithReader(reader, scan, menu);
                break;
            case AUTHOR:
                UserAuthor author = new UserAuthor(user.getUserID(), user.getUserRepository(), bookRepository);
                interactWithAuthor(author, scan, menu);
                break;
            case ADMIN:
                UserAdmin admin = new UserAdmin(user.getUserID(), user.getUserRepository());
                interactWithAdmin(admin, scan, menu);
                break;
        }
    }

    private void interactWithAdmin(UserAdmin admin, Scanner scan, Menu menu) throws SQLException {
        int choice = 0;
        menu.printAdminMenu();

        choice = getChoice(scan);

        while (choice > 0 && choice <= 4) {
            switch (choice) {
                case 1:
                    System.out.println("  User ID to lock: ");
                    int userIDLock = getUserID(scan);
                    if (User.getRoleFromDB(userIDLock) == Role.ADMIN) {
                        System.out.println("  You are not allowed to temper with\n   another admin!");
                        break;
                    } else {
                        lockAccount(admin, userIDLock, scan);
                    }
                    break;
                case 2:
                    System.out.println("  User ID to unlock: ");
                    int userIDUnlock = getUserID(scan);
                    if (User.getRoleFromDB(userIDUnlock) == Role.ADMIN) {
                        System.out.println("  You are not allowed to temper with another admin!");
                        break;
                    } else {
                        unlockAccount(admin, userIDUnlock, scan);
                    }
                    break;
                case 3:
                    System.out.println("  User ID to review: ");
                    int userID = getUserID(scan);
                    admin.printUserDetails(userID);
                    break;
                case 4:
                    return;
            }
            menu.printAdminMenu();
            choice = getChoice(scan);
        }

    }

    private void lockAccount(UserAdmin admin, int userID, Scanner scan) {
        if (!admin.getUserRepository().userExistsInGeneralDB(userID)) {
            System.out.println("  This user does not exist!");
        } else {
            if (admin.lockAccount(userID)) {
                System.out.println("  The account is LOCKED!");
            } else {
                System.out.println("  This account is already locked!");
            }
        }
    }

    private void unlockAccount(UserAdmin admin, int userID, Scanner scan) {
        if (!admin.getUserRepository().userExistsInGeneralDB(userID)) {
            System.out.println("  This user does not exist!");
        } else {
            if (admin.unlockAccount(userID)) {
                System.out.println("  The account is UNLOCKED!");
            } else {
                System.out.println("  This account is already unlocked!");
            }
        }
    }

    private int getUserID(Scanner scan) {
        String userID;
        do {
            userID = scan.nextLine();
            if (userID.matches("\\d\\d")) {
                break;
            } else {
                System.out.println("  Invalid Input!");
            }
        } while (!(scan.hasNextInt()) || !userID.matches("\\d\\d"));
        return Integer.parseInt(userID);
    }

    private void interactWithAuthor(UserAuthor author, Scanner scan, Menu menu) throws SQLException {
        int choice = 0;
        menu.printAuthorMenu();

        choice = getChoice(scan);

        while (choice > 0 && choice <= 4) {
            switch (choice) {
                case 1:
                    Book book = getBookDetails(scan, menu);
                    author.uploadBook(book);
                    System.out.println("  Book uploaded!");
                    break;
                case 2:
                    author.showMyLibrary();
                    break;
                case 3:
                    updateBookAccess(author, scan, menu);
                    break;
                case 4:
                   modifyBookDetails(scan);
                    break;
                case 5:
                    return;
            }
            menu.printAuthorMenu();
            choice = getChoice(scan);
        }
    }

    private void updateBookAccess(UserAuthor author, Scanner scan, Menu menu) throws SQLException {
        System.out.println("  Book name:");
        String bookName = getBookName(scan);
        BookRepository bookRepository = new BookRepository();

        int bookID = bookRepository.getBookIDbyName(bookName);
        Book book = new Book(bookID, true);
        menu.printAccessOptions();
        BookAccess bookAccess = getAccess(scan);

        if (book.getAccess() == bookAccess) {
            System.out.println("  You entered the same access!");
        } else {
            book.setAccessINDB(bookAccess);
            System.out.println("  Access successfully updated!");
        }
    }

    private Book getBookDetails(Scanner scan, Menu menu) throws SQLException {
        String bookTitle = "";
        String authorName = "";
        String ISBN = "";
        String genre = "";
        String language = "";
        BookAccess bookAccess = BookAccess.AVAILABLE;

        menu.printAddBookDetailsHeader();
        System.out.println("  Book name: ");
        bookTitle = scan.nextLine();
        System.out.println("  ISBN: ");
        ISBN = scan.nextLine();
        System.out.println(" Genre: ");
        genre = handleGenre(scan);
        System.out.println("  Language: ");
        language = handleLanguage(scan);
        menu.printAccessOptions();
        bookAccess = getAccess(scan);

        //(int bookID, String title, String author, String ISBN, String genre, String language, double rating, BookAccess access, boolean isRead
        return new Book(0, bookTitle, authorName, ISBN, genre, language, 0.0, bookAccess, true);

    }

    private String handleLanguage(Scanner scan) {
        String language = "";
        try {
            IBookRepository bookRepository = new BookRepository();
            language = scan.nextLine().toLowerCase();

            while (bookRepository.getLanguageID(language) <= 0) {
                System.out.println("  Invalid Input!\n  Please try again!");
                language = scan.nextLine().toLowerCase();
            }

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return language;
    }

    private String handleGenre(Scanner scan) {
        String genre = "";
        try {
            IBookRepository bookRepository = new BookRepository();
            genre = scan.nextLine().toLowerCase();

            while (bookRepository.getGenreID(genre) <= 0) {
                System.out.println("  Invalid Input!\n  Please try again!");
                genre = scan.nextLine().toLowerCase();
            }

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }

        return genre;
    }

    private BookAccess getAccess(Scanner scan) {
        String choice = scan.nextLine().toUpperCase();
        BookAccess bookAccess = BookAccess.AVAILABLE;

        while (!choice.equals("AVAILABLE") && !choice.equals("STAGED") && !choice.equals("ANNOUNCED")) {
            System.out.println("  Invalid input!\n  Please try again!");
            choice = scan.nextLine().toUpperCase();
        }

        switch (choice) {
            case "AVAILABLE":
                break;
            case "STAGED":
                bookAccess = BookAccess.STAGED;
                break;
            case "ANNOUNCED":
                bookAccess = BookAccess.ANNOUNCED;
                break;
        }

        return bookAccess;
    }

    private void interactWithReader(UserReader reader, Scanner scan, Menu menu) throws SQLException {
        int choice = 0;
        menu.printReaderMenu();

        choice = getChoice(scan);

        while (choice > 0 && choice <= 6) {
            switch (choice) {
                case 1:
                    ReaderSearchBook(reader, scan, menu);
                    break;
                case 2:
                    ReaderAddBook(reader, scan);
                    break;
                case 3:
                    Menu.printReaderLastRead();
                    reader.showLastRead();
                    break;
                case 4:
                    ReaderSortLibrary(reader, scan, menu);
                    break;
                case 5:
                    System.out.println("  Book name: ");
                    int bookID = getBookID(scan, menu);
                    System.out.println(" Book rating: ");
                    double rating = getRating(scan, menu);
                    reader.rateBook(bookID, rating);
                    System.out.println("Rating added!");
                    break;
                case 6:
                    return;
            }
            menu.printReaderMenu();
            choice = getChoice(scan);
        }

    }

    private void ReaderAddBook(UserReader reader, Scanner scan) throws SQLException {
        System.out.println("  Book to add:");
        String bookName = getBookName(scan);
        BookRepository bookRepository = new BookRepository();
        int bookID = bookRepository.getBookIDbyName(bookName);
        System.out.println("  Read?\n    1. Yes\n    2. No");
        int read = getChoice(scan);
        boolean isRead = read == 1;
        Book book = new Book(bookID, isRead);
        if (!book.bookExistsInLibrary(reader.getUserRepository().getUserLibraryID(reader.getUserID()))) {
            boolean bookAdded = reader.addToLibrary(bookName, isRead, reader.getUserID());
            if (bookAdded) {
                System.out.println("  Book successfully added!");
            } else {
                System.out.println("  Unable to add!");
            }
        } else {
            System.out.println("  This book is already added!");
        }
    }

    private int getBookID(Scanner scan, Menu menu) {
        int bookID = -1;
        try {
            String bookName = getBookName(scan);
            IBookRepository bookRepository = new BookRepository();
            ArrayList<Book> books = bookRepository.searchBookByName(bookName);
            if (books.isEmpty()) {
                return -1;
            } else {
                Book book = books.get(0);
                bookID = book.getBookID();
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return bookID;
    }

    private double getRating(Scanner scan, Menu menu) {
        String choice;
        do {
            choice = scan.nextLine();
            if (choice.matches("\\d.\\d\\d")) {
                break;
            } else {
                System.out.println("  Invalid Input!");
            }
        } while (!(scan.hasNextInt()) || !choice.matches("\\d.\\d\\d") || Double.parseDouble(choice) > 5.0);
        return Double.parseDouble(choice);
    }

    private void ReaderSortLibrary(UserReader reader, Scanner scan, Menu menu) {
        int choice = 0;
        menu.printReaderSortLibraryMenu();

        choice = getChoice(scan);

        while (choice > 0 && choice <= 4) {
            switch (choice) {
                case 1:
                    Menu.printSortedBy("AUTHOR");
                    reader.sortLibraryByAuthor();
                    break;
                case 2:
                    Menu.printSortedBy("TITLE");
                    reader.sortLibraryByTitle();
                    break;
                case 3:
                    Menu.printSortedBy("GENRE");
                    reader.sortLibraryByGenre();
                    break;
                case 4:
                    return;
            }
            menu.printReaderSortLibraryMenu();
            choice = getChoice(scan);
        }
    }

    private void ReaderSearchBook(UserReader reader, Scanner scan, Menu menu) {
        int choice = 0;
        menu.printReaderSearchBookMenu();

        choice = getChoice(scan);

        while (choice > 0 && choice <= 3) {
            switch (choice) {
                case 1:
                    System.out.println("  Book name: ");
                    String bookName = getBookName(scan);
                    printBooks(reader.searchBookByName(bookName));
                    break;
                case 2:
                    System.out.println("  Author name: ");
                    String authorName = getAuthorName(scan);
                    printBooks(reader.searchBookByAuthor(authorName));
                    break;
                case 3:
                    return;
            }
            menu.printReaderSearchBookMenu();
            choice = getChoice(scan);
        }
    }

    private void printBooks(ArrayList<Book> books) {
        Menu.printReaderLibraryHeader();
        for (Book book : books) {
            System.out.println(book.toStringReader());
        }
        Menu.printReaderLibraryFooter();
    }

    private int getChoice(Scanner scan) {
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

    private String getBookName(Scanner scan) {
        String bookName = "";
        try {
            IBookRepository bookRepository = new BookRepository();
            bookName = scan.nextLine();
            while (!bookRepository.bookExistsInGeneralLibrary(bookName)) {
                System.out.println("  This book does not exist!\n  Please try again!");
                bookName = scan.nextLine();
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return bookName;
    }

    private String getAuthorName(Scanner scan) {
        String authorName = "";
        try {
            IBookRepository bookRepository = new BookRepository();
            authorName = scan.nextLine();
            while (!bookRepository.authorExistsInGeneralLibrary(authorName)) {
                System.out.println("  This author does not exist!\n  Please try again!");
                authorName = scan.nextLine();
            }
        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
        return authorName;
    }

    public void modifyBookDetails(Scanner scan) {
        System.out.println("Enter the ID of the book you wish to modify:");
        int bookID = scan.nextInt();
        scan.nextLine();
        System.out.println("What would you like to modify");
        System.out.println("1. Title");
        System.out.println("2. Genre");
        System.out.println("3. Language");
        System.out.print("Your choice: ");
        int choice = scan.nextInt();
        scan.nextLine();
        try {
            BookRepository bookRepository = new BookRepository();
            switch (choice) {
                case 1:
                    System.out.println("Enter new title");
                    String newTitle = scan.nextLine();
                    bookRepository.updateBookTitle(bookID, newTitle);
                    break;
                case 2:
                    System.out.println("Enter new genre");
                    String newGenre = scan.nextLine();
                    bookRepository.updateBookGenre(bookID, newGenre);
                    break;
                case 3:
                    System.out.println("Enter new language");
                    String newLanguage = scan.nextLine();
                    bookRepository.updateBookLanguage(bookID, newLanguage);
                    break;
            }
        } catch (SQLException e) {
            System.out.printf("Error updating book details: %s%n", e.getMessage());
        }
    }
}
