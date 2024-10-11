import java.util.Scanner;

class Program2{
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}
   
    /**
    * Solution to program 2
    * @param n number of paintings
    * @param w width of the platform
    * @param heights array of heights of the paintings
    * @param widths array of widths of the paintings
    * @return Result object containing the number of platforms, total height of the paintings and the number of paintings on each platform
    */
    private static Result program2(int n, int w, int[] heights, int[] widths) {
        int numPlatforms = 0;
        int totalHeight = 0;
        int currentWidth = 0;
        int maxHeight = 0;
        int paintingCount = 0;
        int[] numPaintingsOnPlatform = new int[n];

        for (int i = 0; i < n; i++) {
            if (currentWidth + widths[i] <= w) {
                currentWidth += widths[i];
                maxHeight = Math.max(maxHeight, heights[i]);
                paintingCount++;
            } else{
                //finish current platform
                numPlatforms++;
                totalHeight += maxHeight;
                numPaintingsOnPlatform[numPlatforms - 1] = paintingCount;

                //start a new platform
                currentWidth = widths[i];
                maxHeight = heights[i];
                paintingCount = 1;
            }
        }
        //final platform
        numPlatforms++;
        totalHeight += maxHeight;
        numPaintingsOnPlatform[numPlatforms - 1] = paintingCount;

        int[] finalPlatformPaintings = new int[numPlatforms];
        System.arraycopy(numPaintingsOnPlatform, 0, finalPlatformPaintings, 0, numPlatforms);

        // return new Result(0, 0, new int[0]);//replace with your own result
        return new Result(numPlatforms, totalHeight, finalPlatformPaintings);
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