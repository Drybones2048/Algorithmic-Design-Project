import java.util.*;

class Program2{
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}
   
    /**
    * Solution to program 2
    * @param n number of paintings
    * @param W width of the platform
    * @param heights array of heights of the paintings
    * @param widths array of widths of the paintings
    * @return Result object containing the number of platforms, total height of the paintings and the number of paintings on each platform
    */
    private static Result program2(int n, int W, int[] heights, int[] widths) {
        int[] numPaintings = new int[n]; // To store the number of paintings on each platform
        int numPlatforms = 0;
        int totalHeight = 0;

        // Find the index 'k' where the height is minimal
        int k = 0;
        for (int i = 1; i < n; i++) {
            if (heights[i] < heights[k]) {
                k = i;
            }
        }

        // Process the decreasing sequence from index 0 to k-1
        int i = 0;
        while (i < k) {
            int cumWidth = widths[i];
            int tallestHeight = heights[i];
            int paintingsOnPlatform = 1;
            int j = i + 1;
            while (j < k && cumWidth + widths[j] <= W) {
                cumWidth += widths[j];
                // tallestHeight remains heights[i] as it's decreasing
                paintingsOnPlatform++;
                j++;
            }
            totalHeight += tallestHeight;
            numPaintings[numPlatforms++] = paintingsOnPlatform;
            i = j;
        }

        // Process the minimal painting at index k
        totalHeight += heights[k];
        numPaintings[numPlatforms++] = 1;
        i = k + 1;

        // Process the increasing sequence from index k+1 to n-1
        while (i < n) {
            int cumWidth = widths[i];
            int tallestHeight = heights[i];
            int paintingsOnPlatform = 1;
            int j = i + 1;
            while (j < n && cumWidth + widths[j] <= W) {
                cumWidth += widths[j];
                tallestHeight = heights[j]; // Update tallestHeight as heights are increasing
                paintingsOnPlatform++;
                j++;
            }
            totalHeight += tallestHeight;
            numPaintings[numPlatforms++] = paintingsOnPlatform;
            i = j;
        }

        // Prepare the result
        int[] numPaintingsResult = new int[numPlatforms];
        System.arraycopy(numPaintings, 0, numPaintingsResult, 0, numPlatforms);

        return new Result(numPlatforms, totalHeight, numPaintingsResult);
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