package se.yrgo.libraryapp.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import se.yrgo.libraryapp.entities.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tag("integration")
@H2
public class RoleDaoIntegrationTest {
    private static DataSource ds;
    private static Connection con;

    @BeforeAll
    static void initDataSource() throws SQLException {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        RoleDaoIntegrationTest.ds = ds;
        RoleDaoIntegrationTest.con = ds.getConnection();
    }

    @Test
    void getCorrectRoles() {
        RoleDao roleDao = new RoleDao(ds);
        UserId user = new UserId(1);
        List<Role> roles = roleDao.get(user);
        assertThat(roles).isNotEmpty();
        assertThat(roles).contains(Role.ADMIN);
    }

    @Test
    void getIncorrectRoles() {
        RoleDao roleDao = new RoleDao(ds);
        UserId user = new UserId(99);
        List<Role> roles = roleDao.get(user);
        assertThat(roles).isEmpty();
    }
}
