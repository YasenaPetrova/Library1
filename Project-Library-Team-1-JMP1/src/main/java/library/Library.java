package library;

import book.Book;
import book.BookRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.*;

public class Library {
    private int libraryID;
    private String libraryName;
    private final LibraryRepository libraryRepository;

    private HashMap<Book, LocalDate> books;


    public Library() throws SQLException {
        this.libraryRepository = new LibraryRepository();
        this.libraryID = libraryRepository.getAvailableLibraryID();
        this.libraryName = "New Library";
        this.books = new HashMap<>();
    }

    public Library(int libraryID, String libraryName, HashMap<Book, LocalDate> books) throws SQLException {
        this.libraryRepository = new LibraryRepository();
        this.libraryID = libraryID;
        this.libraryName = libraryName;
        this.books = books;
    }

    public Library(int libraryID, String libraryName, LibraryRepository libraryRepository){
        this.libraryID = libraryID;
        this.libraryName = libraryName;
        this.books = new HashMap<>();
        this.libraryRepository = libraryRepository;
    }

    public void addBookToLibrary(String bookName, boolean isRead, int userID) {
        try {
            BookRepository bookRepository = new BookRepository();
            ArrayList<Book> books = bookRepository.searchBookByName(bookName);
            for (Book book: books) {
                this.books.put(book, LocalDate.now());
                libraryRepository.addToLibrary(book, userID, isRead);
            }

        } catch (SQLException e) {
            System.out.printf("Error message: %s, cause: %s%n", e.getMessage(), e.getCause());
        }
    }

    public int getLibraryID() {
        return libraryID;
    }

    public void setLibraryID(int libraryID) {
        this.libraryID = libraryID;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public HashMap<Book, LocalDate> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, LocalDate> books) {
        this.books = books;
    }

    public boolean isEmpty() {
        return books.isEmpty();
    }
}
