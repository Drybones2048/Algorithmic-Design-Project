// COP4533 Milestone 2
// Anthony Rumore, Dylan Harle

import java.util.ArrayList;
import java.util.Scanner;

class Program4 {
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}
   
    /**
    * Solution to program 4
    * @param n number of paintings
    * @param w width of the shelf
    * @param heights array of heights of the paintings
    * @param widths array of widths of the paintings
    * @return Result object containing the number of shelves, total height of the paintings and the number of paintings on each shelf
    */
    private static Result program4(int n, int w, int[] heights, int[] widths) {
        // dp[i][j] represents minimum total height needed for paintings i through j
        int[][] dp = new int[n][n];
        // split[i][j] stores where to split between i and j for reconstruction
        int[][] split = new int[n][n];

        // Initialize both arrays with default values
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Initialize with a value bigger than anything we're working with (I hope)
                dp[i][j] = 99999;
                // -1 means that no split has been determined yet
                split[i][j] = -1;
            }
        }

        // Initial case: all the painting heights are in the x-axis
        for (int i = 0; i < n; i++) {
            dp[i][i] = heights[i];
        }

        // We will try different lengths by starting with two paintings on a shelf
        // One painting on a shelf is already accounted for by the initialization of the array
        //
        for (int segmentLength = 2; segmentLength <= n; segmentLength++) {
            // startIndex is the start of the segment we are working with
            for (int startIndex = 0; startIndex <= n - segmentLength; startIndex++) {
                // end of the segment we are working with
                int endIndex = startIndex + segmentLength - 1;

                // Try putting all paintings from startIndex to endIndex on one shelf
                int width = 0;
                int maxHeight = 0;
                boolean canFit = true;

                for (int currentIndex = startIndex; currentIndex <= endIndex; currentIndex++) {
                    // Goes through every painting's width and adds it to the width of the shelf
                    width += widths[currentIndex];
                    // If we go beyond the shelf width, it doesn't fit! Oh no!
                    if (width > w) {
                        canFit = false;
                        // Stop looping because no more can fit
                        break;
                    }
                    // Update height
                    maxHeight = Math.max(maxHeight, heights[currentIndex]);
                }

                // Store the painting's max height if it fits
                if (canFit) {
                    dp[startIndex][endIndex] = maxHeight;
                }

                // Try splitting into two parts
                // splitIndex is the spot in the group to split into two groups.
                // We iterate through different split points to try and find the best!
                for (int splitIndex = startIndex; splitIndex < endIndex; splitIndex++) {

                    // Check can the two groups fit on their own shelves?
                    if (dp[startIndex][splitIndex] != 99999 && dp[splitIndex + 1][endIndex] != 99999) {
                        // This is how high the two groups would be on their own shelves added together
                        int totalHeight = dp[startIndex][splitIndex] + dp[splitIndex + 1][endIndex];
                        // Is the split result any better than every trying be together?
                        if (totalHeight < dp[startIndex][endIndex]) {
                            // If it is, we go with the split results
                            dp[startIndex][endIndex] = totalHeight;
                            split[startIndex][endIndex] = splitIndex;
                        }
                    }
                }
            }
        }

        // Count number of shelves and paintings per shelf
        ArrayList<Integer> shelfSizes = new ArrayList<>();
        // Start recursive function
        countShelves(split, 0, n - 1, shelfSizes);

        // Copy shelfSizes to numPaintings to return at the end
        int[] numPaintings = new int[shelfSizes.size()];
        for (int index = 0; index < shelfSizes.size(); index++) {
            numPaintings[index] = shelfSizes.get(index);
        }

        return new Result(numPaintings.length, dp[0][n-1], numPaintings);
    }

    private static void countShelves(int[][] split, int start, int end, ArrayList<Integer> shelfSizes) {
        // Base-case: if there's nothing to process, leave
        if (start > end) {
            return;
        }

        int splitPoint = split[start][end];
        // Remember, -1 means that no split has been determined
        if (splitPoint == -1) {
            // Everything is on one shelf then
            shelfSizes.add(end - start + 1);
        }
        else {
            // Split into two parts
            countShelves(split, start, splitPoint, shelfSizes);
            countShelves(split, splitPoint + 1, end, shelfSizes);
        }
    }

    public static void main(String[] args){
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
        Result result = program4(n, W, heights, widths);
        System.out.println(result.numPlatforms);
        System.out.println(result.totalHeight);
        for(int i=0; i<result.numPaintings.length; i++){
            System.out.println(result.numPaintings[i]);
        }
    }
}