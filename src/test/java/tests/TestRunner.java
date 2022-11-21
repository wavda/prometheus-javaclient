package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public class TestRunner extends BaseTest {
    @Test
    @DisplayName("Navigate to Google")
    @Tags(value = {@Tag("regression"), @Tag("smoke")})
    void navigateToGoogle() {
        driver.get("https://www.google.com");
        assertTrue(driver.getTitle().contains("Google"));
    }

    @Test
    @DisplayName("Navigate to Wikipedia")
    @Tags(value = {@Tag("regression"), @Tag("smoke")})
    void navigateToWikipedia() {
        driver.get("https://www.wikipedia.org");
        assertTrue(driver.getTitle().contains("Wikipedia"));
    }
}
