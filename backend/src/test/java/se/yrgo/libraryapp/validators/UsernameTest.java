package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import se.yrgo.libraryapp.validators.Username;

public class UsernameTest {
    @Test
    void junitCorrectUsername() {
        Username validator = new Username();
        assertThat(validator.validate("bosse")).isTrue();
    }

    @Test
    void junitIncorrectUsername() {
        Username validator = new Username();
        assertThat(validator.validate("name with space")).isFalse();
    }
}
