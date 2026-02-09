package genome;

import java.util.ArrayList;
import java.util.List;

/**
 * IntervalTree is a data structure optimized for overlap queries.
 * 
 * It is based on a Binary Search Tree (BST) where:
 * - Nodes are ordered by their START position
 * - Each node stores 'maxEnd': the maximum END in its subtree
 * 
 * Key Operations:
 * - insert(): Add a new interval to the tree - O(log n) average case
 * - overlapSearch(): Find all intervals overlapping with query - O(log n + k)
 * where k is the number of overlapping intervals
 * 
 * The maxEnd optimization allows us to prune entire subtrees during search:
 * - If root.left.maxEnd < queryStart, we can skip the entire left subtree
 * 
 * @author DSA-EL Project
 */
public class IntervalTree {

    /** Root node of the interval tree */
    private IntervalNode root;

    /**
     * Constructs an empty Interval Tree.
     */
    public IntervalTree() {
        this.root = null;
    }

    /**
     * Inserts a new interval into the tree.
     * 
     * The interval is inserted based on its start position (BST property).
     * After insertion, maxEnd values are updated along the insertion path.
     * 
     * Time Complexity: O(log n) average, O(n) worst case (unbalanced)
     * Space Complexity: O(log n) for recursion stack
     * 
     * @param start    Start position of the interval
     * @param end      End position of the interval
     * @param geneName Name of the gene
     */
    public void insert(int start, int end, String geneName) {
        root = insertRecursive(root, start, end, geneName);
    }

    /**
     * Recursive helper for insert operation.
     * 
     * @param node     Current node being examined
     * @param start    Start position of new interval
     * @param end      End position of new interval
     * @param geneName Name of the gene
     * @return Updated node with new interval inserted
     */
    private IntervalNode insertRecursive(IntervalNode node, int start, int end, String geneName) {
        // Base case: found insertion point
        if (node == null) {
            return new IntervalNode(start, end, geneName);
        }

        // BST insertion based on start position
        if (start < node.start) {
            node.left = insertRecursive(node.left, start, end, geneName);
        } else {
            node.right = insertRecursive(node.right, start, end, geneName);
        }

        // Update maxEnd for the current node
        // This is the KEY optimization for interval trees!
        node.maxEnd = Math.max(node.maxEnd, end);

        return node;
    }

    /**
     * Checks if two intervals overlap.
     * 
     * Two intervals [s1, e1] and [s2, e2] overlap if and only if:
     * s1 <= e2 AND s2 <= e1
     * 
     * This is equivalent to: NOT (e1 < s2 OR e2 < s1)
     * i.e., they overlap if one doesn't end before the other starts
     * 
     * @param s1 Start of first interval
     * @param e1 End of first interval
     * @param s2 Start of second interval
     * @param e2 End of second interval
     * @return true if intervals overlap, false otherwise
     */
    private boolean isOverlapping(int s1, int e1, int s2, int e2) {
        return s1 <= e2 && s2 <= e1;
    }

    /**
     * Searches for all intervals overlapping with the query interval.
     * 
     * Time Complexity: O(log n + k)
     * - O(log n) to traverse down the tree
     * - O(k) to report k overlapping intervals
     * 
     * The key optimization is the maxEnd check:
     * - If left child's maxEnd < queryStart, skip entire left subtree
     * because no interval in left subtree can overlap with query
     * 
     * @param queryStart Start of query interval
     * @param queryEnd   End of query interval
     * @return List of all overlapping intervals
     */
    public List<IntervalNode> overlapSearch(int queryStart, int queryEnd) {
        List<IntervalNode> result = new ArrayList<>();
        overlapSearchRecursive(root, queryStart, queryEnd, result);
        return result;
    }

    /**
     * Recursive helper for overlap search.
     * 
     * Algorithm:
     * 1. Check if current node overlaps with query -> add to result
     * 2. If left child exists and left.maxEnd >= queryStart:
     * - There MIGHT be overlapping intervals in left subtree
     * - Recurse left
     * 3. Always recurse right (could have overlapping intervals)
     * 
     * Note: We always check right subtree because an interval with
     * start > current.start could still overlap with our query.
     * 
     * @param node       Current node being examined
     * @param queryStart Start of query interval
     * @param queryEnd   End of query interval
     * @param result     List to accumulate overlapping intervals
     */
    private void overlapSearchRecursive(IntervalNode node, int queryStart, int queryEnd, List<IntervalNode> result) {
        // Base case
        if (node == null) {
            return;
        }

        // Check if current interval overlaps with query
        if (isOverlapping(node.start, node.end, queryStart, queryEnd)) {
            result.add(node);
        }

        // KEY OPTIMIZATION: Only go left if there's a possibility of overlap
        // If left.maxEnd >= queryStart, there might be an overlapping interval
        if (node.left != null && node.left.maxEnd >= queryStart) {
            overlapSearchRecursive(node.left, queryStart, queryEnd, result);
        }

        // Always check right subtree
        // (intervals with larger start could still overlap)
        overlapSearchRecursive(node.right, queryStart, queryEnd, result);
    }

    /**
     * Returns the root of the tree (for testing/debugging).
     * 
     * @return Root node of the interval tree
     */
    public IntervalNode getRoot() {
        return root;
    }

    /**
     * Checks if the tree is empty.
     * 
     * @return true if tree has no nodes, false otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }
}
