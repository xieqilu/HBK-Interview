//Design and Implement a data structure for
//Least Recently Used (LRU) Cache.
//Support two operations as follows:

//Get(key): get the value of the key if the key
//exists in the cache, otherwise return -1

//Set(key, value): set the new value to the specific key
//if the key doesn't exist, insert the new (key, value)
//to the cache, if the cache is full of capacity, remove
//the least rechetly used item before inserting new item.

//both of Get and Set operation should be done in O(1) time(constant time)

/**
 * Solution:
 * For this problem, in order to achieve constant time operation,
 * we cannot use hashMap and list. Because if we use them, when we
 * move a key/value to the head of the list, we need to update all
 * other key/value pair's index in the hashmap, then it's O(n)
 * 
 * Instead, in order to achieve quick move/insert element, we need
 * to use a Doubly Linked List and HashMap.
 * 
 * Why we need a Doubly Linked List instead of a singly Linked List?
 * Because if the capacity is full, we need to move the end Node of 
 * the list, then set the end pointer to the new end, this requires
 * us to get the node before the end Node. So we need a pre Pointer in
 * each node.
 * 
 * In detail:
 * HashMap<Key,Node>
 * Node:
 * next, pre, value,key
 * int capacity, keep track of capacity
 * int len, keep track of the number of current nodes
 * 
 * Method:
 * RemoveNode(Node): remove a Node from the Doubly Linked List
 * SetHead(Node): Set a New Node to the head of the Doubly Linked List
 * Get(key)
 * Set(Key)
 * 
 * Note: SetHead method should always be used after RemoveHead method
*/


class Node{
    int value;
    int key;
    Node next;
    Node pre;
    public Node(int k, int v){
        this.key = k;
        this.value = v;
    }
}

public class LRUCache {
    private HashMap<Integer, Node> map = new HashMap<Integer,Node>();
    private Node head; //most recently used node
    private Node end; //least recently used node
    int capacity;
    int len;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.len = 0;
    }
    
    //remove a node from the list
    private void removeNode(Node node){
        Node curr = node;
        Node pre = node.pre;
        Node post = node.next;
        if(pre!=null)
            pre.next = post;
        else
            head = post;
        if(post!=null)
            post.pre = pre;
        else
            end = pre;
    }
    
    //set a NEW node to the head of list
    private void setHead(Node node){
        node.next = head;
        node.pre = null;
        if(head!=null)
            head.pre = node;
        head = node;
        if(end == null)
            end = node;
    }
    
    public int get(int key) {
        if(map.containsKey(key)){
            Node curr = map.get(key);
            int value = curr.value;
            removeNode(curr);
            setHead(curr);
            return value;
        }
        else
            return -1;
    }
    
    public void set(int key, int value) {
        if(map.containsKey(key)){
            Node curr = map.get(key);
            curr.value = value;
            removeNode(curr);
            setHead(curr);
        }
        else{
            Node newNode = new Node(key,value);
            if(len == capacity){
                Node last = end;
                removeNode(last);
                map.remove(last.key);
                map.put(key,newNode);
                setHead(newNode);
            }
            else{
                map.put(key,newNode);
                setHead(newNode);
                len++;
            }
        }
    }
}
