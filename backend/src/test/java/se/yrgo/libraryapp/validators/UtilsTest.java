package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import se.yrgo.libraryapp.validators.Username;

import java.util.regex.Pattern;

public class UtilsTest {
    @ParameterizedTest
    @ValueSource(strings = {"letters with space", "letters with space55", "letters with space@@", "LETTERS WITH SPACE"})
    void junitCorrectLettersAndWhiteSpace(String correctText){
        assertThat(Utils.onlyLettersAndWhitespace(correctText)).isEqualTo("letters with space").isLowerCase();
    }

    @ParameterizedTest
    @ValueSource(strings = {"l33t", "l01", "8455", "8455 l33t", "cool@dude"})
    void junitCleanLeetTest(String leetText){
        assertThat(Utils.cleanAndUnLeet(leetText)).matches("[a-z\\s]+");
    }
}
