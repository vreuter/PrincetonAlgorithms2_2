import edu.princeton.cs.algs4.Digraph;

/**
 * Implementation of BFS tailored to finding shortest
 * ancestral path (SAP) between a pair of digraph vertices.
 *
 * @author Vince Reuter
 * @version 0.0
 */
public class PairedShortCircuitBFS {


    public final class Paths {

        private final Iterable<Integer> fromA;
        private final Iterable<Integer> fromB;
        private final int length;

        public Paths(Iterable<Integer> fromA, Iterable<Integer> fromB) {
            this.fromA = fromA;
            this.fromB = fromB;
            this.length = fromA.iterator().size() + fromB.iterator.size() - 1;
        }

        public Iterable<Integer> fromA() {return this.fromA;}
        public Iterable<Integer> fromB() {return this.fromB;}
        public int length() {return this.length;}

    }


    private final Digraph G;

    public PairedShortCircuitBFS(Digraph G) {this.G = G;}

    public Paths sapPaths(int v1, int v2) {

    }


}