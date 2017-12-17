package trees;

import graph.Place;

import java.util.HashMap;
import java.util.LinkedHashMap;

// Q1

public class UnionFind {
	//parent relation, parent.put(src,dst) indicates that src points to dst
    private HashMap<Place,Place> parent;
    private LinkedHashMap<Place,Integer> rank;

    //maximum acceptable distance to root element
    //we pick the very conservative value of 5
    private int MAX_DIST = 5;
    //ranking of representatives
    
    public UnionFind( ){
        parent = new HashMap<>();
        rank = new LinkedHashMap<>();
    }
    
    public Place find( Place src ){
        Place root = parent.get(src);
        //if in fact src had no parent yet, then set it as its own ancestor and return it
        if (root == null) {
            parent.put(src,src);
            rank.put(src,0);
            return src;
        }
        int dist = 1;
        while (!(root.equals(parent.get(root)))) {
            root = parent.get(root);
            dist++;
        }
        if (dist > MAX_DIST) parent.put(src,root);
    	return root;
    }
    
    public void union( Place v0, Place v1 ){
        Place root_v0 = this.find(v0);
        Place root_v1 = this.find(v1);
        if (!root_v0.equals(root_v1)) {
            if (rank.get(root_v0) > rank.get(root_v1)) {
                parent.put(root_v1,root_v0);
            } else if (rank.get(root_v0) < rank.get(root_v1)) {
                parent.put(root_v0,root_v1);
            } else {
                parent.put(root_v0,root_v1);
                rank.put(root_v1,rank.get(root_v1)+1);
            }
        }
    	return;
    }
}
