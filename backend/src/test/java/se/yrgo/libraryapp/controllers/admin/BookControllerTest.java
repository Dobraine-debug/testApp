package se.yrgo.libraryapp.controllers.admin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pac4j.core.profile.CommonProfile;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    @Mock
    private BookDao bookDao;

    @Test
    public void testSearch(){
        BookController controller = new BookController(bookDao);
        String bookId = "1";
        Book book = new Book(BookId.of(1),true,
                new BookEdition(1, "Test book", "Test author",
                        "1234567890", new DdsClassification(11, "123")));
        when(bookDao.get(BookId.of(bookId))).thenReturn(Optional.of(book));
        assertThat(controller.search(bookId)).isEqualTo(book);
    }

    @Test
    public void exceptionTest(){
        BookController controller = new BookController(bookDao);
        String bookId = "1";
        when(bookDao.get(BookId.of(bookId))).thenThrow(new NumberFormatException());
        assertThat(controller.search(bookId)).isNull();
    }
}
