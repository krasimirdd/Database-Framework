package bookshopsystemapp.service;

import bookshopsystemapp.domain.entities.*;
import bookshopsystemapp.repository.AuthorRepository;
import bookshopsystemapp.repository.BookRepository;
import bookshopsystemapp.repository.CategoryRepository;
import bookshopsystemapp.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final static String BOOKS_FILE_PATH = "D:\\IntelliJ_Projects\\DatabasesFrameworks\\SpringDataAdvanced\\src\\main\\resources\\files\\books.txt";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final FileUtil fileUtil;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository, FileUtil fileUtil) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() != 0) {
            return;
        }

        String[] booksFileContent = this.fileUtil.getFileContent(BOOKS_FILE_PATH);
        for (String line : booksFileContent) {
            String[] lineParams = line.split("\\s+");

            Book book = new Book();
            book.setAuthor(this.getRandomAuthor());

            EditionType editionType = EditionType.values()[Integer.parseInt(lineParams[0])];
            book.setEditionType(editionType);

            LocalDate releaseDate = LocalDate.parse(lineParams[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
            book.setReleaseDate(releaseDate);

            int copies = Integer.parseInt(lineParams[2]);
            book.setCopies(copies);

            BigDecimal price = new BigDecimal(lineParams[3]);
            book.setPrice(price);

            AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(lineParams[4])];
            book.setAgeRestriction(ageRestriction);

            StringBuilder title = new StringBuilder();
            for (int i = 5; i < lineParams.length; i++) {
                title.append(lineParams[i]).append(" ");
            }

            book.setTitle(title.toString().trim());

            Set<Category> categories = this.getRandomCategories();
            book.setCategories(categories);

            this.bookRepository.saveAndFlush(book);
        }
    }

    @Override
    public List<String> getAllBooksTitlesAfter() {
        List<Book> books = this.bookRepository.findAllByReleaseDateAfter(LocalDate.parse("2000-12-31"));

        return books.stream().map(b -> b.getTitle()).collect(Collectors.toList());
    }

    @Override
    public Set<String> getAllAuthorsWithBookBefore() {
        List<Book> books = this.bookRepository.findAllByReleaseDateBefore(LocalDate.parse("1990-01-01"));

        return books.stream().map(b -> String.format("%s %s", b.getAuthor().getFirstName(), b.getAuthor().getLastName())).collect(Collectors.toSet());
    }

    private Author getRandomAuthor() {
        Random random = new Random();

        int randomId = random.nextInt((int) (this.authorRepository.count() - 1)) + 1;

        return this.authorRepository.findById(randomId).orElse(null);
    }

    private Set<Category> getRandomCategories() {
        Set<Category> categories = new LinkedHashSet<>();

        Random random = new Random();
        int length = random.nextInt(5);

        for (int i = 0; i < length; i++) {
            Category category = this.getRandomCategory();

            categories.add(category);
        }

        return categories;
    }

    private Category getRandomCategory() {
        Random random = new Random();

        int randomId = random.nextInt((int) (this.categoryRepository.count() - 1)) + 1;

        return this.categoryRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<String> bookTitlesByAgeRestriction(String ageRestrictionStr) {

        AgeRestriction restriction = AgeRestriction.valueOf(ageRestrictionStr.toUpperCase());

        List<Book> books = bookRepository.findAllByAgeRestriction(restriction);

        return books.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());

    }

    @Override
    public List<String> getGoldenBooks() {

        List<Book> books = bookRepository.findAllByEditionType(EditionType.valueOf("GOLD")).stream()
                .filter(book -> book.getCopies() < 5000).collect(Collectors.toList());

        return books.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public String getBooksByPrice() {
        List<Book> books = bookRepository.findAllByPrice();

        return String.join(System.lineSeparator(),
                books.stream()
                        .map(b -> String.format("%s - $%.2f",
                                b.getTitle(), b.getPrice()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public String getNotReleasedBooks(String year) {
        LocalDate before = LocalDate.parse(year + "-01-01");
        LocalDate after = LocalDate.parse(year + "-12-31");

        List<Book> books = bookRepository.findAllByReleaseDateBeforeOrReleaseDateAfter(before, after);

        return String.join(System.lineSeparator(),
                books.stream().
                        map(Book::getTitle).collect(Collectors.toList()
                )
        );
    }

    @Override
    public String getReleasedBeforeDate(String date) {
        LocalDate before = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        List<Book> books = bookRepository.findAllByReleaseDateBefore(before);

        return String.join(System.lineSeparator(),
                books.stream()
                        .map(Book::getTitle)
                        .collect(Collectors.toList()
                        )
        );
    }

    public String getTitleContaining(String pattern) {

        List<Book> books = bookRepository.findAllByTitleContains(pattern);

        return String.join(System.lineSeparator(),
                books.stream()
                        .map(Book::getTitle)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public String getBookByAuthorLastName(String pattern) {

        List<Book> books = bookRepository.findAllByAuthorLastName(pattern.concat("%"));

        return String.join(System.lineSeparator(),
                books.stream()
                        .map(b -> String.format("%s (%s %s)",
                                b.getTitle(), b.getAuthor().getFirstName(), b.getAuthor().getLastName()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Integer getBooksCountByTitleLength(Integer num) {
        Integer booksCount = bookRepository.countBooksByTitleLength(num);
        return booksCount;
    }

    @Override
    public String getReducedBook(String title) {
        ReducedBook book = bookRepository.findByTitle(title);
        return String.format("%s %s %s %.2f",
                book.getTitle(),
                book.getEditionType(),
                book.getAgeRestriction(),
                book.getPrice()
        );
    }
}
