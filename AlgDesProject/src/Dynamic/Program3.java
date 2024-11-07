package Dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Program3 {
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}
   
    /**
    * Solution to program 3
    * @param n number of paintings
    * @param w width of the platform
    * @param heights array of heights of the paintings
    * @param widths array of widths of the paintings
    * @return Result object containing the number of platforms, total height of the paintings and the number of paintings on each platform
    */
    private static Result program3(int n, int w, int[] heights, int[] widths) {
        // Keep track of best solution found
        int[] optimalSolution = new int[n];
        int[] currentShelf = new int[n];
        // This ArrayList keeps track of the height of each shelf and assists in further calculations
        ArrayList<Integer> currentShelfHeights = new ArrayList<>();

        // Initialize with a very high number, likely bigger than any parameter.
        // We're using an Array of size 1 to get a pointer we can access within the recursive function calls. Java is fun
        int[] totalHeightSolution = {99999};

        // Start recursive search
        findMinHeight(0, n, w, heights, widths, 0, 0, currentShelf, currentShelfHeights, optimalSolution, totalHeightSolution);

        // Convert the best solution to required format
        int numShelves = 0;
        for (int i = 0; i < n; i++) {
            if (optimalSolution[i] > numShelves) {
                numShelves = optimalSolution[i];
            }
        }

        // Count paintings per shelf
        int[] numPaintings = new int[numShelves];
        for (int i = 0; i < n; i++) {
            numPaintings[optimalSolution[i] - 1]++;
        }

        return new Result(numShelves, totalHeightSolution[0], numPaintings);
    }

    private static void findMinHeight(int pos, int n, int w, int[] heights, int[] widths,
                                      int currentWidth, int shelfHeight, int[] currentShelf,
                                      ArrayList<Integer> shelfHeights, int[] bestSolution, int[] totalHeightSolution) {
        // Pos tracks our position recursively. It is first called with 0 and serves as a marker as we progress.
        if (pos == n) {
            // When we reach the end, include the height of the last shelf
            int totalHeight = 0;
            if (!shelfHeights.isEmpty()) {
                // Calculates the total height
                for (Integer height : shelfHeights) {
                    totalHeight = totalHeight + height;
                }

                // Make sure the total height accounts for the last shelf
                if (shelfHeight > 0) {  // If there are items on the current shelf
                    totalHeight += shelfHeight;
                }
            }
            
            else { // Otherwise there's nothing on other shelves
                totalHeight = shelfHeight;
            }

            // If this solution is better than the current one saved, make this the solution
            if (totalHeight < totalHeightSolution[0]) {
                totalHeightSolution[0] = totalHeight;
                for (int i = 0; i < n; i++) {
                    bestSolution[i] = currentShelf[i]; // Copy each element from currentShelf to bestSolution
                }
            }

            return;
        }

        // If the width of the new painting can on the current shelf
        // (meaning currentWidth + the width of the painting is less than the max shelf width), then we add it
        if (currentWidth + widths[pos] <= w) {
            int prevShelfHeight = shelfHeight; // Temp storage
            currentShelf[pos] = shelfHeights.size() + 1;

            // check to see if the new painting is the tallest on the shelf
            shelfHeight = Math.max(shelfHeight, heights[pos]);

            // Now that we added our painting, we will try to add the next one recursively
            findMinHeight(pos + 1, n, w, heights, widths,
                    currentWidth + widths[pos], shelfHeight,
                    currentShelf, shelfHeights, bestSolution, totalHeightSolution);

            shelfHeight = prevShelfHeight;
        }

        // Try starting a new shelf
        if (shelfHeight > 0) {  // Only add height if there were items on the shelf
            shelfHeights.add(shelfHeight);
        }
        currentShelf[pos] = shelfHeights.size() + 1;
        findMinHeight(pos + 1, n, w, heights, widths,
                widths[pos], heights[pos],
                currentShelf, shelfHeights, bestSolution, totalHeightSolution);

        // Helps us with backtracking; cleans up unnecessary shelves
        if (shelfHeight > 0) {
            shelfHeights.remove(shelfHeights.size() - 1);
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
        Result result = program3(n, W, heights, widths);
        System.out.println(result.numPlatforms);
        System.out.println(result.totalHeight);
        for(int i=0; i<result.numPaintings.length; i++){
            System.out.println(result.numPaintings[i]);
        }
    }
}