package com.library;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LibraryTest {
    private Library library;
    private Book book1;
    private Book book2;

    @Before
    public void setUp() {
        library = new Library();
        book1 = new Book("Effective Java", "Joshua Bloch", "1234567890", "Programming", 2018, "Computer Science", true);
        book2 = new Book("Clean Code", "Robert C. Martin", "0987654321", "Programming", 2008, "IT", false);
    }

    @Test
    public void testAddBook() {
        library.addBook(book1);
        assertEquals(1, library.listAllBooks().size());
    }

    @Test
    public void testRemoveBook() {
        library.addBook(book1);
        library.removeBook("1234567890");
        assertEquals(0, library.listAllBooks().size());
    }

    @Test
    public void testFindBookByTitle() {
        library.addBook(book1);
        List<Book> foundBooks = library.findBookByTitle("Effective Java");
        assertEquals(1, foundBooks.size());
    }

    @Test
    public void testFindBookByAuthor() {
        library.addBook(book1);
        List<Book> foundBooks = library.findBookByAuthor("Joshua Bloch");
        assertEquals(1, foundBooks.size());
    }

    @Test
    public void testListAllBooks() {
        library.addBook(book1);
        library.addBook(book2);
        List<Book> allBooks = library.listAllBooks();
        assertEquals(2, allBooks.size());
    }

    @Test
    public void testListAvailableBooks() {
        library.addBook(book1);
        library.addBook(book2);
        List<Book> availableBooks = library.listAvailableBooks();
        assertEquals(1, availableBooks.size());
    }
}
