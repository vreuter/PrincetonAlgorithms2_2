/**
 * Parsing details for synset definition file line.
 *
 * @author Vince Reuter
 */
final class SynsetLine {


    /* Parsing constants */
    private static final String FIELDS_DELIMITER = ",";
    private static final String NOUNS_DELIMITER = " ";
    private static final int NOUNS_INDEX = 1;
    private static final int GLOSS_INDEX = 2;

    /* Data fields */
    private final String[] nouns;
    private final String gloss;


    /**
     * Input line is parsed, and data
     * fields of interest are stored.
     *
     * @param line input line to parse
     */
    public SynsetLine(String line) {
        String[] fields = line.split(FIELDS_DELIMITER);
        this.nouns = fields[NOUNS_INDEX].split(NOUNS_DELIMITER);
        this.gloss = fields[GLOSS_INDEX];
    }


    /* Data accessors */
    public String gloss() {return this.gloss;}
    public String[] nouns() {return this.nouns;}


}