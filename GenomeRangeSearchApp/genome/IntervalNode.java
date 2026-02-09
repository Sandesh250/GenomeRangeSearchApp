package genome;

/**
 * IntervalNode represents a single node in an Interval Tree.
 * 
 * Each node stores:
 * - An interval [start, end] representing a genomic region
 * - The gene name associated with this interval
 * - maxEnd: the maximum endpoint in the subtree rooted at this node
 * (used for efficient pruning during overlap searches)
 * - References to left and right children
 * 
 * Time Complexity for operations:
 * - Access: O(1)
 * - Modification: O(1)
 * 
 * @author DSA-EL Project
 */
public class IntervalNode {

    /** Start position of the genomic interval */
    public int start;

    /** End position of the genomic interval */
    public int end;

    /**
     * Maximum end value in the subtree rooted at this node.
     * This is the key optimization for interval tree queries.
     * It allows us to prune entire subtrees that cannot contain overlapping
     * intervals.
     */
    public int maxEnd;

    /** Name of the gene (e.g., BRCA1, TP53) */
    public String geneName;

    /** Left child - contains intervals with smaller start positions */
    public IntervalNode left;

    /** Right child - contains intervals with larger start positions */
    public IntervalNode right;

    /**
     * Constructs a new IntervalNode with the given interval and gene name.
     * 
     * @param start    Start position of the interval
     * @param end      End position of the interval
     * @param geneName Name of the gene
     */
    public IntervalNode(int start, int end, String geneName) {
        this.start = start;
        this.end = end;
        this.geneName = geneName;
        this.maxEnd = end; // Initially, maxEnd equals the node's own end
        this.left = null;
        this.right = null;
    }

    /**
     * Returns a string representation of this interval.
     * Format: "GeneName [start, end]"
     * 
     * @return Formatted string representation
     */
    @Override
    public String toString() {
        return geneName + " [" + start + ", " + end + "]";
    }
}
