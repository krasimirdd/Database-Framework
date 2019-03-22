package bookshopsystemapp.controller;

import bookshopsystemapp.service.AuthorService;
import bookshopsystemapp.service.BookService;
import bookshopsystemapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Controller
public class BookshopController implements CommandLineRunner {

    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final BookService bookService;

    @Autowired
    public BookshopController(AuthorService authorService, CategoryService categoryService, BookService bookService) {
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... strings) throws Exception {
       /* this.authorService.seedAuthors();
        this.categoryService.seedCategories();
        this.bookService.seedBooks();
       */

        // ex1 ->  booksTitlesByAgeRestriction();
        // ex2 ->  goldenBooks();
        // ex3 ->  booksByPrice();
        // ex4 ->  notReleasedBooks();
        // ex5 ->  booksReleasedBeforeDate();
        // ex6 ->  authorsSearch();
        // ex7 ->  getBooksByTitlePattern();
        // ex8 ->  bookSearchByAuthor();
        // ex9 ->  countBooks();
        // ex10 -> totalBookCopies();
        // ex11 -> getReducedBook();
    }

    private void booksTitlesByAgeRestriction() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String ageRestriction = reader.readLine();

        bookService.bookTitlesByAgeRestriction(ageRestriction)
                .forEach(System.out::println);
    }

    private void goldenBooks() {
        bookService.getGoldenBooks().forEach(System.out::println);
    }

    private void booksByPrice() {
        System.out.println(bookService.getBooksByPrice());
    }

    private void notReleasedBooks() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String year = reader.readLine();

        System.out.println(bookService.getNotReleasedBooks(year));
    }

    private void booksReleasedBeforeDate() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String date = reader.readLine();
        System.out.println(bookService.getReleasedBeforeDate(date));
    }

    private void authorsSearch() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String pattern = reader.readLine();
        System.out.println(authorService.getAuthorByPattern(pattern));
    }

    private void getBooksByTitlePattern() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String pattern = reader.readLine();

        System.out.println(bookService.getTitleContaining(pattern));
    }

    private void bookSearchByAuthor() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String pattern = reader.readLine();
        System.out.println(bookService.getBookByAuthorLastName(pattern));
    }

    private void countBooks() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Integer num = Integer.valueOf(reader.readLine());

        System.out.println(bookService.getBooksCountByTitleLength(num));
    }

    private void totalBookCopies() {
        System.out.println(authorService.getTotalBookCopies());
    }

    private void getReducedBook() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String title = reader.readLine();

        System.out.println(bookService.getReducedBook(title));
    }
}
