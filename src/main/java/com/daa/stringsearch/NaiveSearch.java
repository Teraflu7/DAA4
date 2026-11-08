package com.daa.stringsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NaiveSearch implements StringSearch {
    private long charComparisons = 0;

    @Override
    public List<Integer> search(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }
        if (text == null || pattern.length() > text.length()) {
            return Collections.emptyList();
        }

        List<Integer> occurrences = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {
            boolean match = true;
            for (int j = 0; j < m; j++) {
                charComparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                occurrences.add(i);
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