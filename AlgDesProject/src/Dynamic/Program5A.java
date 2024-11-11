import java.util.Scanner;

class Program5A {
    public record Result(int numShelves, int totalHeight, int[] numPaintings) {}

    // Similarly to the last milestone, we will be implementing a helper class to store the state of the shelves and paintings
    private static class MemoState {
        int totalHeight;        // Optimal height for this subproblem
        int numShelves;       // Number of shelves used
        int numPaintings;       // Number of paintings on first shelf

        // Constructor for our class MemoState
        MemoState(int height, int shelves, int paintings) {
            totalHeight = height;
            numShelves = shelves;
            numPaintings = paintings;
        }
    }

    /**
     * Solution to program 5A
     * @param n number of paintings
     * @param w width of the shelf
     * @param heights array of heights of the paintings
     * @param widths array of widths of the paintings
     * @return Result object containing the number of shelves, total height of the
    paintings and the number of paintings on each shelf
     */
    private static Result program5A(int n, int w, int[] heights, int[] widths) {
        int[] maxConsecutive = new int[n];  // Max number of paintings that can fit starting from each position
        int[] groupHeights = new int[n];    // Tallest painting in each group starting from each position
        // These values will save us computational time later

        // This loop calculates the max paintings and heights for each starting position
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

        // Create memoization table to store the results of previous runs (as learned in class)
        MemoState[][] memo = new MemoState[n][n];

        // Creates a variable of the MemoState object type, and will use the function solveDP to find the optimal painting arrangement
        MemoState finalState = solveDP(0, n-1, maxConsecutive, groupHeights, memo);

        // Reconstruct solution to get paintings per shelf
        int[] numPaintings = reconstructSolution(0, n-1, memo);

        return new Result(finalState.numShelves, finalState.totalHeight, numPaintings);
    }

    private static MemoState solveDP(int start, int end, int[] maxConsecutive, int[] groupHeights, MemoState[][] memo) {
        // Base case, reaches this point if we have reached the end of the paintings, so the total height and number of shelves is zero
        if (start > end) {
            return new MemoState(0, 0, 0);
        }

        // Memoization being utilized, if we already have a previously calculated solution, return said previous solution
        if (memo[start][end] != null) {
            return memo[start][end];
        }

        // Retrieve the precomputed values for the starting positions
        // These are the number of paintings that fit on the current shelf
        int numOnShelf = Math.min(maxConsecutive[start], end - start + 1);
        // This is the height of the shelf for these paintings
        int shelfHeight = groupHeights[start];

        // Can all the paintings fit on one shelf?
        if (start + numOnShelf > end) {
            // If all the paintings do, store it in memo and return it
            memo[start][end] = new MemoState(shelfHeight, 1, numOnShelf);
            return memo[start][end];
        }

        // If the paintings don't all fit, we place the remaining paintings on a new shelf and solve w/ recursion
        MemoState nextShelf = solveDP(start + numOnShelf, end, maxConsecutive, groupHeights, memo);

        memo[start][end] = new MemoState(
                shelfHeight + nextShelf.totalHeight,
                1 + nextShelf.numShelves,
                numOnShelf
        );

        return memo[start][end];
    }

    private static int[] reconstructSolution(int start, int end, MemoState[][] memo) {
        // Base case: if there are no paintings, return it as empty
        if (start > end) {
            return new int[0];
        }

        // Another base case: if there's only one shelf, just return that
        MemoState state = memo[start][end];
        if (state.numShelves == 1) {
            return new int[]{state.numPaintings};
        }

        // Add the number of paintings together on the current shelf
        // Recursively construct the rest of the solution
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
        System.out.println(result.numShelves);
        System.out.println(result.totalHeight);
        for(int i=0; i<result.numPaintings.length; i++){
            System.out.println(result.numPaintings[i]);
        }
    }
}