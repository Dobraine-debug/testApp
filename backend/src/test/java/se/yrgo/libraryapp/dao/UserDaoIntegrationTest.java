package se.yrgo.libraryapp.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tag("integration")
@H2
public class UserDaoIntegrationTest{
    private static DataSource ds;
    private static Connection con;

    @BeforeAll
    static void initDataSource() throws SQLException {
        // this way we do not need to create a new datasource every time
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        UserDaoIntegrationTest.ds = ds;
        UserDaoIntegrationTest.con = ds.getConnection();
    }

    @Test
    void getUserById(){
        // this data comes from the test migration files
        final String username = "test";
        final UserId userId = UserId.of(1);

        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(Integer.toString(userId.getId()));

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
        assertThat(maybeUser.get().getId()).isEqualTo(userId);
    }

    @Test
    void getInvalidUser(){
        UserDao userDao = new UserDao(ds);
        Optional<User> badUser = userDao.get("999");

        assertThat(badUser).isNotPresent();
    }

    @Test
    void getCorrectLoginInfo(){
        final String user = "test";
        final UserId userId = UserId.of(1);
        final String password = "$argon2i$v=19$m=16,t=2,p=1$MTIzNDU2Nzg5MDEyMzQ1NjA$LmFqTZeUWwqsnbZCS2E8XQ";

        UserDao userDao = new UserDao(ds);
        Optional<LoginInfo> maybeLogin = userDao.getLoginInfo(user);

        assertThat(maybeLogin).isPresent();
        assertThat(maybeLogin.get().getUserId()).isEqualTo(userId);
        assertThat(maybeLogin.get().getPasswordHash()).isEqualTo(password);
    }

    @ParameterizedTest
    @ValueSource(strings = {"badTest"})
    @EmptySource
    @NullSource
    void getIncorrectLoginInfo(String incorrectUser){
        UserDao userDao = new UserDao(ds);
        Optional<LoginInfo> maybeLogin = userDao.getLoginInfo(incorrectUser);

        assertThat(maybeLogin).isNotPresent();
    }

    @Test
    void registerUser(){
        final String user = "registertest";
        final String realName = "Bosse";
        final String password = "password";
        UserDao userDao = new UserDao(ds);

        userDao.register(user, realName, password);
        assertThat(userDao.getLoginInfo(user)).isPresent();
    }

    @Test
    void getException() {
        final String user = "registertest";
        final String realName = "Bosse";
        final String password = "password";
        UserDao userDao = new UserDao(ds);
        when(userDao.register(user, realName, password)).thenAnswer(i -> {throw new SQLException();
        });
        assertThat(userDao.register(user, realName, password)).isFalse();
    }

    @Test
    void checkAvailableName(){
        final String user = "newname";
        UserDao userDao = new UserDao(ds);

        assertThat(userDao.isNameAvailable(user)).isTrue();

    }

    @Test
    void checkUnavailableName(){
        final String user = "test";
        UserDao userDao = new UserDao(ds);

        assertThat(userDao.isNameAvailable(user)).isFalse();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void checkInvalidName(String name){
        UserDao userDao = new UserDao(ds);

        assertThat(userDao.isNameAvailable(name)).isFalse();
    }
}
