package Dynamic;

import java.util.Scanner;

class Program5A {
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}

    // Helper class to store memoization state
    private static class MemoState {
        int totalHeight;        // Optimal height for this subproblem
        int numPlatforms;       // Number of platforms used
        int numPaintings;       // Number of paintings on first platform

        MemoState(int height, int platforms, int paintings) {
            totalHeight = height;
            numPlatforms = platforms;
            numPaintings = paintings;
        }
    }

    private static Result program5A(int n, int w, int[] heights, int[] widths) {
        // Precompute max consecutive sculptures and their heights for each starting position
        int[] maxConsecutive = new int[n];  // Number of sculptures that fit
        int[] groupHeights = new int[n];    // Max height of the group

        // Compute for each starting position
        for (int start = 0; start < n; start++) {
            int currentWidth = 0;
            int maxHeight = 0;
            int count = 0;

            for (int i = start; i < n && currentWidth + widths[i] <= w; i++) {
                currentWidth += widths[i];
                maxHeight = Math.max(maxHeight, heights[i]);
                count++;
            }

            maxConsecutive[start] = count;
            groupHeights[start] = maxHeight;
        }

        // Create memoization table
        MemoState[][] memo = new MemoState[n][n];

        // Get optimal solution starting from beginning
        MemoState finalState = solveDP(0, n-1, maxConsecutive, groupHeights, memo);

        // Reconstruct solution
        int[] numPaintings = reconstructSolution(0, n-1, memo);

        return new Result(finalState.numPlatforms, finalState.totalHeight, numPaintings);
    }

    private static MemoState solveDP(int start, int end, int[] maxConsecutive, int[] groupHeights, MemoState[][] memo) {
        // Base case: empty sequence
        if (start > end) {
            return new MemoState(0, 0, 0);
        }

        // Check if already solved
        if (memo[start][end] != null) {
            return memo[start][end];
        }

        // Get precomputed values for this start position
        int numOnPlatform = Math.min(maxConsecutive[start], end - start + 1);
        int platformHeight = groupHeights[start];

        // If all sculptures fit on one platform
        if (start + numOnPlatform > end) {
            memo[start][end] = new MemoState(platformHeight, 1, numOnPlatform);
            return memo[start][end];
        }

        // Split after the maximum number that can fit on first platform
        MemoState nextPlatform = solveDP(start + numOnPlatform, end, maxConsecutive, groupHeights, memo);

        memo[start][end] = new MemoState(
                platformHeight + nextPlatform.totalHeight,
                1 + nextPlatform.numPlatforms,
                numOnPlatform
        );

        return memo[start][end];
    }

    private static int[] reconstructSolution(int start, int end, MemoState[][] memo) {
        if (start > end) {
            return new int[0];
        }

        MemoState state = memo[start][end];
        if (state.numPlatforms == 1) {
            return new int[]{state.numPaintings};
        }

        int[] restSolution = reconstructSolution(start + state.numPaintings, end, memo);

        int[] result = new int[restSolution.length + 1];
        result[0] = state.numPaintings;
        for (int i = 0; i < restSolution.length; i++) {
            result[i + 1] = restSolution[i];
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