package Dynamic;

import java.util.Scanner;
import java.util.Arrays;

class Program5A {
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}

    // Helper class to store memoization state
    private static class MemoState {
        int totalHeight;        // Optimal height for this subproblem
        int numPlatforms;       // Number of platforms used
        int splitPosition;      // Where to split for this optimal solution

        MemoState(int height, int platforms, int split) {
            totalHeight = height;
            numPlatforms = platforms;
            splitPosition = split;
        }
    }

    private static Result program5A(int n, int w, int[] heights, int[] widths) {
        // Create memoization table
        MemoState[][] memo = new MemoState[n][n];

        // Get optimal solution starting from beginning
        MemoState finalState = solveDP(0, n-1, w, heights, widths, memo);

        // Reconstruct solution to get paintings per platform
        int[] numPaintings = reconstructSolution(0, n-1, memo);

        return new Result(finalState.numPlatforms, finalState.totalHeight, numPaintings);
    }

    // Recursive function to solve subproblems with memoization
    private static MemoState solveDP(int start, int end, int w, int[] heights, int[] widths, MemoState[][] memo) {
        // Base case: empty sequence
        if (start > end) {
            return new MemoState(0, 0, -1);
        }

        // Check if already solved
        if (memo[start][end] != null) {
            return memo[start][end];
        }

        // Try placing all sculptures from start to end on one platform
        int currentWidth = 0;
        int maxHeight = 0;
        boolean canFitOnePlatform = true;

        // Check if all sculptures fit on one platform
        for (int i = start; i <= end; i++) {
            currentWidth += widths[i];
            maxHeight = Math.max(maxHeight, heights[i]);
            if (currentWidth > w) {
                canFitOnePlatform = false;
                break;
            }
        }

        // If all fit on one platform, this is one possible solution
        if (canFitOnePlatform) {
            memo[start][end] = new MemoState(maxHeight, 1, end);
            return memo[start][end];
        }

        // Try all possible splits
        int minTotalHeight = Integer.MAX_VALUE;
        int bestNumPlatforms = 0;
        int bestSplit = -1;

        for (int split = start; split < end; split++) {
            MemoState leftResult = solveDP(start, split, w, heights, widths, memo);
            MemoState rightResult = solveDP(split + 1, end, w, heights, widths, memo);

            int totalHeight = leftResult.totalHeight + rightResult.totalHeight;

            if (totalHeight < minTotalHeight) {
                minTotalHeight = totalHeight;
                bestNumPlatforms = leftResult.numPlatforms + rightResult.numPlatforms;
                bestSplit = split;
            }
        }

        memo[start][end] = new MemoState(minTotalHeight, bestNumPlatforms, bestSplit);
        return memo[start][end];
    }

    // Helper function to reconstruct the solution
    private static int[] reconstructSolution(int start, int end, MemoState[][] memo) {
        if (start > end) {
            return new int[0];
        }

        MemoState state = memo[start][end];
        if (state.splitPosition == end) {
            // All sculptures on one platform
            return new int[]{end - start + 1};
        }

        // Combine solutions from left and right sides
        int[] leftSolution = reconstructSolution(start, state.splitPosition, memo);
        int[] rightSolution = reconstructSolution(state.splitPosition + 1, end, memo);

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
        Result result = program5A(n, W, heights, widths);
        System.out.println(result.numPlatforms);
        System.out.println(result.totalHeight);
        for(int i=0; i<result.numPaintings.length; i++){
            System.out.println(result.numPaintings[i]);
        }
    }
}