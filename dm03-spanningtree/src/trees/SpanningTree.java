package trees;

import java.util.*;

import graph.*;

public class SpanningTree {
    
    public static Collection<Edge> kruskal(UnionFind u, EuclideanGraph g){
    	List<Edge> edges = g.getAllEdges();
        EdgeComparator ac = new EdgeComparator();
        Collections.sort(edges,ac);

        LinkedList<Edge> forest = new LinkedList<>();

        //we go through the edges in g, as they're already sorted in order
        Place beg, end;
        for(int i=0; i<edges.size(); i++) {
            beg = edges.get(i).source;
            end = edges.get(i).target;

            //now check if the beginning and end are currently in the same connected component
            if (u.find(beg) != u.find(end)) {
                forest.add(edges.get(i));
                u.union(beg,end);
            }
        }

    	return forest;
    }

    public static Collection<Collection<Edge>> kruskal(EuclideanGraph g){
    	// Q3
    	return null;
    }
    
    public static Collection<Edge> primTree(HashSet<Place> nonVisited, Place start, EuclideanGraph g){
    	// Q4
    	return null;
    }
    
    public static Collection<Collection<Edge>> primForest(EuclideanGraph g){
    	// Q5
    	return null;
    }
    
   
}
