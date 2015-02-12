/**Write a program to find the node at which the intersection of two singly linked lists begins.
*
*
*For example, the following two linked lists:
*
*A:          a1 → a2
*                   ↘
*                     c1 → c2 → c3
*                   ↗            
*B:     b1 → b2 → b3
*begin to intersect at node c1.
*
*Notes:
*
*If the two linked lists have no intersection at all, return null.
*The linked lists must retain their original structure after the function returns.
*You may assume there are no cycles anywhere in the entire linked structure.
*Your code should preferably run in O(n) time and use only O(1) memory.
*/

/**
 * Three Methods:
 * 
 * private int getLength(ListNode head): 
 * get length of a linkedlist
 * 
 * private ListNode getIntersectionhelper(int d, ListNode1, ListNode2):
 * d is the difference of length of two linkedlists
 * ListNode1 is the head of longer linkedlist, ListNode2 is the head of
 * shorter LinkedList
 * 
 * public ListNode getIntersection(ListNode1, ListNode2)
 * first caculate the difference of length, then according to the difference,
 * call the helper method
 * */


/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    private int getLength(ListNode head)
    {
        int l =0;
        while(head!=null)
        {
            l++;
            head = head.next;
        }
        return l;
    }
    
    private ListNode getIntersectionHelper(int d, ListNode headA, ListNode headB)
    {
        for(int i=0;i<d;i++)
        {
            if(headA == null)
                return null;
            headA = headA.next;
        }
        
        while(headA!=null && headB!=null)
        {
            if(headA ==headB)
                return headA;
            headA = headA.next;
            headB = headB.next;
        }
        return null;
    }
    
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        int lA = getLength(headA);
        int lB = getLength(headB);
        if(lA > lB)
        {
            int d = lA-lB;
            return getIntersectionHelper(d, headA, headB);
        }
        else
        {
            int d = lB-lA;
            return getIntersectionHelper(d, headB, headA);
        }
    }
}
