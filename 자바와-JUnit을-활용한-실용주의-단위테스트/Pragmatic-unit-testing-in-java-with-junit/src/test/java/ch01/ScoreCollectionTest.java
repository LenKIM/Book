package ch01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


class ScoreCollectionTest {

    @Test
    void name() {
        // Arrange
        ScoreCollection collection = new ScoreCollection();
        collection.add(() -> 5);
        collection.add(() -> 7);
        // Act
        int actual = collection.arithmeticMean();

        // Assert
        assertThat(actual, equalTo(6));
    }
}