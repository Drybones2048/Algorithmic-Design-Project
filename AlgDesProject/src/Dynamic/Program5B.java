package Dynamic;

import java.util.Scanner;

class Program5B {
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}

    // Helper class to store DP state
    private static class DPState {
        int totalHeight;        // Optimal height for this subproblem
        int numPlatforms;       // Number of platforms used
        int lastSplit;          // Last split point for reconstruction

        DPState(int height, int platforms, int split) {
            totalHeight = height;
            numPlatforms = platforms;
            lastSplit = split;
        }
    }

    private static Result program5B(int n, int w, int[] heights, int[] widths) {
        // Create DP table for storing optimal solutions for subproblems
        DPState[][] dp = new DPState[n][n];

        // Initialize dp table for single sculpture cases
        for (int i = 0; i < n; i++) {
            dp[i][i] = new DPState(heights[i], 1, i);
        }

        // Fill dp table bottom-up
        for (int len = 2; len <= n; len++) {
            for (int start = 0; start <= n - len; start++) {
                int end = start + len - 1;

                // Try placing all sculptures from start to end on one platform
                int currentWidth = 0;
                int maxHeight = 0;
                boolean canFitOnePlatform = true;

                for (int i = start; i <= end; i++) {
                    currentWidth += widths[i];
                    maxHeight = Math.max(maxHeight, heights[i]);
                    if (currentWidth > w) {
                        canFitOnePlatform = false;
                        break;
                    }
                }

                if (canFitOnePlatform) {
                    dp[start][end] = new DPState(maxHeight, 1, end);
                    continue;
                }

                // Try all possible splits
                int minTotalHeight = Integer.MAX_VALUE;
                int bestNumPlatforms = 0;
                int bestSplit = -1;

                for (int split = start; split < end; split++) {
                    DPState leftResult = dp[start][split];
                    DPState rightResult = dp[split + 1][end];

                    int totalHeight = leftResult.totalHeight + rightResult.totalHeight;

                    if (totalHeight < minTotalHeight) {
                        minTotalHeight = totalHeight;
                        bestNumPlatforms = leftResult.numPlatforms + rightResult.numPlatforms;
                        bestSplit = split;
                    }
                }

                dp[start][end] = new DPState(minTotalHeight, bestNumPlatforms, bestSplit);
            }
        }

        // Reconstruct solution to get paintings per platform
        int[] numPaintings = reconstructSolution(0, n-1, dp);

        return new Result(dp[0][n-1].numPlatforms, dp[0][n-1].totalHeight, numPaintings);
    }

    // Helper function to reconstruct the solution
    private static int[] reconstructSolution(int start, int end, DPState[][] dp) {
        if (start > end) {
            return new int[0];
        }

        DPState state = dp[start][end];
        if (state.lastSplit == end) {
            // All sculptures on one platform
            return new int[]{end - start + 1};
        }

        // Combine solutions from left and right sides
        int[] leftSolution = reconstructSolution(start, state.lastSplit, dp);
        int[] rightSolution = reconstructSolution(state.lastSplit + 1, end, dp);

        // Combine the solutions
        int[] result = new int[leftSolution.length + rightSolution.length];
        for (int i = 0; i < leftSolution.length; i++) {
            result[i] = leftSolution[i];
        }
        for (int i = 0; i < rightSolution.length; i++) {
            result[leftSolution.length + i] = rightSolution[i];
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int W = sc.nextInt();
        int[] heights = new int[n];
        int[] widths = new int[n];
        for(int i=0; i<n; i++){
            heights[i] = sc.nextInt();
        }
        for(int i=0; i<n; i++){
            widths[i] = sc.nextInt();
        }
        sc.close();
        Result result = program5B(n, W, heights, widths);
        System.out.println(result.numPlatforms);
        System.out.println(result.totalHeight);
        for(int i=0; i<result.numPaintings.length; i++){
            System.out.println(result.numPaintings[i]);
        }
    }
}