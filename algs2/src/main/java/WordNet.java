import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

// TODO: corner cases, docstrings, exceptions.


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


    private ArrayList<Synset> synsets;
    private Map<String, Set<Integer>> synIdsByWord;
    private Digraph G;


    /**
     * Create WordNet by parsing synsets and definitions,
     * then connecting synsets by hypernym relationships
     * defined within another file.
     *
     * @param synsets path to file defining the synonym sets
     * @param hypernyms path to file defining the hypernyms
     */
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) {
            throw new NullPointerException("At least one input file paths is null.");
        }

        this.synIdsByWord = new HashMap<String, Set<Integer>>();
        this.synsets = new ArrayList<Synset>();

        int synsetID = 0;
        In synsetParser = new In(synsets);
        SynsetLine synLine;

        /* Parse the synset words and definitions. */
        while (synsetParser.hasNextLine()) {
            synLine = new SynsetLine(synsetID, synsetParser.readLine());
            synsets.add(new Synset(synLine.gloss(), synLine.nouns()));
            // Update word lookup structure.
            this.indexWords(synsetID, synLine.nouns());
            synsetID++;
        }

        /* Connect the synsets by hypernym relationships. */
        // After parsing synsets, we know size.
        this.G = new Digraph(this.synsets.size());
        In hypernymParser = new In(hypernyms);
        while (hypernymParser.hasNextLine()) {
            hypLine = hypernymParser.readLine();
            int synID = hypLine.id();
            for (int hypId : hypLine.hypernyms()) {
                this.G.addEdge(synId, hypId);
            }
        }

        if (!isRootedDAG()) throw new IllegalArgumentException("Not a rooted DAG");

    }


    /**
     * Iterate over WordNet's words.
     *
     * @return iterable over WordNet's words
     */
    public Iterable<String> nouns() {
        return this.synIdsByWord.keySet();
    }


    /**
     * Determine whether WordNet knows the given word.
     *
     * @param word query word
     * @return whether WordNet knows given word
     */
    public boolean isNoun(String word) {
        return this.synIdsByWord.containsKey(word);
    }


    /**
     * Determine length of shortest path through a hypernym of each given word.
     *
     * @param nounA first word
     * @param nounB other word
     * @return length of shortest path through a hypernym of each given word
     * @throws IllegalArgumentException WordNet doesn't know both words
     */
    public int distance(String nounA, String nounB) {
        validateWord(nounA);
        validateWord(nounB);
        Iterable<Integer> aSynIds = this.synIdsByWord.get(nounA);
        Iterable<Integer> bSynIds = this.synIdsByWord.get(nounA);
        return new SAP(this.G).length(aSynIds, bSynIds);
    }


    /**
     * Determine the nearest hypernym shared by the given words.
     *
     * @param nounA first query word
     * @param nounB other query word
     * @return nearest hypernym shared by the given words
     * @throws IllegalArgumentException WordNet doesn't know both words
     */
    public String sap(String nounA, String nounB) {
        validateWord(nounA);
        validateWord(nounB);
        Iterable<Integer> aSynIds = this.synIdsByWord.get(nounA);
        Iterable<Integer> bSynIds = this.synIdsByWord.get(nounA);
        int ancestorHypernymID = new SAP(this.G).ancestor(aSynIds, bSynIds);
        return this.synsets.get(ancestorHypernymID);
    }


    /* After parsing input, determine if we have a rooted DAG as expected. */
    private boolean isRootedDAG() {
        // TODO: determine efficiency of DirectedCycle & rootedness detection.
        if (new DirectedCycle(this.G).hasCycle()) return false;
        boolean foundRoot = false;
        for (int v = 0; v < G.V() v++) {
            if (this.G.outdegree(v) == 0) {
                if (foundRoot) return false;
                foundRoot = true;
            }
        }
        return foundRoot;
    }


    /* Throw IllegalArgumentException WordNet doesn't know given word. */
    private void validateWord(String word) {
        if (this.isNoun(word)) return;
        throw new IllegalArgumentException(
                String.format("WordNet doesn't know about '%s'", word)
        );
    }


    /* Update mapping from word to collection of its synset member IDs. */
    private void indexWords(int synId, Iterable<String> words) {
        for (String w : words) {
            if (!this.synIdsByWord.contains(w)) {
                this.synIdsByWord.put(new HashSet<Integer>());
            }
            this.synIdsByWord.get(w).add(synId);
        }
    }


    /* Crude testing */
    public static void main(String[] args) {

    }


}