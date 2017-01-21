import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;


/**
 * English-language semantic lexicon representing
 * relationships between words. A relationship may
 * indicate synonyms (synset of words), or hypernyms
 * (a set of synonyms that encompasses another). This
 * ADT uses a directed graph to represent WordNet.
 *
 * @author Vince Reuter
 */
public class WordNet {


    /**
     *
     *
     * @param synsets path to file defining the synonym sets
     * @param hypernyms path to file defining the hypernyms
     */
    public WordNet(String synsets, String hypernyms) {
        In synsetParser = new In(synsets);
        In hypernymParser = new In(hypernyms);

    }


    public Iterable<String> nouns() {

    }


    public boolean isNoun(String word) {

    }


    public int distance(String nounA, String nounB) {

    }


    public String sap(String nounA, String nounB) {

    }


    /* Crude testing */
    public static void main(String[] args) {

    }


}