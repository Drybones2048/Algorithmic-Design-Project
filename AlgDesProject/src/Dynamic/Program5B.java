package Dynamic;

import java.util.Scanner;

class Program5B {
    public record Result(int numShelves, int totalHeight, int[] numPaintings) {}

    // Once again, we will be implementing a helper class to store the state of the shelves and paintings
    private static class MemoState {
        int totalHeight;        // Optimal height for this subproblem
        int numShelves;       // Number of shelves used
        int lastSplit;          // Last split point for reconstruction

        MemoState(int height, int shelves, int split) {
            totalHeight = height;
            numShelves = shelves;
            lastSplit = split;
        }
    }
    /**
     * Solution to program 5B
     * @param n number of paintings
     * @param w width of the platform
     * @param heights array of heights of the paintings
     * @param widths array of widths of the paintings
     * @return Result object containing the number of platforms, total height of the
    paintings and the number of paintings on each platform
     */
    private static Result program5B(int n, int w, int[] heights, int[] widths) {
        // Create memoization table to store the results of previous runs (as learned in class)
        MemoState[][] memo = new MemoState[n][n];

        // Initialize memo table for single sculpture cases
        for (int i = 0; i < n; i++) {
            memo[i][i] = new MemoState(heights[i], 1, i);
        }

        // Fill memo table bottom-up
        for (int len = 2; len <= n; len++) {
            for (int start = 0; start <= n - len; start++) {
                int end = start + len - 1;

                // Try placing all sculptures from start to end on one shelf
                int currentWidth = 0;
                int maxHeight = 0;
                boolean canFitOneShelf = true;

                for (int i = start; i <= end; i++) {
                    currentWidth += widths[i];
                    maxHeight = Math.max(maxHeight, heights[i]);
                    if (currentWidth > w) {
                        canFitOneShelf = false;
                        break;
                    }
                }

                if (canFitOneShelf) {
                    memo[start][end] = new MemoState(maxHeight, 1, end);
                    continue;
                }

                // Since the rest of the paintings are not able to fit on one shelf, there must be a split somewhere, and we have to find the ideal place to do so
                int minTotalHeight = 99999; // really large height constraint as default
                int bestNumShelves = 0; // ideal number of shelves for split
                int bestSplit = -1; // index of the best split location; -1 for none

                for (int split = start; split < end; split++) {
                    MemoState leftResult = memo[start][split];
                    MemoState rightResult = memo[split + 1][end];

                    int totalHeight = leftResult.totalHeight + rightResult.totalHeight;

                    if (totalHeight < minTotalHeight) {
                        minTotalHeight = totalHeight;
                        bestNumShelves = leftResult.numShelves + rightResult.numShelves;
                        bestSplit = split;
                    }
                }

                memo[start][end] = new MemoState(minTotalHeight, bestNumShelves, bestSplit);
            }
        }

        // Reconstruct solution to get paintings per shelf
        int[] numPaintings = reconstructSolution(0, n-1, memo);

        return new Result(memo[0][n-1].numShelves, memo[0][n-1].totalHeight, numPaintings);
    }

    // Helper function to reconstruct the solution
    private static int[] reconstructSolution(int start, int end, MemoState[][] memo) {
        // Base case
        if (start > end) {
            return new int[0];
        }

        MemoState state = memo[start][end];
        if (state.lastSplit == end) {
            // All sculptures on one shelf
            return new int[]{end - start + 1};
        }

        // recursively calls the function to reconstruct the left and right sides of the solution. leftSolution will hold the number of paintings on each shelf for the left part,
        // and the rightSolution will hold the number of paintings on each shelf for the right part
        int[] leftSolution = reconstructSolution(start, state.lastSplit, memo);
        int[] rightSolution = reconstructSolution(state.lastSplit + 1, end, memo);

        // we create an array that will be a return value for the function. This array will hold the shelf counts for both the left and right halves
        int[] result = new int[leftSolution.length + rightSolution.length];

        // fill result with the shelf data from the left half
        for (int i = 0; i < leftSolution.length; i++) {
            result[i] = leftSolution[i];
        }

        // fill result with the shelf data from the right half
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
        System.out.println(result.numShelves);
        System.out.println(result.totalHeight);
        for(int i=0; i<result.numPaintings.length; i++){
            System.out.println(result.numPaintings[i]);
        }
    }
}