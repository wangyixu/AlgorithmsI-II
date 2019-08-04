import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.Map;

/**
 *
    ASSESSMENT SUMMARY

    Compilation:  PASSED
    API:          PASSED

    Spotbugs:     PASSED
    PMD:          FAILED (1 warning)
    Checkstyle:   FAILED (0 errors, 1 warning)

    Correctness:  36/36 tests passed
    Memory:       4/4 tests passed
    Timing:       27/27 tests passed

    Aggregate score: 100.00%
 *
 * Created by yixu on 2019/1/30.
 */
public class WordNet {

    private final Map<String, Bag<Integer>> map;  // word - IDs
    private final String[] synsets;
    private final SAP sap;

    // constructor takes the name of the two input files: synsets, hypernyms
    public WordNet(String syn, String hyp) {
        if (syn == null || hyp == null)
            throw new java.lang.IllegalArgumentException("Input is null");

        In in = new In(syn);
        String[] lines = in.readAllLines();
        map = new HashMap<>(lines.length << 1);
        synsets = new String[lines.length];
        String[] strs;
        Bag<Integer> bag;
        for (String line : lines) {
            strs = line.split(",");
            // System.out.println(Arrays.toString(strs));

            int id = Integer.parseInt(strs[0]);
            synsets[id] = strs[1];
            for (String synset: strs[1].split(" ")) {
                bag = map.computeIfAbsent(synset, k -> new Bag<>());
                bag.add(id);
            }
        }

        Digraph digraph = new Digraph(lines.length);
        in = new In(hyp);
        while (in.hasNextLine()) {
            strs = in.readLine().split(",");

            int id = Integer.parseInt(strs[0]);
            for (int i = 1; i < strs.length; i++) {
                digraph.addEdge(id, Integer.parseInt(strs[i]));
            }
        }

        /*
        // check if it's a rooted DAG
        int rootCnt = 0;
        boolean[] marked = new boolean[digraph.V()];
        boolean[] onStack = new boolean[digraph.V()];
        // Call the recursive helper function to detect cycle in different DFS trees
        for (int v = 0; v < digraph.V(); v++) {
            if (digraph.outdegree(v) == 0)  // root
                rootCnt++;

            if (dfsCycleHelper(digraph, v, marked, onStack))
                throw new java.lang.IllegalArgumentException();
        }
        if (rootCnt != 1)
            throw new java.lang.IllegalArgumentException();
*/

        // check if it's a rooted DAG
        int rootCnt = 0;
        for (int v = 0; v < digraph.V(); v++) {
            if (digraph.outdegree(v) == 0)  // root
                rootCnt++;
        }
        if (rootCnt != 1)
            throw new java.lang.IllegalArgumentException();

        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle())
            throw new java.lang.IllegalArgumentException();


        sap = new SAP(digraph);
    }

/*
    private boolean dfsCycleHelper(Digraph digraph, int v, boolean[] marked, boolean[] onStack) {
        if (onStack[v]) return true;
        if (marked[v]) return false;

        onStack[v] = true;
        marked[v] = true;

        for (int w : digraph.adj(v)) {
            if (dfsCycleHelper(digraph, w, marked, onStack))
                return true;
        }

        onStack[v] = false;
        return false;
    }
*/

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }


    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new java.lang.IllegalArgumentException();
        return map.containsKey(word);
    }


    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException();

        return sap.length(map.get(nounA), map.get(nounB));
    }


    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException();

        return synsets[sap.ancestor(map.get(nounA), map.get(nounB))];
    }



    public static void main(String[] args) {
        // do unit testing of this class
    }
}
