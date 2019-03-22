package bookshopsystemapp.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface BookService {

    void seedBooks() throws IOException;

    List<String> getAllBooksTitlesAfter();

    Set<String> getAllAuthorsWithBookBefore();

    List<String> bookTitlesByAgeRestriction(String ageRestrictionStr);

    List<String> getGoldenBooks();

    String getBooksByPrice();

    String getNotReleasedBooks(String year);

    String getReleasedBeforeDate(String date);

    String getTitleContaining(String pattern);

    String getBookByAuthorLastName(String pattern);

    Integer getBooksCountByTitleLength(Integer num);

    String getReducedBook(String title);
}
