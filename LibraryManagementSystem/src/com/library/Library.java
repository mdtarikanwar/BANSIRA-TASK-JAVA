package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
    	
    	Connection connection = JdbcConnectivityUtil.getConnection();
    	
    	if (connection == null) {
    		boolean bookNotFoundInListObject = books.stream().noneMatch(b -> b.getISBN().equals(book.getISBN()));
    		if (bookNotFoundInListObject) {
    			books.add(book);
    			System.out.println("Book with ISBN " + book.getISBN() + " added successfully.");
    		}
    		else {
    			System.out.println("Book with ISBN " + book.getISBN() + " already exists.");
    		}
    	}
    	
    	else {
    		
			try {
				PreparedStatement preparedStatement = null;
				Integer departmentId = null;
				
				// checking for existing book.
	            Statement statement = connection.createStatement();
	            String queryForBook = "SELECT * FROM Book where isbn = '"+book.getISBN()+"'";
	            ResultSet existingBook = statement.executeQuery(queryForBook);
	            
	            if (existingBook.next()) {
	            	System.out.println("Book with ISBN " + book.getISBN() + " already exists.");
	            }
				
	            else {
	            	// getting already existing department id.
		            String queryForDepartment = "SELECT * FROM Department where name = '"+book.getDepartment()+"'";
		            ResultSet departments = statement.executeQuery(queryForDepartment);
		            
		            if (departments.next()) {
		            	departmentId = departments.getInt(1);
		            }
		            
		            if (departmentId == null) {
		            	String insertQueryForDepartment = "INSERT INTO Department (name) VALUE(?)";
		            	preparedStatement = connection.prepareStatement(insertQueryForDepartment, PreparedStatement.RETURN_GENERATED_KEYS);
		            	preparedStatement.setString(1, book.getDepartment());
		            	int rowEffected = preparedStatement.executeUpdate();
		            	if (rowEffected > 0) {
		            		ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
		            		if (generatedKeys.next()) {
		            			departmentId = generatedKeys.getInt(1);
		            		}
		            	}
		            }

		            // Execute a query
		            String sql = "INSERT INTO Book (title, author, isbn, genre, publicationYear, available, downloadCount, departmentId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		            // Create a PreparedStatement
		            preparedStatement = connection.prepareStatement(sql);

		            // Set the parameters
		            preparedStatement.setString(1, book.getTitle());
		            preparedStatement.setString(2, book.getAuthor());
		            preparedStatement.setString(3, book.getISBN());
		            preparedStatement.setString(4, book.getGenre());
		            preparedStatement.setInt(5, book.getPublicationYear());
		            preparedStatement.setBoolean(6, book.isAvailability());
		            preparedStatement.setInt(7, 0);
		            preparedStatement.setInt(8, departmentId); 

		            // Execute the insert operation
		            preparedStatement.executeUpdate();
		            System.out.println("Book with ISBN " + book.getISBN() + " added successfully.");
	            }
					            
			} catch (SQLException e) {
				e.printStackTrace();
			}

            
        }
    }

    public void removeBook(String ISBN) {
    	
    	Connection connection = JdbcConnectivityUtil.getConnection();
    	
    	if (connection == null) {    		
    		boolean foundBook = books.stream().anyMatch(book -> book.getISBN().equals(ISBN));
    		if (foundBook) {
    			books.removeIf(book -> book.getISBN().equals(ISBN));
    			System.out.println("Book with ISBN " + ISBN + " removed successfully.");
    		}
    		else {
    			System.out.println("No book found with the given ISBN.");
    		}
    	}
    	else {
    		
    		try {
    			String deleteQuery = "DELETE FROM Book WHERE isbn = ?";
				PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
				preparedStatement.setString(1, ISBN);
				
				int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Book with ISBN " + ISBN + " was successfully deleted.");
	            } 
	            else {
	                System.out.println("No book found with the given ISBN.");
	            }
				
			} catch (SQLException e) {
				System.out.println("Something went wrong.");
				e.printStackTrace();
			}
    	}
    }

    public List<Book> findBookByTitle(String title) {
    	
    	Connection connection = JdbcConnectivityUtil.getConnection();
    	List<Book> responseList = new ArrayList<>();
    	if (connection == null) {    		
    		return books.stream()
    				.filter(book -> book.getTitle().equalsIgnoreCase(title))
    				.collect(Collectors.toList());
    	}
    	else {
    		
    		try {    			
  		
    			String selectQuery = "SELECT b.*, d.name AS departmentName FROM Book b " +
                         			 "JOIN Department d ON b.departmentId = d.id WHERE b.title = ?";
			    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			    preparedStatement.setString(1, title);
			
			    // Execute the query
			    ResultSet resultSet = preparedStatement.executeQuery();
			
			    // Process the result set
			    while (resultSet.next()) {
			        Book book = new Book();
			        book.setTitle(resultSet.getString("title"));
			        book.setAuthor(resultSet.getString("author"));
			        book.setISBN(resultSet.getString("isbn"));
			        book.setGenre(resultSet.getString("genre"));
			        book.setPublicationYear(resultSet.getInt("publicationYear"));
			        book.setAvailability(resultSet.getBoolean("available"));
			        book.setDownloadCount(resultSet.getInt("downloadCount"));
			        book.setDepartment(resultSet.getString("departmentName"));
			
			        responseList.add(book);
			    }
			    if (responseList.isEmpty()) {
			    	System.out.println("Book not found for the title : " + title);
			    }
			    			
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    		
    	}
    	return responseList;
    }

    public List<Book> findBookByAuthor(String author) {
    	
    	Connection connection = JdbcConnectivityUtil.getConnection();
    	List<Book> responseList = new ArrayList<>();
    	
    	if (connection == null) {    		
    		return books.stream()
    				.filter(book -> book.getAuthor().equalsIgnoreCase(author))
    				.collect(Collectors.toList());
    	}
    	else {
    		try {    			
    	  		
    			String selectQuery = "SELECT b.*, d.name AS departmentName FROM Book b " +
                         			 "JOIN Department d ON b.departmentId = d.id WHERE b.author = ?";
			    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			    preparedStatement.setString(1, author);
			
			    // Execute the query
			    ResultSet resultSet = preparedStatement.executeQuery();
			
			    // Process the result set
			    while (resultSet.next()) {
			        Book book = new Book();
			        book.setTitle(resultSet.getString("title"));
			        book.setAuthor(resultSet.getString("author"));
			        book.setISBN(resultSet.getString("isbn"));
			        book.setGenre(resultSet.getString("genre"));
			        book.setPublicationYear(resultSet.getInt("publicationYear"));
			        book.setAvailability(resultSet.getBoolean("available"));
			        book.setDownloadCount(resultSet.getInt("downloadCount"));
			        book.setDepartment(resultSet.getString("departmentName"));
			
			        responseList.add(book);
			    }
			    if (responseList.isEmpty()) {
			    	System.out.println("Book not found for the author : " + author);
			    }
			    			
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    	return responseList;
    }

    public List<Book> listAllBooks() {
    	Connection connection = JdbcConnectivityUtil.getConnection();
    	List<Book> responseList = new ArrayList<>();
    	
    	if (connection == null) {    		
    		return new ArrayList<>(books);
    	}
    	else {
			try {    			
			    	  		
    			String selectQuery = "SELECT b.*, d.name AS departmentName FROM Book b " +
                         			 "JOIN Department d ON b.departmentId = d.id";
			    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			    // Execute the query
			    ResultSet resultSet = preparedStatement.executeQuery();
			
			    // Process the result set
			    while (resultSet.next()) {
			        Book book = new Book();
			        book.setTitle(resultSet.getString("title"));
			        book.setAuthor(resultSet.getString("author"));
			        book.setISBN(resultSet.getString("isbn"));
			        book.setGenre(resultSet.getString("genre"));
			        book.setPublicationYear(resultSet.getInt("publicationYear"));
			        book.setAvailability(resultSet.getBoolean("available"));
			        book.setDownloadCount(resultSet.getInt("downloadCount"));
			        book.setDepartment(resultSet.getString("departmentName"));
			
			        responseList.add(book);
			    }
			    			
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return responseList;
    	
    }

    public List<Book> listAvailableBooks() {
    	Connection connection = JdbcConnectivityUtil.getConnection();
    	List<Book> responseList = new ArrayList<>();
    	
    	if (connection == null) {    		
    		return books.stream()
    				.filter(Book::isAvailability)
    				.collect(Collectors.toList());
    	}
    	else {
    		try {    			
    	  		
    			String selectQuery = "SELECT b.*, d.name AS departmentName FROM Book b " +
                         			 "JOIN Department d ON b.departmentId = d.id WHERE b.available = ?";
			    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			    preparedStatement.setBoolean(1, true);
			
			    // Execute the query
			    ResultSet resultSet = preparedStatement.executeQuery();
			
			    // Process the result set
			    while (resultSet.next()) {
			        Book book = new Book();
			        book.setTitle(resultSet.getString("title"));
			        book.setAuthor(resultSet.getString("author"));
			        book.setISBN(resultSet.getString("isbn"));
			        book.setGenre(resultSet.getString("genre"));
			        book.setPublicationYear(resultSet.getInt("publicationYear"));
			        book.setAvailability(resultSet.getBoolean("available"));
			        book.setDownloadCount(resultSet.getInt("downloadCount"));
			        book.setDepartment(resultSet.getString("departmentName"));
			
			        responseList.add(book);
			    }
			    			
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return responseList;
    }
}
