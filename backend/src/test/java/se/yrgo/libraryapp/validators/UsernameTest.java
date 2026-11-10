package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import se.yrgo.libraryapp.validators.Username;

public class UsernameTest {
    @ParameterizedTest
    @ValueSource(strings = {"bosse", "bosse@", "bos_se", "bos.se", "bos-se", "bosssssssssssssssse", "bosse0"})
    void junitCorrectUsernames(String validUser) {
        Boolean result = Username.validate(validUser);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"name with space", "shr", "bos/se", "bos$e", "bossé", "niño"})
    @EmptySource
    void junitIncorrectUsernames(String invalidUser) {
        Boolean result = Username.validate(invalidUser);
        assertThat(result).isFalse();
    }
}
