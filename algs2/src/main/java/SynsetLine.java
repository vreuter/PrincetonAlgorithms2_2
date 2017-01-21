/**
 * Parsing details for synset definition file line.
 *
 * @author Vince Reuter
 */
public final class SynsetLine {


    /* Parsing constants */
    private static final String FIELDS_DELIMITER = ",";
    private static final String NOUNS_DELIMITER = ", ";
    private static final int INDEX_INDEX = 0;
    private static final int NOUNS_INDEX = 1;
    private static final int GLOSS_INDEX = 2;

    /* Data fields */
    private final int index;
    private final String[] nouns;
    private final String gloss;


    /**
     * Input line is parsed, and data
     * fields of interest are stored.
     *
     * @param line input line to parse
     */
    public SynsetLine(String line) {
        String[] fields = line.trim().split(DELIMITER);
        this.index = Integer.parseInt(fields[INDEX_INDEX]);
        this.nouns = fields[NOUNS_INDEX].split(NOUNS_DELIMITER);
        this.gloss = fields[GLOSS_INDEX];
    }


    /* Data accessors */
    public int index() {return this.index;}
    public String[] nouns() {return this.nouns;}
    public String gloss() {return this.gloss;}


}