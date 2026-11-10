package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import se.yrgo.libraryapp.validators.Username;

public class RealNameTest {
    @ParameterizedTest
    @ValueSource(strings = {"David", "John Applesauce", "Jane Doe", "Mr Steve"})
    void junitValidNamesTest(String validName){
        Boolean result = RealName.validate(validName);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"John Bad", "Dark McDuff", "Patrick Stupid"})
    void junitInvalidNamesTest(String invalidName){
        Boolean result = RealName.validate(invalidName);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"John Badly", "Darkington McDuff", "Patrick Stupidhead"})
    void junitQuestionableNamesTest(String invalidName){
        Boolean result = RealName.validate(invalidName);
        assertThat(result).isFalse();
    }

}
