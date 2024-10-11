import java.util.Scanner;

class Program1{
    public record Result(int numPlatforms, int totalHeight, int[] numPaintings) {}
   
    /**
    * Solution to program 1
    * @param n number of paintings
    * @param w width of the platform
    * @param heights array of heights of the paintings
    * @param widths array of widths of the paintings
    * @return Result object containing the number of platforms, total height of the paintings and the number of paintings on each platform
    */
    private static Result program1(int n, int w, int[] heights, int[] widths) {
        int numPlatforms = 0;
        int totalHeight = 0;
        int currentWidth = 0;
        int maxHeight = 0;
        int paintingCount = 0;
        int[] numPaintingsOnPlatform = new int[n];

        for (int i = 0; i < n; i++) {
            if(currentWidth + widths[i] <= w){ //if the width of the new painting plus the width of the current platform is still less than the max width of one platform, add it to the platform
                currentWidth += widths[i];
                maxHeight = Math.max(maxHeight, heights[i]); //checks to see what is higher, the height of the tallest current painting or the maxHeight parameter
                paintingCount++;
            } else{ //if the width of the new painting is too much for the current platform and goes over the max, we add a new platform

                //Next three lines take the process of finishing off the current platform's details before moving onto the next one
                numPlatforms++;
                totalHeight += maxHeight; //the max height of the new shelf is the default
                numPaintingsOnPlatform[numPlatforms - 1] = paintingCount; //STATEMENT NEEDS FURTHER ANALYSIS

                //Next three lines create a new platform
                currentWidth = widths[i];
                maxHeight = heights[i];
                paintingCount = 1; //count is one because of the painting just added
            }
        }

        //final platform wrap-up
        numPlatforms++;
        totalHeight += maxHeight;
        numPaintingsOnPlatform[numPlatforms - 1] = paintingCount;

        int[] finalPlatformPaintings = new int[numPlatforms];
        System.arraycopy(numPaintingsOnPlatform, 0, finalPlatformPaintings, 0, numPlatforms); //LINE NEEDS FURTHER ANALYSIS

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
        Result result = program1(n, W, heights, widths);
        System.out.println(result.numPlatforms);
        System.out.println(result.totalHeight);
        for(int i=0; i<result.numPaintings.length; i++){
            System.out.println(result.numPaintings[i]);
        }
    }
}