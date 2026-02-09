package genome;

/**
 * IntervalNode represents a single node in an Interval Tree.
 * 
 * Each node stores:
 * - An interval [start, end] representing a genomic region
 * - The gene name and extended metadata
 * - maxEnd: the maximum endpoint in the subtree rooted at this node
 * - References to left and right children
 * 
 * @author DSA-EL Project
 */
public class IntervalNode {

    /** Start position of the genomic interval */
    public int start;

    /** End position of the genomic interval */
    public int end;

    /** Maximum end value in the subtree rooted at this node */
    public int maxEnd;

    /** Name of the gene (e.g., BRCA1, TP53) */
    public String geneName;

    /** Chromosome location (e.g., Chr17, Chr8) */
    public String chromosome;

    /** Gene type (e.g., Protein Coding) */
    public String type;

    /** Biological function */
    public String function;

    /** Associated disease */
    public String diseaseAssociation;

    /** Applications in research/medicine */
    public String applications;

    /** Left child */
    public IntervalNode left;

    /** Right child */
    public IntervalNode right;

    /**
     * Constructs a new IntervalNode with basic info (for backward compatibility).
     */
    public IntervalNode(int start, int end, String geneName) {
        this(start, end, geneName, "", "", "", "", "");
    }

    /**
     * Constructs a new IntervalNode with full metadata.
     */
    public IntervalNode(int start, int end, String geneName, String chromosome,
            String type, String function, String diseaseAssociation, String applications) {
        this.start = start;
        this.end = end;
        this.geneName = geneName;
        this.chromosome = chromosome;
        this.type = type;
        this.function = function;
        this.diseaseAssociation = diseaseAssociation;
        this.applications = applications;
        this.maxEnd = end;
        this.left = null;
        this.right = null;
    }

    @Override
    public String toString() {
        return geneName + " [" + start + ", " + end + "]";
    }
}
