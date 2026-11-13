package se.yrgo.libraryapp.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BookService {
    private BookDao bookDao;
    private PasswordEncoder encoder;

    @Inject
    public BookService(BookDao bookDao, PasswordEncoder encoder) {

        this.bookDao = bookDao;
        this.encoder = encoder;
    }
    public Optional<Book> get(String id) {
        BookId bookId = BookId.of(id);
        return bookDao.get(bookId);
    }

    public Set<BookEdition> find(String isbn, String title, String author) {
        if ((isbn == null || isbn.trim().length() == 0)
                && (title == null || title.trim().length() == 0)
                && (author == null || author.trim().length() == 0)) {
            return Set.of();
        }
        return bookDao.find(isbn, title, author);
    }

    public Set<BookEdition> withClass(DdsClassification classification) {
        return bookDao.withClass(classification);
    }

    public List<BookLoan> loans(UserId userId) {
        return bookDao.loans(userId);
    }

    public List<BookLoan> overdueLoans() {
        return bookDao.overdueLoans();
    }

    public boolean lend(BookId book, UserId user) {
        return bookDao.lend(book, user);
    }

    public boolean returnBook(BookId book) {
        return bookDao.returnBook(book);
    }
}
