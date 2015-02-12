/**
 * Definition for undirected graph.
 * class UndirectedGraphNode {
 *     int label;
 *     List<UndirectedGraphNode> neighbors;
 *     UndirectedGraphNode(int x) { label = x; neighbors = new ArrayList<UndirectedGraphNode>(); }
 * };
 * 
 * Solution:
 * use a queue to do BFS on the graph
 * use a HashMap<Node, Node> to store each Node and its copy
 * Also use the HashMap to check if a Node is already been visited
 * For each new node, create a copy of itself and a list of neighbours
 * 
 * Details:
 * edge case: check if input node is null, if it is, return null
 * 
 * put the head node into queue, then create a copy of it, put
 * head node and its copy to hashmap.
 * 
 * In the while loop, remove a current node from queue, 
 * for each neighbor, check if the neighbor is already in hashmap.
 * If it's not, create a copy of the neighbor, put the neighbor and
 * its copy to hashmap. Then add the copy to the neighbors list of current
 * node. Then add the neighbor itself to the queue.
 * 
 * After the loop, return the copy of the head node.
 */
public class Solution {
    public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        //handle edge case
        if(node == null) 
            return null;
        Queue<UndirectedGraphNode> q = new LinkedList<UndirectedGraphNode>();
        HashMap<UndirectedGraphNode, UndirectedGraphNode> map = new
        HashMap<UndirectedGraphNode, UndirectedGraphNode>();
        q.add(node);
        UndirectedGraphNode newNode = new UndirectedGraphNode(node.label);
        map.put(node,newNode);
        while(!q.isEmpty()){
            UndirectedGraphNode currNode = q.remove();
            List<UndirectedGraphNode> neighbors = currNode.neighbors;
            for(UndirectedGraphNode neighbor : currNode.neighbors){
                if(!map.containsKey(neighbor)){
                    q.add(neighbor);
                    UndirectedGraphNode copyNeighbor = new UndirectedGraphNode(neighbor.label);
                    map.put(neighbor, copyNeighbor);
                    map.get(currNode).neighbors.add(copyNeighbor);
                }
                else
                    map.get(currNode).neighbors.add(map.get(neighbor));
            }
        }
        return newNode;
        
    }
}
