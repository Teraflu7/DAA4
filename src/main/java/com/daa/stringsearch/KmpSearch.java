package com.daa.stringsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KmpSearch implements StringSearch {
    private long charComparisons = 0;

    public int[] lps(String p) {
        if (p == null || p.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }
        int m = p.length();
        int[] pi = new int[m];
        int len = 0;
        int i = 1;

        while (i < m) {
            charComparisons++;
            if (p.charAt(i) == p.charAt(len)) {
                len++;
                pi[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = pi[len - 1];
                } else {
                    pi[i] = 0;
                    i++;
                }
            }
        }
        return pi;
    }

    @Override
    public List<Integer> search(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }
        if (text == null || pattern.length() > text.length()) {
            return Collections.emptyList();
        }

        List<Integer> occurrences = new ArrayList<>();
        int[] pi = lps(pattern);

        int n = text.length();
        int m = pattern.length();
        int i = 0; // Pointer in text (never decrements)
        int j = 0; // Pointer in pattern

        while (i < n) {
            charComparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                occurrences.add(i - j);
                j = pi[j - 1];
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = pi[j - 1];
                } else {
                    i++;
                }
            }
        }

        return occurrences;
    }

    @Override
    public long getCharComparisons() {
        return charComparisons;
    }

    @Override
    public void resetCounter() {
        charComparisons = 0;
    }
}