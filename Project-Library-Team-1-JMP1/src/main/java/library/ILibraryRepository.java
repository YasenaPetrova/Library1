package library;

import book.Book;

public interface ILibraryRepository {
    void sortLibraryByAuthor();
    void sortLibraryByGenre();
    void sortLibraryByName();
    int getAvailableLibraryID();
    int addToLibrary(Book book, int userID, boolean isRead);
    int getLibraryIDFromDB(int userID);
}
