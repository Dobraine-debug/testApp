package se.yrgo.libraryapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.BookEdition;

import java.util.Set;

import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class BookServiceTest {
    @Mock
    private BookDao bookDao;;

    @Test
    public void findValidName(){
        final String isbn = "9781784726188";
        final String title = "The world atlas of wine";
        final String author = "Hugh Johnson";
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        BookService bookService = new BookService(bookDao, encoder);
        when(bookDao.find(isbn, title, author)).thenReturn(Set.of(new BookEdition(isbn, title, author)));
        Set<BookEdition> book = bookService.find(isbn, title, author);
        assert(book.size() == 1);
    }

    @ParameterizedTest
    @NullSource
    public void findNullBook(String value){
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        BookService bookService = new BookService(bookDao, encoder);
        Set<BookEdition> book = bookService.find(value, value, value);
        assert(book.size() == 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {" "})
    public void findEmptyBook(String value){
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        BookService bookService = new BookService(bookDao, encoder);
        Set<BookEdition> book = bookService.find(value, value, value);
        assert(book.size() == 0);
    }
}
