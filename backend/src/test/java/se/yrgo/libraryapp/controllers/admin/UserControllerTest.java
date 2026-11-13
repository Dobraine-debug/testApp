package se.yrgo.libraryapp.controllers.admin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pac4j.core.profile.CommonProfile;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserDao userDao;

    @Test
    public void getCorrectUser() throws SQLException {
        UserController controller = new UserController(userDao);
        String userId = "1";
        User user = new User(UserId.of(1), "test", "Mr Test");
        when(userDao.get(userId)).thenReturn(Optional.of(user));
        assertThat(controller.getUser(userId)).isEqualTo(user);
    }

    @Test
    public void getIncorrectUser() throws SQLException {
        UserController controller = new UserController(userDao);
        String userId = "999";
        when(userDao.get(userId)).thenReturn(Optional.empty());
        assertThat(controller.getUser(userId)).isNull();
    }

    @Test
    public void getException() throws SQLException {
        UserController controller = new UserController(userDao);
        String userId = "1";
        when(userDao.get(userId)).thenThrow(NumberFormatException.class);
        assertThat(controller.getUser(userId)).isNull();
    }
}
