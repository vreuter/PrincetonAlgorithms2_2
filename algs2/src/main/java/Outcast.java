import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


/**
 * Detect the most dissimilar word from a group of words.
 * Dissimilarity is defined by minimmal common ancestral
 * distance within WordNet, with a common ancestor between
 * a pair of words being a hypernym shared by the word pair.
 *
 * @author Vince Reuter
 */
public final class Outcast {


    private static final int SYNSET_FILEPATH_INDEX = 0;
    private static final int HYPERNYM_FILEPATH_INDEX = 1;
    private static final int OUTCAST_FILEPATHS_INDEX_START = 2;

    private final WordNet wordnet;


    /**
     * Supply dissimilar word detective with a WordNet.
     *
     * @param wordnet data with which to detect outcast
     */
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }


    /**
     * Find most distinct word within given group. Assume at
     * least two words are provided, and that each given word
     * is a noun that's known to WordNet.
     *
     * @param nouns group of words within which to find outcast
     * @return word in given group least similar to others
     */
    public String outcast(String[] nouns) {

        int[] distances = new int[nouns.length];

        /* Iterate over word pairs to get total distance for each word. */
        for (int i = 0; i < nouns.length - 1; i++) {
            for (int j = i + 1; j < nouns.length; j++) {
                int d = this.wordnet.distance(nouns[i], nouns[j]);
                distances[i] += d;
                distances[j] += d;
            }
        }

        int maxDist = Integer.MIN_VALUE;
        int maxDistIndex = 0;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] > maxDist) {
                maxDist = distances[i];
                maxDistIndex = i;
            }
        }

        return nouns[maxDistIndex];

    }


    /* Crude testing, taken from assignment specification */
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[SYNSET_FILEPATH_INDEX],
                                      args[HYPERNYM_FILEPATH_INDEX]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = OUTCAST_FILEPATHS_INDEX_START; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }


}