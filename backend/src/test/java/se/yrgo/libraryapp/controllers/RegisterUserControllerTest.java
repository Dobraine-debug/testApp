package se.yrgo.libraryapp.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.forms.RegisterUserData;
import se.yrgo.libraryapp.services.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class RegisterUserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private UserDao userDao;

    private RegisterUserData userData = new RegisterUserData("testuser", "Test User", "password123");

    @Test
    void TestGoodRegister(){
        RegisterUserController controller = new RegisterUserController(userDao, userService);
        RegisterUserData userData = new RegisterUserData("testuser", "Test User", "password123");
        assertThat(controller.register(userData)).isTrue();
    }

    @Test
    void TestBadRegister(){
        RegisterUserController controller = new RegisterUserController(userDao, userService);
        RegisterUserData userData = new RegisterUserData("", "", "");
        assertThat(controller.register(userData)).isFalse();
    }
}
