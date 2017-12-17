package trees;

import java.util.*;

import graph.*;

public class SpanningTree {
    
    public static Collection<Edge> kruskal(UnionFind u, EuclideanGraph g){
        //we cast the Edges list to an ArrayList
        //to guarantee constant-time access by index
    	ArrayList<Edge> edges = new ArrayList<>(g.getAllEdges());
        EdgeComparator ac = new EdgeComparator();
        Collections.sort(edges,ac);

        ArrayList<Edge> forest = new ArrayList<>();

        //we go through the edges in g, as they're already sorted in increasing order
        Place beg, end;
        Edge curEdge;
        for(int i=0; i<edges.size(); i++) {
            //the edges.get operation is done in O(1) time
            curEdge = edges.get(i);
            beg = curEdge.source;
            end = curEdge.target;

            //now check if the beginning and end are currently in the same connected component
            if (u.find(beg) != u.find(end)) {
                forest.add(edges.get(i));
                u.union(beg,end);
            }
        }

    	return forest;
    }

    public static Collection<Collection<Edge>> kruskal(EuclideanGraph g){
    	UnionFind u = new UnionFind();
    	Collection<Edge> forest = kruskal(u, g);
    	HashMap<Place,Collection<Edge>> edgeList = new HashMap<>();

    	Collection<Edge> curComponent;
        for(Edge e: forest) {
            curComponent = edgeList.get(u.find(e.source));
            if (curComponent == null) {
                edgeList.put(u.find(e.source),
                        new ArrayList<>(Arrays.asList(e)));
            } else {
                curComponent.add(e);
            }
        }
    	return edgeList.values();
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
