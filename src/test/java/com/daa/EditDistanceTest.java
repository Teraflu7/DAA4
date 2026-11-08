package com.daa;

import com.daa.editdistance.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class EditDistanceTest {
    private final EditDistance rec = new RecursiveEditDistance();
    private final EditDistance table = new TableEditDistance();
    private final EditDistance twoRow = new TwoRowEditDistance();

    @Test
    @DisplayName("Verify reference values from assignment specification")
    void testReferenceValues() {
        assertEquals(3, table.distance("kitten", "sitting"));
        assertEquals(2, table.distance("flaw", "lawn"));
        assertEquals(5, table.distance("intention", "execution"));
        assertEquals(3, table.distance("", "abc"));
        assertEquals(0, table.distance("abc", "abc"));

        assertEquals(3, twoRow.distance("kitten", "sitting"));
        assertEquals(2, twoRow.distance("flaw", "lawn"));
        assertEquals(5, twoRow.distance("intention", "execution"));
        assertEquals(3, twoRow.distance("", "abc"));
        assertEquals(0, twoRow.distance("abc", "abc"));

        assertEquals(3, rec.distance("kitten", "sitting"));
        assertEquals(2, rec.distance("flaw", "lawn"));
    }

    @Test
    @DisplayName("All 3 versions return identical results on 100 random short strings")
    void testThreeVersionsAgreementOn100Strings() {
        Random rnd = new Random(42);
        String alphabet = "abcdef";

        for (int t = 0; t < 100; t++) {
            int len1 = rnd.nextInt(9); // Short length to allow recursion to finish
            int len2 = rnd.nextInt(9);

            StringBuilder s1 = new StringBuilder();
            for (int i = 0; i < len1; i++) s1.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
            StringBuilder s2 = new StringBuilder();
            for (int i = 0; i < len2; i++) s2.append(alphabet.charAt(rnd.nextInt(alphabet.length())));

            String a = s1.toString();
            String b = s2.toString();

            int rRes = rec.distance(a, b);
            int tRes = table.distance(a, b);
            int rowRes = twoRow.distance(a, b);

            assertEquals(tRes, rRes, "Recursive and Table mismatch on: " + a + ", " + b);
            assertEquals(tRes, rowRes, "Table and TwoRow mismatch on: " + a + ", " + b);
        }
    }
}