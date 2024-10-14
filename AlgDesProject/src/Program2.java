// Team Members: Dylan Harle & Anthony Rumore
// Algorithm Abstraction & Design Fall 2024

import java.util.ArrayList;
import java.util.Scanner;

class Program2 {
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}

    /**
     * Solution to program 2
     * @param n number of paintings
     * @param w width of the platform
     * @param heights array of heights of the paintings
     * @param widths array of widths of the paintings
     * @return Result object containing the number of platforms, total height of the paintings and the number of paintings on each platform
     */
    public static Result program2(int n, int w, int[] heights, int[] widths) {
        int minimumHeightIndex = 0;
        for (int i = 1; i < n; i++) {
            if (heights[i] < heights[minimumHeightIndex]) { // If it finds a height that is lower, it updates the min height
                minimumHeightIndex = i;
            }
        }

        ArrayList<Shelf> totalShelves = new ArrayList<>();

        // First half of numbers
        if (minimumHeightIndex > 0) {
            totalShelves.addAll(paintingsToShelves(heights, widths, w, 0, minimumHeightIndex));
        }

        // Minimum logic
        int minItemWidth = widths[minimumHeightIndex];

        // Check to see if the minHeight item fits on the prev shelf
        if (!totalShelves.isEmpty() && minItemWidth + totalShelves.get(totalShelves.size() - 1).width <= w) {
            Shelf lastShelf = totalShelves.get(totalShelves.size() - 1);
            lastShelf.numPaintings++;
            lastShelf.width += minItemWidth;

            if (minimumHeightIndex < n - 1) {
                totalShelves.addAll(paintingsToShelves(heights, widths, w, minimumHeightIndex + 1, n));
            }
        }
        else {
            // Find out if a single shelf is optimal

            // Attempt 1: Min shelf is on its own
            ArrayList<Shelf> attempt1 = new ArrayList<>();
            // Add the min shelf as its own shelf
            Shelf minShelf = new Shelf();
            minShelf.numPaintings = 1;
            minShelf.width = minItemWidth;
            minShelf.tallestHeight = heights[minimumHeightIndex];
            attempt1.add(minShelf);

            if (minimumHeightIndex < n - 1) {
                attempt1.addAll(paintingsToShelves(heights, widths, w, minimumHeightIndex + 1, n));
            }

            // Attempt 2: Min shelf gets grouped with 2nd half shelves
            ArrayList<Shelf> attempt2 = new ArrayList<>();
            attempt2.addAll(paintingsToShelves(heights, widths, w, minimumHeightIndex, n));

            // See which solution is better
            if (getHeight(attempt1) <= getHeight(attempt2)) {
                totalShelves.addAll(attempt1);
            }
            else {
                totalShelves.addAll(attempt2);
            }

        }


        int numPlatforms = totalShelves.size();
        int totalHeight = getHeight(totalShelves);

        int[] numPaintingsResult = new int[numPlatforms];
        for (int i = 0; i < numPlatforms; i++)
            numPaintingsResult[i] = totalShelves.get(i).numPaintings;

        return new Result(numPlatforms, totalHeight, numPaintingsResult);
    }

    public static class Shelf {
        public int tallestHeight = 0;
        public int numPaintings = 0;
        public int width = 0;
    }

    // Helper method that assigns paintings to shelves and return shelves
    private static ArrayList<Shelf> paintingsToShelves(int[] height, int[] widths, int w, int startIndex, int endIndex) {
        ArrayList<Shelf> shelves = new ArrayList<>();
        Shelf currentShelf = new Shelf();

        for (int i = startIndex; i < endIndex; i++) {

            // Is there room for another painting?
            if (currentShelf.width + widths[i] <= w) {
                // Add a new painting
                currentShelf.width += widths[i];
                currentShelf.numPaintings += 1;
                // Update the tallest painting
                currentShelf.tallestHeight = Math.max(currentShelf.tallestHeight, height[i]);
            }
            // Can't fit? New shelf
            else {
                // Save old shelf
                shelves.add(currentShelf);
                // Make a new shelf
                currentShelf = new Shelf();
                // Adds 1 painting to the next shelf
                currentShelf.numPaintings += 1;
                // Reset current width to the painting
                currentShelf.width = widths[i];
                // Initialize the height of the shelf
                currentShelf.tallestHeight = height[i];
            }
        }

        shelves.add(currentShelf);

        return shelves;
    }

    private static int getHeight(ArrayList<Shelf> shelves) {
        int height = 0;

        for (Shelf shelf : shelves)
            height += shelf.tallestHeight;

        return height;
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
        Result result = program2(n, W, heights, widths);
        System.out.println(result.numPlatforms);
        System.out.println(result.totalHeight);
        for(int i=0; i<result.numPaintings.length; i++){
            System.out.println(result.numPaintings[i]);
        }
    }
}