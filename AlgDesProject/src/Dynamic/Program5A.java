import java.util.Scanner;
import java.util.Arrays;
class Program5A {
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}
    //Similarly to the last milestone, we will be implementing a helper class to store the state of the shelves and paintings
    private static class MemoState {
        int totalHeight;        // Total cost of the problem
        int numShelves;         // Number of shelves used
        int splitPosition;      // Stores the point that tells us how to split the paintings. Remembers previous solutions to reduce calculation time
        //Use a constructor for the MemoState object
        MemoState(int height, int shelves, int split) {
            totalHeight = height;
            numShelves = shelves;
            splitPosition = split;
        }
    }
    /**
     * Solution to program 5A
     * @param n number of paintings
     * @param w width of the platform
     * @param heights array of heights of the paintings
     * @param widths array of widths of the paintings
     * @return Result object containing the number of platforms, total height of the
    paintings and the number of paintings on each platform
     */
    private static Result program5A(int n, int w, int[] heights, int[] widths) {
        // Create memoization table to store the results of previous runs (as learned in class)
        MemoState[][] memo = new MemoState[n][n];
        // Creates a variable of the MemoState object type, and will use the function solveDP to find the optimal painting arrangement
        MemoState finalState = solveDP(0, n-1, w, heights, widths, memo);
        // Reconstruct solution to get paintings per shelf
        int[] numPaintings = reconstructSolution(0, n-1, memo);
        return new Result(finalState.numShelves, finalState.totalHeight, numPaintings);
    }
    // solveDP is a recursive function that will solve all subproblems using memoization.
    private static MemoState solveDP(int start, int end, int w, int[] heights, int[] widths, MemoState[][] memo) {
        // Base case, reaches this point if we have reached the end of the paintings, so the total height and number of shelves is zero
        if (start > end) {
            return new MemoState(0, 0, -1);
        }
        // Memoization being utilized, if we already have a previously calculated solution, return said previous solution
        if (memo[start][end] != null) {
            return memo[start][end];
        }
        //variable that is keeping track of the current width of the shelf
        int currentWidth = 0;
        //Will keep track of the max height on a shelf
        int maxHeight = 0;
        //boolean used to keep track of the control flow. Ideally, the rest of the paintings can fit onto one shelf, so we try doing that
        boolean canFitOneShelf = true;
        for (int i = start; i <= end; i++) {
            //updates current width with the widths of the paintings to be put on the shelf
            currentWidth += widths[i];
            //determines what is greater, the already stated max height, or the height of one of the paintings
            maxHeight = Math.max(maxHeight, heights[i]);
            //if after these calculations, currentWidth exceeds w, then the remaining paintings cannot fit onto one shelf
            if (currentWidth > w) {
                canFitOneShelf = false;
                break;
            }
        }
        // if the remaining paintings are able to fit onto one shelf, then this is the ideal solution and there is no need to continue further
        if (canFitOneShelf) {
            //stores the calculations in the table
            memo[start][end] = new MemoState(maxHeight, 1, end);
            return memo[start][end];
        }
        // Since the rest of the paintings are not able to fit on one shelf, there must be a split somewhere, and we have to find the ideal place to do so
        int minTotalHeight = 99999; //really large height constraint as default
        int bestNumShelves = 0; //ideal number of shelves for split
        int bestSplit = -1; //index of the best split location
        //runs through all the paintings that come after the starting index
        for (int split = start; split < end; split++) {
            //We split our shelf calculations into left and right halves to create subproblems that we will solve recursively
            MemoState leftResult = solveDP(start, split, w, heights, widths, memo);
            MemoState rightResult = solveDP(split + 1, end, w, heights, widths, memo);
            //total cost will be determined by adding subproblem's recursive solution's totalHeights
            int totalHeight = leftResult.totalHeight + rightResult.totalHeight;
            //if the totalHeight is less than the current minTotalHeight, then this solution is more optimal, so update the variables to reflect this
            if (totalHeight < minTotalHeight) {
                minTotalHeight = totalHeight;
                bestNumShelves = leftResult.numShelves + rightResult.numShelves;
                bestSplit = split;
            }
        }
        //stores the results in the memoization table and returns the result
        memo[start][end] = new MemoState(minTotalHeight, bestNumShelves, bestSplit);
        return memo[start][end];
    }
    // Now that we have identified the best locations to place the splits, it is time to determine how many of these paintings go on each of these shelves specifically
    private static int[] reconstructSolution(int start, int end, MemoState[][] memo) {
        //base case, if the start ever goes past the end index, then there are no more paintings left in the range, return an empty value
        if (start > end) {
            return new int[0];
        }
        //grab the current value in the memoization table, which has all the information stored in the class
        MemoState currentVal = memo[start][end];
        //if all of the remaining paintings are able to fit onto one shelf, return an array with the number of paintings in the range, which is calculated as end - start + 1
        if (currentVal.splitPosition == end) {
            return new int[]{end - start + 1};
        }
        // recursively calls the function to reconstruct the left and right sides of the solution. leftSolution will hold the number of paintings on each shelf for the left part,
        // and the rightSolution will hold the number of paintings on each shelf for the right part
        int[] leftSolution = reconstructSolution(start, currentVal.splitPosition, memo);
        int[] rightSolution = reconstructSolution(currentVal.splitPosition + 1, end, memo);
        // we create an array that will be a return value for the function. This array will hold the shelf counts for both the left and right halves
        int[] result = new int[leftSolution.length + rightSolution.length];
        //fill result with the shelf data from the left half
        for (int i = 0; i < leftSolution.length; i++) {
            result[i] = leftSolution[i];
        }
        //fill result with the shelf data from the right half
        for (int i = 0; i < rightSolution.length; i++) {
            result[leftSolution.length + i] = rightSolution[i];
        }
        //on the final iteration, this array will store how many paintings should optimally go on each shelf
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