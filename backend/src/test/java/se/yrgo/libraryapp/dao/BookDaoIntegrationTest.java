package se.yrgo.libraryapp.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import se.yrgo.libraryapp.entities.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tag("integration")
@H2
public class BookDaoIntegrationTest {
    private static DataSource ds;
    private static Connection con;

    @BeforeAll
    static void initDataSource() throws SQLException {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        BookDaoIntegrationTest.ds = ds;
        BookDaoIntegrationTest.con = ds.getConnection();
    }

    @Test
    void getCorrectBook() {
        BookDao bookDao = new BookDao(ds);
        var bookId = new BookId(1);
        var book = bookDao.get(bookId);
        assertThat(book).isPresent();
        assertThat(book.get().getId()).isEqualTo(bookId);
    }

    @Test
    void getIncorrectBook() {
        BookDao bookDao = new BookDao(ds);
        BookId bookId = new BookId(999);
        Optional<Book> book = bookDao.get(bookId);
        assertThat(book).isNotPresent();
    }

    @Test
    void findCorrectBook() {
        BookDao bookDao = new BookDao(ds);
        String isbn = "9781784726188";
        String title = "The world atlas of wine";
        String author = "Hugh Johnson";
        Set<BookEdition> books = bookDao.find(isbn, title, author);
        assertThat(books).isNotEmpty();
    }

    @Test
    void findIncorrectBook() {
        BookDao bookDao = new BookDao(ds);
        String isbn = "9999999999999";
        String title = "Fake Book Title";
        String author = "Fake Author";
        Set<BookEdition> books = bookDao.find(isbn, title, author);
        assertThat(books).isEmpty();
    }

    @Test
    void findCorrectClassification(){
        BookDao bookDao = new BookDao(ds);
        DdsClassification classification = new DdsClassification(641, "Cookery");
        Set<BookEdition> books = bookDao.withClass(classification);
        assertThat(books).isNotEmpty();
    }

    @Test
    void findIncorrectClassification(){
        BookDao bookDao = new BookDao(ds);
        DdsClassification classification = new DdsClassification(999, "Fake Classification");
        Set<BookEdition> books = bookDao.withClass(classification);
        assertThat(books).isEmpty();
    }

    @Test
    void findExistingLoans() {
        BookDao bookDao = new BookDao(ds);
        UserId user = new UserId(2);
        List<BookLoan> loans = bookDao.loans(user);
        assertThat(loans).isNotEmpty();
    }

    @Test
    void findNonExistingLoans() {
        BookDao bookDao = new BookDao(ds);
        UserId user = new UserId(1);
        List<BookLoan> loans = bookDao.loans(user);
        assertThat(loans).isEmpty();
    }

    @Test
    void findIncorrectLoans() {
        BookDao bookDao = new BookDao(ds);
        UserId user = new UserId(9999);
        List<BookLoan> loans = bookDao.loans(user);
        assertThat(loans).isEmpty();
    }

    @Test
    void returnCorrectBook(){
        BookDao bookDao = new BookDao(ds);
        BookId book = new BookId(2);
        boolean result = bookDao.returnBook(book);
        assertThat(result).isTrue();
    }

    @Test
    void returnIncorrectBook(){
        BookDao bookDao = new BookDao(ds);
        BookId book = new BookId(9999);
        boolean result = bookDao.returnBook(book);
        assertThat(result).isFalse();
    }

}
