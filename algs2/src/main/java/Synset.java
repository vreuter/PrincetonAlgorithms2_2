import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Collection of synonyms with a definition for WordNet.
 *
 * @author Vince Reuter
 */
public class Synset {

    private final String gloss;
    private Set<String> words;

    /**
     * Create an empty synset with the given definition.
     *
     * @param gloss meaning for the new, empty synset
     */
    public Synset(String gloss) {
        this.gloss = gloss;
        this.words = new HashSet<String>();
    }

    /**
     * Create synset with given definition and some initial word members.
     *
     * @param definintion meaning for the new synset
     * @param words some initial synset members
     */
    public Synset(String gloss, String[] words) {
        this(gloss);
        this.add(words);
    }

    /**
     * Get this synset's meaning.
     *
     * @return this synset's meaning
     */
    public String gloss() {return this.gloss;}

    /**
     * Get (unmodifiable view of) this synset's words.
     *
     * @return unmodifiable view of this synset's words
     */
    public Set<String> words() {return Collections.unmodifiableSet(this.words);}

    /**
     * Add a word to this synset.
     *
     * @param word word to add
     * @return flag indicating whether word was added (false if already included)
     */
    public boolean add(String word) {return this.words.add(word);}

    /**
     * Add multiple words to this synset.
     *
     * @param words new members for the synset
     */
    public void add(String[] words) {
        for (String w : words) {this.words.add(w);}
    }


}