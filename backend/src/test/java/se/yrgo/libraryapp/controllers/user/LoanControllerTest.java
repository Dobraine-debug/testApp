package se.yrgo.libraryapp.controllers.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pac4j.core.profile.CommonProfile;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanControllerTest {
    @Mock
    private BookDao bookDao;

    @Test
    public void testLoans() {
        LoanController controller = new LoanController(bookDao);
        CommonProfile user = new CommonProfile();
        user.setId("1");
        BookEdition edition = null;
        Book book = new Book(new BookId(1), true, edition);
        BookLoan loan = new BookLoan(book, new UserId(1), LocalDate.now());
        List<BookLoan> loans = new java.util.ArrayList<>(List.of());
        loans.add(loan);
        when(bookDao.loans(UserId.of(user.getId()))).thenReturn(loans);
        assertThat(controller.loans(user)).isEqualTo(loans);
    }

    @Test
    public void testNullLoans(){
        LoanController controller = new LoanController(bookDao);
        CommonProfile user = null;
        assertThat(controller.loans(user)).isEmpty();
    }
}
