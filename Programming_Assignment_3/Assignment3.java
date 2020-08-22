import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.min;

public class Assignment3 {
    private int[][] sum;
    int[][] OPT;

    private int Compute_OPT(int start, int end){
        if(start == end){
            //no place to put blockade
            OPT[start][end] = 0;
            return 0;
        }

        int blockadeleftbound = start;
        int blockaderightbound = end;
        int blockade = (blockadeleftbound + blockaderightbound)/2;
        int oldmin = -1;
        int actualblockade = 0; //blockade of 1 means there is a blockade between index 1 and 2
        int left;
        int right;
        while(true){
            if(OPT[start][blockade] == Integer.MAX_VALUE) {
                left = sum[start][blockade] + Compute_OPT(start, blockade);
                OPT[start][blockade] = left - sum[start][blockade];
            }
            else{
                left = sum[start][blockade] + OPT[start][blockade];
            }

            if(OPT[blockade + 1][end] == Integer.MAX_VALUE) {
                right = sum[blockade + 1][end] + Compute_OPT(blockade + 1, end);
                OPT[blockade + 1][end] = right - sum[blockade + 1][end];
            }
            else{
                right = sum[blockade + 1][end] + OPT[blockade + 1][end];
            }

            //test if improved
            if(left < right){
                if(left > oldmin) {
                    actualblockade = blockade;
                    oldmin = left;
                }
            }
            else{
                if(right > oldmin){
                    actualblockade = blockade;
                    oldmin = right;
                }
            }

            //update blockade location
            int newblockade;
            if(left < right){
                //blockade needs to be shifted right
                blockadeleftbound = blockade;
            }
            else{
                //blockade needs to be shifted left
                blockaderightbound = blockade;
            }

            newblockade = (blockadeleftbound + blockaderightbound)/2;
            if(blockade == newblockade){
                //nowhere else to go
                break;
            }
            else{
                blockade = newblockade;
            }
        }

        //know optimal location of blockade, now take the minimum side
        left = sum[start][actualblockade] + OPT[start][actualblockade];
        right = sum[actualblockade + 1][end] + OPT[actualblockade + 1][end];
        if(left < right){
            return left;
        }
        else if (left == right){
            if(actualblockade - start + 1 > end - actualblockade){
                return left;
            }
            else{
                return right;
            }
        }
        else{
            return right;
        }
    }

    public int maxFruitCount (int[] sections) {
        // Implement your dynamic programming algorithm here
        // You may use helper functions as needed
        sum = new int[sections.length][sections.length];
        OPT = new int[sections.length][sections.length];

        //initialize sums
        for(int i = 0; i < sections.length; i++){
            int total = sections[i];
            sum[i][i] = total;

            for(int j = i + 1; j < sections.length; j++){
                total += sections[j];
                sum[i][j] = total;
            }
        }

        //initialize OPT
        for(int i = 0; i < sections.length; i++){
            for(int j = 0; j < sections.length; j++){
                OPT[i][j] = Integer.MAX_VALUE;
            }
        }

        return Compute_OPT(0, sections.length - 1);
    }
}
