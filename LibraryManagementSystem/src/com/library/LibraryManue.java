package com.library;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LibraryManue {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option;
        Library library = new Library();

        do {
            System.out.println();
            System.out.println("1) Add book              : ");
            System.out.println("2) Remove book           : ");
            System.out.println("3) Search book by title  : ");
            System.out.println("4) Search book by author : ");
            System.out.println("5) List book             : ");
            System.out.println("6) List available book   : ");
            System.out.println("7) Exit                  : ");
            System.out.println("--------------------------");
            System.out.print("Enter you choice : ");
            try {
                option = scanner.nextInt();
                System.out.println("--------------------------");
                
                switch (option) {
                    case 1:
                        System.out.print("Enter title of the book : ");
                        scanner.nextLine();
                        String title = scanner.nextLine();
                        System.out.print("Enter author of the book : ");
                        String author = scanner.nextLine();
                        System.out.print("Enter ISBN of the book : ");
                        String isbn = scanner.nextLine();
                        System.out.print("Enter genre of the book : ");
                        String genre = scanner.nextLine();
                        System.out.print("Enter publication year of the book : ");
                        int publicationYear = scanner.nextInt();
                        System.out.print("Enter department : ");
                        scanner.nextLine();
                        String department = scanner.nextLine();
                        System.out.print("Enter availablity (true/false) : ");
                        boolean availablity = scanner.nextBoolean();

                        Book book = new Book(title, author, isbn, genre, publicationYear, department, availablity);
                        library.addBook(book);      
                        break;
                    
                    case 2:
                        System.out.print("Enter the ISBN : ");
                        scanner.nextLine();
                        String bIsbn = scanner.nextLine();
                        library.removeBook(bIsbn);
                        break;

                    case 3:
                        System.out.print("Enter the title : ");
                        scanner.nextLine();
                        String bTitle = scanner.nextLine();
                        List<Book> bookList1 = library.findBookByTitle(bTitle);
                        printBooks(bookList1);
                        break;

                    case 4:
                        System.out.print("Enter the author : ");
                        scanner.nextLine();
                        String bAuthor = scanner.nextLine();
                        List<Book> bookList2 = library.findBookByAuthor(bAuthor);
                        printBooks(bookList2);
                        break;

                    case 5:
                        List<Book> bookList3 = library.listAllBooks();
                        printBooks(bookList3);
                        break;

                    case 6:
                        List<Book> bookList4 = library.listAvailableBooks();
                        printBooks(bookList4);
                        break;
                    default:
                        break;
                }

            } catch(InputMismatchException e) {
                System.out.println("Please Enter numeric value only (1 - 5).");
                System.out.println();
                option = 6;
            }

        } while (option != 7);
        scanner.close();
    }

    static private void printBooks(List<Book> books) {
        books.stream().forEach(book -> {
            System.out.println(book.toString());
        });
    }

}