/**
 * Solution:
 * use two int maxHere and minHere to keep track of the current
 * max and min value.
 * make sure that maxHere is alwasys >=1 and minHere is always <=1.
 * 
 * foreach int i in array:
 * if i>0: maxHere = maxHere*i, minHere = minHere*i
 * if minHere*i >1, minHere = 1; then try to update
 * finalMax using maxHere.
 * 
 * if i=0: reset both minHere and maxHere to 1, because 0 cannot
 * be included. 
 * 
 * if i<0: int temp = maxHere,maxHere = minHere*i, minHere = temp*i
 * if maxHere*i<1, maxHere=1; then try to update
 * finalMax using maxHere
 * 
 * Note two important edge case:
 * 1, the Max product is 0. {0,0,-2}, {0,-2,0,-3,0,-5}, {0,0,0};
 * 2, the Max product is a negative number. occurs when array has
 * only one element and it's a negative number. {-3}, {-4};
 * 
 * So we need to do a pretest to exclude the above two edge cases.
 * Then if the pretest fail, we manually check if it's edge case#1,
 * if it is, return a[0], otherwise, return 0;
 * */


public class Solution {
    private boolean preTest(int[] A){
        for(int i=0;i<A.length;i++){
            if(A[i] > 0)  //if any positive element
                return true;
            if(i!=0 && A[i]<0 && A[i-1]<0)//if any two concecutive negative element
                return true;
        }
        return false;
    }
    
    public int maxProduct(int[] A) {
        if(!preTest(A)){
            if(A.length == 1 && A[0]<0)
                return A[0];
            else
                return 0;
        }
        int maxHere = 1, minHere = 1, finalMax = 1;
        for(int i : A){
            if(i>0){
                maxHere = maxHere * i;
                minHere = minHere * i;
                if(minHere > 1)
                    minHere = 1;
            }
            else if(i==0){
                maxHere = 1;
                minHere = 1;
            }
            else{ //i<0
                int temp = maxHere; //store original value of maxHere first!!
                maxHere = minHere * i;
                minHere = temp * i;
                if(maxHere<1)
                    maxHere = 1;
            }
            if(maxHere>finalMax)
                finalMax = maxHere;
        }
        return finalMax;
    }
}
