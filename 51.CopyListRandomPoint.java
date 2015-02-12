/**
 * Definition for singly-linked list with a random pointer.
 * class RandomListNode {
 *     int label;
 *     RandomListNode next, random;
 *     RandomListNode(int x) { this.label = x; }
 * };
 * 
 * Solution:
 * 
 * Three Steps:
 * 1, copy each node in the given list and insert them after the original node
 * 2, copy the random pointer of original node to new copy node
 * 3, split the whole List into two parts:
 * one is still the original list, the other is the deepcopy list
 */
public class Solution {
    public RandomListNode copyRandomList(RandomListNode head) {
        if(head == null) //handle edge case
            return null;
        RandomListNode current = head;
        
        //insert node after original node
        while(current!=null){
            RandomListNode temp = new RandomListNode(current.label);
            temp.next = current.next;
            current.next = temp;
            current = temp.next;
        }
        
        //copy random pointer
        current = head;
        while(current!=null){
            RandomListNode temp = current.next;
            if(current.random!=null)
                temp.random = current.random.next; //point to the new copy of original random node
            current = temp.next;
        }
        
        //split the list
        current = head;
        RandomListNode newHead = current.next; //must get the newHead before spliting!!!
        while(current!=null){
            RandomListNode temp = current.next;
            current.next = temp.next;
            if(temp.next!=null)
                temp.next = current.next.next;
            current = current.next;
        }
        return newHead;
    }
}
