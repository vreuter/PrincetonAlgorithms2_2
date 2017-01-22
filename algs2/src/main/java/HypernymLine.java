import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Parsing details for hypernym definition file line.
 *
 * @author Vince Reuter
 */
final class HypernymLine {


    private static final String FIELDS_DELIMITER = ",";
    private static final String HYPERNYM_SETS_DELIMITER = ",";
    private static final int ID_INDEX = 0;
    private static final int HYPERNYM_SETS_INDEX = 1;

    private final int id;
    private final Set<Integer> hypIds;

    public HypernymLine(String line) {

        String[] fields = line.trim().split(FIELDS_DELIMITER, 2);

        this.id = Integer.parseInt(fields[ID_INDEX]);

        this.hypIds = new HashSet<Integer>();
        if (fields.length == 1) return;         // No hypernyms
        for (String hypIdText : fields[HYPERNYM_SETS_INDEX].split(HYPERNYM_SETS_DELIMITER)) {
            this.hypIds.add(Integer.parseInt(hypIdText));
        }

    }


    public int id() {return this.id;}
    public Set<Integer> hypernyms() {return Collections.unmodifiableSet(this.hypIds);}


}