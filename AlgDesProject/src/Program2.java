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
        int[] dp = new int[n + 1]; // dp[0] = 0
        int[] numPaintingsOnPlatform = new int[n + 1];
        int[] platformStarts = new int[n + 1]; // To track the start of each platform
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE;
            int cumWidth = 0;
            int tallestHeight = 0;
            // Consider all possible platforms ending at painting i
            for (int j = i; j >= 1; j--) {
                cumWidth += widths[j - 1];
                if (cumWidth > W) {
                    break;
                }
                tallestHeight = Math.max(tallestHeight, heights[j - 1]);
                if (dp[j - 1] + tallestHeight < dp[i]) {
                    dp[i] = dp[j - 1] + tallestHeight;
                    numPaintingsOnPlatform[i] = i - (j - 1);
                    platformStarts[i] = j - 1;
                }
            }
        }

        // Reconstruct the number of paintings on each platform
        List<Integer> numPaintingsList = new ArrayList<>();
        int i = n;
        while (i > 0) {
            numPaintingsList.add(0, numPaintingsOnPlatform[i]);
            i = platformStarts[i];
        }

        int numPlatforms = numPaintingsList.size();
        int totalHeight = dp[n];
        int[] numPaintingsResult = new int[numPlatforms];
        for (int k = 0; k < numPlatforms; k++) {
            numPaintingsResult[k] = numPaintingsList.get(k);
        }

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