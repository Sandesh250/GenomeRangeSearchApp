package genome;

import java.util.ArrayList;
import java.util.List;

/**
 * IntervalTree is a data structure optimized for overlap queries.
 * 
 * Time Complexity: O(log n + k) for search where k = results
 * 
 * @author DSA-EL Project
 */
public class IntervalTree {

    private IntervalNode root;

    public IntervalTree() {
        this.root = null;
    }

    /**
     * Insert with basic info (backward compatibility)
     */
    public void insert(int start, int end, String geneName) {
        root = insertRecursive(root, new IntervalNode(start, end, geneName));
    }

    /**
     * Insert with full metadata
     */
    public void insert(int start, int end, String geneName, String chromosome,
            String type, String function, String diseaseAssociation, String applications) {
        root = insertRecursive(root, new IntervalNode(start, end, geneName, chromosome,
                type, function, diseaseAssociation, applications));
    }

    private IntervalNode insertRecursive(IntervalNode node, IntervalNode newNode) {
        if (node == null) {
            return newNode;
        }

        if (newNode.start < node.start) {
            node.left = insertRecursive(node.left, newNode);
        } else {
            node.right = insertRecursive(node.right, newNode);
        }

        node.maxEnd = Math.max(node.maxEnd, newNode.end);
        return node;
    }

    private boolean isOverlapping(int s1, int e1, int s2, int e2) {
        return s1 <= e2 && s2 <= e1;
    }

    public List<IntervalNode> overlapSearch(int queryStart, int queryEnd) {
        List<IntervalNode> result = new ArrayList<>();
        overlapSearchRecursive(root, queryStart, queryEnd, result);
        return result;
    }

    private void overlapSearchRecursive(IntervalNode node, int queryStart, int queryEnd, List<IntervalNode> result) {
        if (node == null)
            return;

        if (isOverlapping(node.start, node.end, queryStart, queryEnd)) {
            result.add(node);
        }

        if (node.left != null && node.left.maxEnd >= queryStart) {
            overlapSearchRecursive(node.left, queryStart, queryEnd, result);
        }

        overlapSearchRecursive(node.right, queryStart, queryEnd, result);
    }

    public IntervalNode getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return root == null;
    }
}
