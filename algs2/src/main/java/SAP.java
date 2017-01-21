import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


// TODO: exceptions, corner cases, docstrings


/**
 * SAP (shortest ancestral path) ADT, motivated
 * by need to support WordNet application use case.
 *
 * @author Vince Reuter
 */
public class SAP {


    // Length and ancestor sentinel for when there's no common ancestor
    private static final int NO_ANCESTOR = -1;

    // Underlying (very possibly non-acyclic) directed graph
    private final Digraph G;


    /**
     * Directed graph defines the SAP ADT.
     *
     * @param G directed graph to encoding data for this SAP instance
     */
    public SAP(Digraph G) {this.G = G;}


    /**
     * Determing length of shortest ancestral path between v and w.
     * Return -1 if an ancestral path between v and w doesn't exist.
     *
     * @param v index for one of the two SAP query vertices
     * @param w index for the other of two SAP query vertices
     * @return lentgh of SAP between v and w; -1 if nonexistent
     */
    public int length(int v, int w) {
        Paths sapComponents = sap(v, w);
        return sapComponents == null ? NO_ANCESTOR : sapComponents.length();
    }


    /**
     * Determine the common ancestor that creates the
     * shortest ancestral path from vertex v to vertex w.
     *
     * @param v one SAP query vertex index
     * @param w other SAP query vertex index
     * @return index of common ancestor that creates the SAP from v to w
     */
    public int ancestor(int v, int w) {
        Paths sapComponents = sap(v, w);
        return sapComponents == null ? NO_ANCESTOR : sapComponents.ancestor();
    }


    /**
     * Determing length of shortest ancestral path between
     * any vertex in v and any vertex in w. Return -1 if an
     * not a single common ancestor exists between any vertex
     * in v and any vertex in w.
     *
     * @param v first set of SAP query vertices
     * @param w other set of SAP query vertices
     * @return lentgh of SAP; -1 if nonexistent
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        Paths sapComponents = sap(v, w);
        return sapComponents == null ? NO_ANCESTOR : sapComponents.length();
    }


    /**
     * Determine the common ancestor that creates the
     * shortest ancestral path from any vertex in v to
     * any vertex in w. Return -1 if no such path exists.
     *
     * @param v first set of SAP query vertices
     * @param w other set of SAP query vertices
     * @return index of common ancestor that creates the SAP; -1 if nonexistent
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        Paths sapComponents = sap(v, w);
        return sapComponents == null ? NO_ANCESTOR : sapComponents.ancestor();
    }


    /**
     * Determine shortest path through common ancestor between v and w.
     * If v and w share no common ancestor, return null. Otherwise, return
     * a Paths ADT that stores each component path (v to ancestor and w to
     * ancestor), along with total path length and the common ancestor.
     *
     * @param v first query vertex
     * @param w other query vertex
     * @return null if no common ancestor, otherwise ancestor- and length-storing
     * ADT representing the shortest ancestral path
     */
    private Paths sap(int v, int w) {
        validate(v);
        validate(w);
        BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(this.G, w);
        return processPaths(fromV, fromW);
    }


    /**
     * Determine shortest path through common ancestor between any vertex
     * in v and any vertex in w. If no pair of vertices in which on vertex
     * comes from v and the other comes from w share a common ancestor,
     * return null. Otherwise, return a Paths ADT that stores each component
     * path (v to ancestor and w to ancestor), along with t
     * otal path length and the common ancestor.
     *
     * @param v first query vertex
     * @param w other query vertex
     * @return null if no common ancestor, otherwise ancestor- and
     * length-storing ADT representing the shortest ancestral path
     */
    private Paths sap(Iterable<Integer> v, Iterable<Integer> w) {
        for (int vertex : v) validate(vertex);
        for (int vertex : w) validate(vertex);
        BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(this.G, w);
        return processPaths(fromV, fromW);
    }


    /**
     * Following to BFS in G from one vertex and BFS in G from another vertex,
     * process results to determine the length of the shortest path through a
     * common ancestor between the two vertices from which the BFSs were begun.
     * The result is an ADT that stores the path from each of the two source
     * vertices to the common ancestor, along with the paths themselves and
     * total path length. The result is null if the BFS source vertices share
     * no common ancestor.
     *
     * @param fromV ADT of BFS results from a single vertex in G
     * @param fromW ADT of BFS results from another vertex in G
     * @return ADT that stores the path from each of the two source
     * vertices to the common ancestor, along with the paths themselves and
     * total path length, null if BFS source vertices share no common ancestor
     */
    private Paths processPaths(BreadthFirstDirectedPaths fromV, BreadthFirstDirectedPaths fromW) {
        List<Integer> common = new LinkedList<Integer>();
        for (int i = 0; i < G.V(); i++) {
            if (fromV.hasPathTo(i) && fromW.hasPathTo(i)) common.add(i);
        }

        if (common.size() == 0) return null;

        int minDist = Integer.MAX_VALUE;
        int closestAncestor = NO_ANCESTOR;
        int dist;
        for (int ancestor : common) {
            dist = fromV.distTo(ancestor) + fromW.distTo(ancestor);
            if (dist < minDist) {
                minDist = dist;
                closestAncestor = ancestor;
            }
        }

        return new Paths(fromV.pathTo(closestAncestor), fromW.pathTo(closestAncestor));
    }


    /* Per assignment specification, throw index OOB exception for low/high vertex. */
    private void validate(int v) {
        int upperBound = G.V() - 1;
        if (v < 0 || v > upperBound) {
            String errMsg = String.format("Need vertex v; 0 <= v <= %d; got %d",
                                          upperBound, v);
            throw new IndexOutOfBoundsException(errMsg);
        }
    }


    /** Store path from one vertex to nearest common ancestor shared with another vertex. */
    private final class Paths {

        // Components of path to nearest common ancestor
        private Iterable<Integer> fromA;
        private Iterable<Integer> fromB;

        // Nearest common ancestor
        private final int ancestor;

        // Length of path between vertices through nearest common ancestor
        private final int length;

        /* Process component paths to determine total path length and common ancestor. */
        private Paths(Iterable<Integer> fromA, Iterable<Integer> fromB) {

            this.fromA = fromA;
            this.fromB = fromB;

            ArrayList<Integer> aPath = path(fromA);
            ArrayList<Integer> bPath = path(fromB);

            /* Find component path lengths and common ancestor candidate. */
            int numA = aPath.size();
            int numB = bPath.size();
            int ancestorA = aPath.get(numA - 1);
            int ancestorB = bPath.get(numB - 1);

            // Validate that nearest ancestor really is shared/common.
            if (ancestorA != ancestorB) {
                String errMsg = String.format(
                        "Ancestor disagreement: %d and %d", ancestorA, ancestorB
                );
                throw new IllegalArgumentException(errMsg);
            }

            /* Set nearest common ancestor and length of path through it. */
            this.ancestor = ancestorA;
            this.length = numA + numB - 2;

        }

        /* Accessor methods for processed/computed data */
        public Iterator<Integer> fromA() {return this.fromA.iterator();}
        public Iterator<Integer> fromB() {return this.fromB.iterator();}
        public int ancestor() {return this.ancestor;}
        public int length() {return this.length;}

        /* Create ArrayList from Iterable. */
        private ArrayList<Integer> path(Iterable<Integer> p) {
            ArrayList<Integer> path = new ArrayList<Integer>();
            for (int i : p) {path.add(i);}
            return path;
        }

    }


    /* Crude testing */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }


}
