import java.util.Scanner;

class Program5B {
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}


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
        // Min total height for each painting index (for dynamic programming)
        int[] dp = new int[n];
        // How many paintings should go on each shelf data
        int[] shelfSizes = new int[n];

        // Base case: first painting is always alone on its shelf
        dp[0] = heights[0];
        // First shelf must contain exactly one painting
        shelfSizes[0] = 1;

        // Iterate through all paintings starting from the second one (since the first is already on the shelf)
        for (int currentPainting = 1; currentPainting < n; currentPainting++) {
            // Set current width with the width of the current painting
            int currentWidth = widths[currentPainting];


            // Initial solution: put current painting on new shelf above previous solution
            dp[currentPainting] = dp[currentPainting-1] + heights[currentPainting];
            shelfSizes[currentPainting] = 1;

            // Initialize maxHeight
            int maxHeight = heights[currentPainting];
            int previousPainting = currentPainting - 1; // Index

            // Try combining current painting with previous paintings if they fit on shelf width
            while (previousPainting >= 0 && currentWidth + widths[previousPainting] <= w) {
                currentWidth += widths[previousPainting]; // Update width with the prev
                maxHeight = Math.max(maxHeight, heights[previousPainting]); // Update the max height (if the new painting is bigger)

                // Get height of all shelves before current shelf configuration
                int prevHeight;
                if (previousPainting > 0) {
                    prevHeight = dp[previousPainting - 1];
                } else {
                    prevHeight = 0;
                }


                // Is this configuration actually better?
                if (prevHeight + maxHeight < dp[currentPainting]) {
                    // If so, let's make implement it
                    // Update minimum total height
                    dp[currentPainting] = prevHeight + maxHeight;
                    // Store number of paintings on current shelf in this configuration
                    shelfSizes[currentPainting] = currentPainting - previousPainting + 1;
                }

                // Let's process the previous to the previous painting next
                previousPainting--;
            }
        }

        int totalShelves = 0;

        // Start from last painting and work backwards
        int currentPaintingIndex = n-1;
        while (currentPaintingIndex >= 0) {
            totalShelves++;
            // Go back by the number of paintings on current shelf to go shelf by shelf to the beginning
            currentPaintingIndex -= shelfSizes[currentPaintingIndex];
        }

        // Result will be the number of paintings on each shelf
        int[] result = new int[totalShelves];

        currentPaintingIndex = n-1; // Current painting (starting from the last)
        int currShelfIndex = totalShelves - 1; // Current shelf (also starting at the end)

        while (currentPaintingIndex >= 0) {
            // Fill in result with the data
            result[currShelfIndex] = shelfSizes[currentPaintingIndex];

            // Move positions backwards until we reach the start
            currentPaintingIndex -= shelfSizes[currentPaintingIndex];
            currShelfIndex--;
        }

        // number of shelves, total height, and paintings per shelf
        return new Result(totalShelves, dp[n-1], result);
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