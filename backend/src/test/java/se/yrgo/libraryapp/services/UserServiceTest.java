package se.yrgo.libraryapp.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    @Test
    @SuppressWarnings("deprecation")
    void correctLogin() {
        String userId = "1";
        UserId id = UserId.of(userId);
        String username = "testuser";
        String password = "password";
        String passwordHash = "password";
        LoginInfo info = new LoginInfo(id, passwordHash);
        PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.validate(username,
                password)).isEqualTo(Optional.of(id));
    }

    @Test
    @SuppressWarnings("deprecation")
    void IncorrectLogin() {
        String userId = "1";
        UserId id = UserId.of(userId);
        String username = "testuser";
        String badUser = "incorrectuser";
        String badPassword = "incorrectPassword";
        String passwordHash = "password";
        LoginInfo info = new LoginInfo(id, passwordHash);
        PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.validate(badUser,
                badPassword)).isNotEqualTo(Optional.of(id));
    }
    @Test
    void checkRegister(){
        final String name = "testuser";
        final String realName = "testname";
        final String password = "password";
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        when(userDao.register(name, realName, password)).thenReturn(true);
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.register(name, realName, password)).isTrue();
    }

    @Test
    void specialNameRegister(){
        final String name = "test";
        final String formattedName = "o\\'test";
        final String realName = "o'test";
        final String password = "password";
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        UserService userService = new UserService(userDao, encoder);
        when(userDao.register(name, realName, password)).thenReturn(true);
        assertThat(userService.register(name, formattedName, password)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"shr"})
    @NullSource
    void checkInvalidNames(String invalidName){
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.isNameAvailable(invalidName)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"John", "Cecilia", "Bosse"})
    void checkValidNames(String validName){
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        UserService userService = new UserService(userDao, encoder);
        when(userDao.isNameAvailable(validName)).thenReturn(true);
        assertThat(userService.isNameAvailable(validName)).isTrue();

    }

}


