package se.yrgo.libraryapp.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import javax.security.auth.login.CredentialException;
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
import org.pac4j.core.exception.CredentialsException;
import se.yrgo.libraryapp.entities.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tag("integration")
@H2
public class SessionDaoIntegrationTest {
    private static DataSource ds;
    private static Connection con;

    @BeforeAll
    static void initDataSource() throws SQLException {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        SessionDaoIntegrationTest.ds = ds;
        SessionDaoIntegrationTest.con = ds.getConnection();
    }

    @Test
    void createCorrectUser(){
        SessionDao sessionDao = new SessionDao(ds);
        UserId userId = new UserId(1);
        UUID uuid;
        uuid = sessionDao.create(userId);
        assertThat(uuid).isNotNull();
    }

    @Test
    void createIncorrectUser(){
        SessionDao sessionDao = new SessionDao(ds);
        UserId userId = new UserId(999);
        assertThatThrownBy(() -> sessionDao.create(userId)).isInstanceOf(CredentialsException.class);
    }

    @Test
    void deleteSession(){
        SessionDao sessionDao = new SessionDao(ds);
        UserId userId = new UserId(2);
        UUID uuid = sessionDao.create(userId);
        sessionDao.delete(uuid);
        assertThatThrownBy(() -> sessionDao.validate(uuid)).isInstanceOf(CredentialsException.class);
    }

    @Test
    void validateCorrectSession(){
        SessionDao sessionDao = new SessionDao(ds);
        UserId userId = new UserId(2);
        UUID uuid = sessionDao.create(userId);
        UserId validatedUserId = sessionDao.validate(uuid);
        assertThat(validatedUserId).isEqualTo(userId);
    }

    @Test
    void validateIncorrectSession() {
        SessionDao sessionDao = new SessionDao(ds);
        UUID uuid = UUID.randomUUID();
        assertThatThrownBy(() -> sessionDao.validate(uuid)).isInstanceOf(CredentialsException.class);
    }
}
