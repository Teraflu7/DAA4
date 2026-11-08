package com.daa.stringsearch;

import java.util.List;

public interface StringSearch {
    List<Integer> search(String text, String pattern);
    long getCharComparisons();
    void resetCounter();
}