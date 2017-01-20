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

/**
 * SAP (shortest ancestral path) ADT, motivated
 * by need to support WordNet application use case.
 *
 * @author Vince Reuter
 */
public class SAP {


    private static final int NO_ANCESTOR = -1;
    private final Digraph G;


    /**
     * Directed graph defines the SAP ADT.
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


    private Paths sap(int v, int w) {
        BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(this.G, w);
        return processPaths(fromV, fromW);
    }


    private Paths sap(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(this.G, w);
        return processPaths(fromV, fromW);
    }


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


    private final class Paths {

        private Iterable<Integer> fromA;
        private Iterable<Integer> fromB;
        private final int ancestor;
        private final int length;

        private Paths(Iterable<Integer> fromA, Iterable<Integer> fromB) {

            this.fromA = fromA;
            this.fromB = fromB;

            ArrayList<Integer> aPath = path(fromA);
            ArrayList<Integer> bPath = path(fromB);

            int numA = aPath.size();
            int numB = bPath.size();
            int ancestorA = aPath.get(numA - 1);
            int ancestorB = bPath.get(numB - 1);

            if (ancestorA != ancestorB) {
                String errMsg = String.format(
                        "Ancestor disagreement: %d and %d", ancestorA, ancestorB
                );
                throw new IllegalArgumentException(errMsg);
            }
            this.ancestor = ancestorA;
            this.length = numA + numB - 1;

        }

        private ArrayList<Integer> path(Iterable<Integer> p) {
            ArrayList<Integer> path = new ArrayList<Integer>();
            for (int i : p) {path.add(i);}
            return path;
        }

        public Iterator<Integer> fromA() {return this.fromA.iterator();}
        public Iterator<Integer> fromB() {return this.fromB.iterator();}
        public int ancestor() {return this.ancestor;}
        public int length() {return this.length;}

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
