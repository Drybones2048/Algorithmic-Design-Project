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
        int totalShelves = 0; //total number of shelves
        int totalHeight = 0; //total cost of the algoritm
        int currentWidth = 0; //width of paintings on the shelf
        int maxHeight = 0; //max height of a shelf
        int paintingCount = 0; //number of paintings on a shelf
        int[] numPaintingsOnShelf = new int[n]; 

        for (int i = 0; i < n; i++) {
            if(currentWidth + widths[i] <= w){ //if the width of the new painting plus the width of the current shelf is still less than the max width of one shelf, add it to the shelf
                currentWidth = currentWidth + widths[i];
                
                maxHeight = Math.max(maxHeight, heights[i]); //checks to see what is higher, the height of the tallest current painting or the maxHeight parameter
                
                paintingCount = paintingCount + 1; //increases count of the number of paintings on the shelf
                
            } else{ //if the width of the new painting is too much for the current shelf and goes over the max, we add a new shelf

                //Next three lines take the process of finishing off the current shelf's details before moving onto the next one
                totalShelves++;
                totalHeight += maxHeight; //the max height of the new shelf is the default
                numPaintingsOnShelf[totalShelves - 1] = paintingCount;

                //next three lines reset the current shelf and create a new one
                currentWidth = widths[i];
                maxHeight = heights[i];
                paintingCount = 1; //count is one because of the painting just added
            }
        }

        //final shelf wrap-up
        totalShelves = totalShelves + 1;
        totalHeight = totalHeight + maxHeight;
        numPaintingsOnShelf[totalShelves - 1] = paintingCount;

        int[] finalShelfPaintings = new int[totalShelves];

        int numPlatforms = totalShelves;
        
        System.arraycopy(numPaintingsOnShelf, 0, finalShelfPaintings, 0, numPlatforms);

        return new Result(numPlatforms, totalHeight, finalShelfPaintings);
        
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int W = sc.nextInt();
        int[] heights = new int[n];
        int[] widths = new int[n];
        for(int i=0; i < n; i++){
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
