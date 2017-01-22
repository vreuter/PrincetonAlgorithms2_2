import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

// TODO: corner cases, docstrings, exceptions, problem description and info links.


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


    private static final String SYNSET_NOUNS_DELIMITER = " ";

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

        /* Parse the synset words and definitions. */
        int synsetID = 0;
        In parser = new In(synsets);
        SynsetLine synLine;
        while (parser.hasNextLine()) {
            synLine = new SynsetLine(parser.readLine());
            this.synsets.add(new Synset(synLine.gloss(), synLine.nouns()));
            // Update word lookup structure.
            this.indexWords(synsetID, synLine.nouns());
            synsetID++;
        }

        /* Connect the synsets by hypernym relationships. */
        // After parsing synsets, we know size.
        this.G = new Digraph(this.synsets.size());
        parser = new In(hypernyms);
        HypernymLine hypLine;
        int root = -1;
        int rootLine = -1;
        int currLine = 1;
        while (parser.hasNextLine()) {
            hypLine = new HypernymLine(parser.readLine());
            int synId = hypLine.id();
            Set<Integer> hypIds = hypLine.hypernyms();
            if (hypIds.isEmpty()) {
                if (root != -1) {
                    String errMsg = String.format(
                            "At least two root candidates (%d on line %d and %d on line %d)",
                            root, rootLine, synId, currLine);
                    throw new IllegalArgumentException(errMsg);
                }
                root = synId;
                rootLine = currLine;
            }
            else {
                for (int hypId : hypLine.hypernyms()) {
                    this.G.addEdge(synId, hypId);
                }
            }
            currLine++;
        }

        if (new DirectedCycle(this.G).hasCycle()) throw new IllegalArgumentException("Cyclic digraph");

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
        return String.join(SYNSET_NOUNS_DELIMITER,
                           this.synsets.get(ancestorHypernymID).words());
    }


    /* Throw IllegalArgumentException WordNet doesn't know given word. */
    private void validateWord(String word) {
        if (this.isNoun(word)) return;
        throw new IllegalArgumentException(
                String.format("WordNet doesn't know about '%s'", word)
        );
    }


    /* Update mapping from word to collection of its synset member IDs. */
    private void indexWords(int synId, String[] words) {
        for (String w : words) {
            if (!this.synIdsByWord.containsKey(w)) {
                this.synIdsByWord.put(w, new HashSet<Integer>());
            }
            this.synIdsByWord.get(w).add(synId);
        }
    }


    /* Crude testing */
    public static void main(String[] args) {

    }


}