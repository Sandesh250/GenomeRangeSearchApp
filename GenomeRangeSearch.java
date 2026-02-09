import java.util.*;

// Interval Tree Node
class IntervalNode {
    int start, end;
    int maxEnd;
    String geneName;
    IntervalNode left, right;

    IntervalNode(int s, int e, String gene) {
        start = s;
        end = e;
        geneName = gene;
        maxEnd = e;
        left = right = null;
    }
}

public class GenomeRangeSearch {

    // Insert interval into Interval Tree
    static IntervalNode insert(IntervalNode root, int start, int end, String gene) {
        if (root == null) {
            return new IntervalNode(start, end, gene);
        }

        if (start < root.start) {
            root.left = insert(root.left, start, end, gene);
        } else {
            root.right = insert(root.right, start, end, gene);
        }

        root.maxEnd = Math.max(root.maxEnd, end);
        return root;
    }

    // Check if two intervals overlap
    static boolean isOverlap(int s1, int e1, int s2, int e2) {
        return s1 <= e2 && s2 <= e1;
    }

    // Search overlapping intervals using Interval Tree
    static void overlapSearch(IntervalNode root, int qs, int qe, List<String> result) {
        if (root == null) return;

        if (isOverlap(root.start, root.end, qs, qe)) {
            result.add(root.geneName + " [" + root.start + ", " + root.end + "]");
        }

        if (root.left != null && root.left.maxEnd >= qs) {
            overlapSearch(root.left, qs, qe, result);
        }

        overlapSearch(root.right, qs, qe, result);
    }

    // Naive Linear Search
    static void naiveSearch(List<IntervalNode> intervals, int qs, int qe) {
        System.out.println("\nNaive Search Results:");
        for (IntervalNode n : intervals) {
            if (isOverlap(n.start, n.end, qs, qe)) {
                System.out.println(n.geneName + " [" + n.start + ", " + n.end + "]");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        IntervalNode root = null;
        List<IntervalNode> naiveList = new ArrayList<>();

        // Sample Genome Dataset
        String[] genes = {"BRCA1", "TP53", "MYC", "EGFR", "KRAS"};
        int[][] intervals = {
            {100, 300},
            {250, 400},
            {500, 650},
            {700, 900},
            {850, 1000}
        };

        for (int i = 0; i < genes.length; i++) {
            root = insert(root, intervals[i][0], intervals[i][1], genes[i]);
            naiveList.add(new IntervalNode(intervals[i][0], intervals[i][1], genes[i]));
        }

        // User Query
        System.out.print("Enter query start position: ");
        int qs = sc.nextInt();

        System.out.print("Enter query end position: ");
        int qe = sc.nextInt();

        // Interval Tree Search
        List<String> result = new ArrayList<>();
        overlapSearch(root, qs, qe, result);

        System.out.println("\nInterval Tree Search Results:");
        if (result.isEmpty()) {
            System.out.println("No overlapping genes found.");
        } else {
            for (String s : result) {
                System.out.println(s);
            }
        }

        // Naive Search Comparison
        naiveSearch(naiveList, qs, qe);

        sc.close();
    }
}
