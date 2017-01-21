import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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


    private ArrayList<Synset> synsets;
    private Map<String, Set<Integer>> synIdsByWord;
    private Digraph G;


    /**
     *
     *
     * @param synsets path to file defining the synonym sets
     * @param hypernyms path to file defining the hypernyms
     */
    public WordNet(String synsets, String hypernyms) {

        this.synIdsByWord = new HashMap<String, Set<Integer>>();
        this.synsets = new ArrayList<Synset>();

        int synsetID = 0;
        In synsetParser = new In(synsets);
        SynsetLine synLine;

        while (synsetParser.hasNextLine()) {
            synLine = new SynsetLine(synsetID, synsetParser.readLine());
            synsets.add(new Synset(synLine.gloss(), synLine.nouns()));
            this.indexWords(synsetID, synLine.nouns());
            synsetID++;
        }

        this.G = new Digraph(this.synsets.size());
        In hypernymParser = new In(hypernyms);
        while (hypernymParser.hasNextLine()) {
            hypLine = hypernymParser.readLine();
            int synID = hypLine.id();
            for (int hypId : hypLine.hypernyms()) {
                this.G.addEdge(synId, hypId);
            }
        }

    }

    
    private void indexWords(int synId, Iterable<String> words) {
        for (String w : words) {
            if (!this.synIdsByWord.contains(w)) {
                this.synIdsByWord.put(new HashSet<Integer>());
            }
            this.synIdsByWord.get(w).add(synId);
        }
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