package se.yrgo.libraryapp.controllers;

import io.jooby.Context;
import io.jooby.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.yrgo.libraryapp.dao.SessionDao;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogoutControllerTest {
    @Mock
    private SessionDao sessionDao;

    @Mock
    private Context context;

    @Test
    void testLogout() {
        String sessioncookie = "sessioncookie";
        LogoutController controller = new LogoutController(sessionDao);
        assertThatCode(() -> controller.logout(context, sessioncookie)).doesNotThrowAnyException();

    }
}
