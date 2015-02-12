/**
 * Find the contiguous subarray within an array (containing at least one number) which has the largest sum.
For example, given the array [−2,1,−3,4,−1,2,1,−5,4],
the contiguous subarray [4,−1,2,1] has the largest sum = 6.
Edge case: all elements in the array are negative value;
*/
public class Solution {
    public int maxSubArray(int[] A) {
        //handle the edge case: all elements are negative
        //directly get the max negative value of the array
        int flag = 0;
        int max=Integer.MIN_VALUE;
        for(int i=0;i<A.length;i++){
            if(A[i] > max)
                max = A[i];
            if(A[i]>=0)
                flag=1;
        }
        if(flag == 0) return max;
        
        int currentMax = 0;
        int finalMax = 0;
        
        for(int i=0;i<A.length;i++){
            if(A[i] < 0){
                if(currentMax+A[i] <0)
                    currentMax = 0;
                else
                    currentMax = currentMax+A[i];
            }
            else{
                currentMax = currentMax+A[i];
                if(currentMax > finalMax)
                    finalMax = currentMax;
            }
        }
        return finalMax;
    }
}
