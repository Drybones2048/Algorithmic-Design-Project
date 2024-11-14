// COP4533 Milestone 2
// Anthony Rumore, Dylan Harle

import java.util.Scanner;

class Program5A {
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}

    // Similarly to the last milestone, we will be implementing a helper class to store the state of the shelves and paintings
    private static class MemoState {
        int minHeight;        // Minimum total height achieved for this state
        int numPaintings;     // Number of paintings on the current shelf
        // Use a constructor for the MemoState object
        MemoState(int height, int numPaintings) {
            this.minHeight = height;
            this.numPaintings = numPaintings;
        }
    }

    /**
     * Solution to program 5A
     * @param n number of paintings
     * @param w width of the platform
     * @param heights array of heights of the paintings
     * @param widths array of widths of the paintings
     * @return Result object containing the number of platforms, total height of the paintings and the number of paintings on each platform
     */
    private static Result program5A(int n, int w, int[] heights, int[] widths) {
        // Create memoization table to store the results of previous runs (as learned in class)
        // dp[i] will store the optimal solution for paintings 0...i
        MemoState[] memo = new MemoState[n];

        // We will use an array to track how many paintings are on each shelf in the optimal solution
        int[] shelfSizes = new int[n];

        // Initialize first painting case - it must be alone on its shelf
        memo[0] = new MemoState(heights[0], 1);
        shelfSizes[0] = 1;

        // Solve for each painting recursively with memoization
        MemoState finalState = solveDPRecursive(n-1, w, heights, widths, memo, shelfSizes);

        // Count total number of shelves and reconstruct solution
        int totalShelves = 0;
        int currentPaintingIndex = n-1;

        // Count how many shelves we need by walking backwards through the solution
        while (currentPaintingIndex >= 0) {
            totalShelves++;
            currentPaintingIndex -= shelfSizes[currentPaintingIndex];
        }

        // Result will be the number of paintings on each shelf
        int[] result = new int[totalShelves];

        // Fill the result array by walking backwards through the solution again
        currentPaintingIndex = n-1;
        int currShelfIndex = totalShelves - 1;

        while (currentPaintingIndex >= 0) {
            result[currShelfIndex] = shelfSizes[currentPaintingIndex];
            currentPaintingIndex -= shelfSizes[currentPaintingIndex];
            currShelfIndex--;
        }

        return new Result(totalShelves, finalState.minHeight, result);
    }

    // solveDPRecursive is a recursive function that will solve all subproblems using memoization
    private static MemoState solveDPRecursive(int currentPainting, int w, int[] heights, int[] widths,
                                              MemoState[] memo, int[] shelfSizes) {
        // Base case: if we're at painting 0, it must be alone on its shelf
        if (currentPainting == 0) {
            shelfSizes[0] = 1;
            return new MemoState(heights[0], 1);
        }

        // Memoization check: if we've solved this before, return the cached result
        if (memo[currentPainting] != null) {
            return memo[currentPainting];
        }

        // Initial solution: put current painting on new shelf above previous solution
        // First, recursively solve for paintings before current
        MemoState prevSolution = solveDPRecursive(currentPainting-1, w, heights, widths, memo, shelfSizes);
        int minTotalHeight = prevSolution.minHeight + heights[currentPainting];
        shelfSizes[currentPainting] = 1;

        // Try combining current painting with previous paintings
        int currentWidth = widths[currentPainting];
        int maxHeight = heights[currentPainting];
        int previousPainting = currentPainting - 1;
        int bestNumPaintings = 1;

        // While we can still add previous paintings to current shelf
        while (previousPainting >= 0 && currentWidth + widths[previousPainting] <= w) {
            currentWidth += widths[previousPainting];
            maxHeight = Math.max(maxHeight, heights[previousPainting]);

            // Get optimal solution for paintings before this shelf
            int prevHeight = 0;
            if (previousPainting > 0) {
                MemoState prevState = solveDPRecursive(previousPainting-1, w, heights, widths, memo, shelfSizes);
                prevHeight = prevState.minHeight;
            }

            // Is this configuration better?
            int totalHeight = prevHeight + maxHeight;
            if (totalHeight < minTotalHeight) {
                minTotalHeight = totalHeight;
                bestNumPaintings = currentPainting - previousPainting + 1;
                shelfSizes[currentPainting] = bestNumPaintings;
            }

            previousPainting--;
        }

        // Store and return the best solution found
        memo[currentPainting] = new MemoState(minTotalHeight, bestNumPaintings);
        return memo[currentPainting];
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