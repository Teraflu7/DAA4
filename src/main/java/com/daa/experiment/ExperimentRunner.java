package com.daa.experiment;

import com.daa.editdistance.*;
import com.daa.stringsearch.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ExperimentRunner {
    private static final long SEED = 20261108L;
    private static final Random RANDOM = new Random(SEED);

    public static void main(String[] args) throws IOException {
        Files.createDirectories(Paths.get("results"));
        String csvPath = "results/results.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvPath))) {
            writer.println("family,algorithm,input_type,n,time_ms,counter_value");

            System.out.println("Running Edit Distance experiments...");
            runEditDistanceExperiments(writer);

            System.out.println("Running String Search experiments...");
            runStringSearchExperiments(writer);
        }
        System.out.println("All experiments finished! Data saved to " + csvPath);
    }

    private static void runEditDistanceExperiments(PrintWriter writer) {
        int[] recSizes = {8, 10, 12, 14};
        int[] dpSizes = {250, 500, 1000, 2000};
        String[] types = {"random", "worst_case_diff"};

        for (String type : types) {
            for (int n : recSizes) {
                String[] pair = generateEditDistancePair(n, type);
                benchmarkEditDistance(new RecursiveEditDistance(), "Recursive", type, n, pair[0], pair[1], writer);
            }
        }

        EditDistance tableED = new TableEditDistance();
        EditDistance twoRowED = new TwoRowEditDistance();

        for (String type : types) {
            for (int n : dpSizes) {
                String[] pair = generateEditDistancePair(n, type);
                benchmarkEditDistance(tableED, "Table", type, n, pair[0], pair[1], writer);
                benchmarkEditDistance(twoRowED, "TwoRow", type, n, pair[0], pair[1], writer);
            }
        }
    }

    private static void benchmarkEditDistance(EditDistance ed, String algName, String inputType, int n, String a, String b, PrintWriter writer) {
        // Warmup
        ed.resetCounter();
        ed.distance(a, b);

        List<Double> times = new ArrayList<>();
        long finalCounter = 0;

        for (int trial = 0; trial < 5; trial++) {
            ed.resetCounter();
            long start = System.nanoTime();
            ed.distance(a, b);
            long elapsed = System.nanoTime() - start;
            times.add(elapsed / 1e6);
            finalCounter = ed.getOperationCount();
        }

        Collections.sort(times);
        double medianTime = times.get(times.size() / 2);
        writer.printf(Locale.US, "EditDistance,%s,%s,%d,%.5f,%d%n", algName, inputType, n, medianTime, finalCounter);
    }

    private static void runStringSearchExperiments(PrintWriter writer) {
        int[] textSizes = {10000, 25000, 50000, 100000};
        int patternLen = 100;
        String[] types = {"random", "worst_case"};

        StringSearch naive = new NaiveSearch();
        StringSearch kmp = new KmpSearch();

        for (String type : types) {
            for (int n : textSizes) {
                String[] data = generateSearchPair(n, patternLen, type);
                String text = data[0];
                String pattern = data[1];

                benchmarkSearch(naive, "Naive", type, n, text, pattern, writer);
                benchmarkSearch(kmp, "KMP", type, n, text, pattern, writer);
            }
        }
    }

    private static void benchmarkSearch(StringSearch searcher, String algName, String inputType, int n, String text, String pattern, PrintWriter writer) {
        // Warmup
        searcher.resetCounter();
        searcher.search(text, pattern);

        List<Double> times = new ArrayList<>();
        long finalCounter = 0;

        for (int trial = 0; trial < 5; trial++) {
            searcher.resetCounter();
            long start = System.nanoTime();
            searcher.search(text, pattern);
            long elapsed = System.nanoTime() - start;
            times.add(elapsed / 1e6);
            finalCounter = searcher.getCharComparisons();
        }

        Collections.sort(times);
        double medianTime = times.get(times.size() / 2);
        writer.printf(Locale.US, "StringSearch,%s,%s,%d,%.5f,%d%n", algName, inputType, n, medianTime, finalCounter);
    }

    private static String[] generateEditDistancePair(int n, String type) {
        if ("worst_case_diff".equals(type)) {
            return new String[]{"A".repeat(n), "B".repeat(n)};
        }
        StringBuilder sb1 = new StringBuilder(n);
        StringBuilder sb2 = new StringBuilder(n);
        String alpha = "ACGT";
        for (int i = 0; i < n; i++) {
            sb1.append(alpha.charAt(RANDOM.nextInt(alpha.length())));
            sb2.append(alpha.charAt(RANDOM.nextInt(alpha.length())));
        }
        return new String[]{sb1.toString(), sb2.toString()};
    }

    private static String[] generateSearchPair(int n, int m, String type) {
        if ("worst_case".equals(type)) {
            String text = "A".repeat(n);
            String pattern = "A".repeat(m - 1) + "B";
            return new String[]{text, pattern};
        }
        StringBuilder textSb = new StringBuilder(n);
        StringBuilder patternSb = new StringBuilder(m);
        String alpha = "ACGT";
        for (int i = 0; i < n; i++) {
            textSb.append(alpha.charAt(RANDOM.nextInt(alpha.length())));
        }
        for (int i = 0; i < m; i++) {
            patternSb.append(alpha.charAt(RANDOM.nextInt(alpha.length())));
        }
        return new String[]{textSb.toString(), patternSb.toString()};
    }
}