package se.yrgo.libraryapp.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.forms.LoginData;
import se.yrgo.libraryapp.services.UserService;
import io.jooby.*;
import io.jooby.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private RoleDao roleDao;

    @Mock
    private SessionDao sessionDao;


    @Test
    void validLoginTest(){
        LoginController controller = new LoginController(userService, roleDao, sessionDao);
        String username = "test";
        String password = "$argon2i$v=19$m=16,t=2,p=1$MTIzNDU2Nzg5MDEyMzQ1NjA$LmFqTZeUWwqsnbZCS2E8XQ";
        String sessioncookie = "sessioncookie";
        Context context = mock(Context.class);

        Optional<UserId> user = Optional.of(new UserId(1));
        when(userService.validate(username, password)).thenReturn(user);
        when(sessionDao.create(user.get())).thenReturn(java.util.UUID.randomUUID());
        when(roleDao.get(user.get())).thenReturn(List.of(Role.USER));

        List<Role> roles = controller.login(context, sessioncookie, new LoginData(username, password));
        assertThat(roles).isNotEmpty();
    }

    @Test
    void invalidLoginTest(){
        LoginController controller = new LoginController(userService, roleDao, sessionDao);
        String username = "falsetest";
        String password = "falsepassword";
        String sessioncookie = "sessioncookie";
        Context context = mock(Context.class);

        Optional<UserId> emptyUser = Optional.empty();
        when(userService.validate(username, password)).thenReturn(emptyUser);

        List<Role> roles = controller.login(context, sessioncookie, new LoginData(username, password));
        assertThat(roles).isEmpty();
    }

    @Test
    void isLoggedInTest() {
        LoginController controller = new LoginController(userService, roleDao, sessionDao);
        String sessionCookie = java.util.UUID.randomUUID().toString();
        UserId userId = new UserId(1);
        when(sessionDao.validate(UUID.fromString(sessionCookie))).thenReturn(userId);
        when(roleDao.get(userId)).thenReturn(List.of(Role.USER));
        List<Role> roles = controller.isLoggedIn(sessionCookie);
        assertThat(roles).isNotEmpty();
    }

    @Test
    void isNotLoggedInTest() {
        LoginController controller = new LoginController(userService, roleDao, sessionDao);
        String sessionCookie = java.util.UUID.randomUUID().toString();
        when(sessionDao.validate(UUID.fromString(sessionCookie))).thenThrow(new IllegalArgumentException());
        List<Role> roles = controller.isLoggedIn(sessionCookie);
        assertThat(roles).isEmpty();
    }
}
