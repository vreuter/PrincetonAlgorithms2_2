package org.vreuter.algs2;

import edu.princeton.cs.Digraph;

/**
 * SAP (shortest ancestral path) ADT, motivated
 * by need to support WordNet application use case.
 *
 * @author Vince Reuter
 */
public class SAP {


    private final Digraph G;


    /**
     * Directed graph defines the SAP ADT.
     */
    public SAP(Digraph G) {
        this.G = G;
    }

    /**
     * Determing length of shortest ancestral path between v and w.
     * Return -1 if an ancestral path between v and w doesn't exist.
     *
     * @param v first vertex index
     * @param w second vertex index
     * @return lentgh of SAP between v and w; -1 if nonexistent
     */
    public int length(int v, int w)

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)

    // do unit testing of this class
    public static void main(String[] args)


}
