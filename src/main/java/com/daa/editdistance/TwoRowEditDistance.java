package com.daa.editdistance;

public class TwoRowEditDistance implements EditDistance {
    private long operationCount = 0;

    @Override
    public int distance(String a, String b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Input strings cannot be null");
        }

        int n = a.length();
        int m = b.length();

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            prev[j] = j;
            operationCount++;
        }

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            operationCount++;
            char ca = a.charAt(i - 1);

            for (int j = 1; j <= m; j++) {
                operationCount++;
                char cb = b.charAt(j - 1);
                if (ca == cb) {
                    curr[j] = prev[j - 1];
                } else {
                    int deleteOp = prev[j];
                    int insertOp = curr[j - 1];
                    int replaceOp = prev[j - 1];
                    curr[j] = 1 + Math.min(deleteOp, Math.min(insertOp, replaceOp));
                }
            }

            System.arraycopy(curr, 0, prev, 0, m + 1);
        }

        return prev[m];
    }

    @Override
    public long getOperationCount() {
        return operationCount;
    }

    @Override
    public void resetCounter() {
        operationCount = 0;
    }
}