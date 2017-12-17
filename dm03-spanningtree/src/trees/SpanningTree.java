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
        EdgeComparator ac = new EdgeComparator();
    	PriorityQueue<Edge> q = new PriorityQueue<>(ac);
    	q.addAll(g.edgesOut(start));
    	nonVisited.remove(start);

    	//since q is a priority queue we have to exhaust, we'll keep the values we go through in a Collection<Edge> called result
    	LinkedList<Edge> result = new LinkedList<>();

    	Edge a;
    	Place u;
    	while (!q.isEmpty()) {
    	    a = q.poll();
    	    u = a.target;
    	    if (nonVisited.contains(u)) {
    	        //if we haven't visited u yet, then we can add the ingoing Edge a to our result
    	        result.add(a);
    	        q.addAll(g.edgesOut(u));
    	        nonVisited.remove(u);
            }
        }

    	return result;
    }
    
    public static Collection<Collection<Edge>> primForest(EuclideanGraph g){
    	HashSet<Place> nonVisited = new HashSet<>(g.places());

    	LinkedList<Collection<Edge>> result = new LinkedList<>();

    	Place p;
    	while (!nonVisited.isEmpty()) {
    	    p = setPeek(nonVisited);
    	    result.add(primTree(nonVisited, p, g));
        }

    	return result;
    }

    /**
     * Auxiliary method to peek inside a HashSet without removing anything
     * Not very elegant...
     * @param placeHashSet
     * @return the first element the iterator method on the HashSet can get
     */
    private static Place setPeek(HashSet<Place> placeHashSet) {
        return placeHashSet.iterator().next();
    }
    
   
}
