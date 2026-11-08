package com.daa;

import com.daa.stringsearch.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class StringSearchTest {
    private final NaiveSearch naive = new NaiveSearch();
    private final KmpSearch kmp = new KmpSearch();

    @Test
    @DisplayName("Verify LPS reference arrays from assignment specification")
    void testLpsReferenceValues() {
        assertArrayEquals(new int[]{0, 1, 2, 0, 1, 2, 3, 3}, kmp.lps("AAACAAAA"));
        assertArrayEquals(new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4}, kmp.lps("ABABCABAB"));
    }

    @Test
    @DisplayName("Verify search reference occurrences including overlaps")
    void testSearchReferenceOccurrences() {
        assertEquals(List.of(10), naive.search("ABABDABACDABABCABAB", "ABABCABAB"));
        assertEquals(List.of(10), kmp.search("ABABDABACDABABCABAB", "ABABCABAB"));

        assertEquals(List.of(0, 1, 2, 3), naive.search("AAAAA", "AA"));
        assertEquals(List.of(0, 1, 2, 3), kmp.search("AAAAA", "AA"));

        assertEquals(List.of(0, 2, 4), naive.search("ABABABA", "ABA"));
        assertEquals(List.of(0, 2, 4), kmp.search("ABABABA", "ABA"));
    }

    @Test
    @DisplayName("Edge cases: empty pattern throws, pattern longer than text returns empty")
    void testEdgeCases() {
        assertThrows(IllegalArgumentException.class, () -> naive.search("Hello", ""));
        assertThrows(IllegalArgumentException.class, () -> kmp.search("Hello", ""));
        assertThrows(IllegalArgumentException.class, () -> kmp.lps(""));

        assertTrue(naive.search("short", "longer_pattern_here").isEmpty());
        assertTrue(kmp.search("short", "longer_pattern_here").isEmpty());
    }

    @Test
    @DisplayName("Cross-check Naive and KMP on 100 random texts")
    void testCrossCheck100RandomTexts() {
        Random rnd = new Random(101);
        String alpha = "ABCD";

        for (int t = 0; t < 100; t++) {
            int tLen = 50 + rnd.nextInt(50);
            int pLen = 2 + rnd.nextInt(4);

            StringBuilder text = new StringBuilder();
            for (int i = 0; i < tLen; i++) text.append(alpha.charAt(rnd.nextInt(alpha.length())));
            StringBuilder pattern = new StringBuilder();
            for (int i = 0; i < pLen; i++) pattern.append(alpha.charAt(rnd.nextInt(alpha.length())));

            List<Integer> naiveMatches = naive.search(text.toString(), pattern.toString());
            List<Integer> kmpMatches = kmp.search(text.toString(), pattern.toString());

            assertEquals(naiveMatches, kmpMatches, "Mismatch on text: " + text + ", pattern: " + pattern);
        }
    }
}