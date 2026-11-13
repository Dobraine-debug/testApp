package se.yrgo.libraryapp.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.DdsClassification;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    @Mock
    private BookDao bookDao;

    @Test
    public void searchValidBook(){
        final String isbn = "9781784726188";
        final String title = "The world atlas of wine";
        final String author = "Hugh Johnson";
        final String classification = "641";
        BookController controller = new BookController(bookDao);
        Set<BookEdition> expectedBooks = Set.of(new BookEdition(1,title, author, isbn, new DdsClassification(641, null)));
        when(bookDao.withClass(new DdsClassification(Integer.parseInt(classification), null))).thenReturn(expectedBooks);
        assertThat(controller.search(isbn, title, author, classification)).isEqualTo(expectedBooks);
    }

    @Test
    public void searchNullClassification(){
        final String isbn = "9781784726188";
        final String title = "The world atlas of wine";
        final String author = "Hugh Johnson";
        final String classification = null;
        BookController controller = new BookController(bookDao);
        Set<BookEdition> expectedBooks = Set.of(new BookEdition(1,title, author, isbn, new DdsClassification(641, null)));
        when(bookDao.find(isbn, title, author)).thenReturn(expectedBooks);
        assertThat(controller.search(isbn, title, author, classification)).isEqualTo(expectedBooks);
    }
}
