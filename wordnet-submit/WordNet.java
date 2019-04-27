import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by yixu on 2019/1/30.
 */
public class WordNet {

    private final Map<String, Bag<Integer>> map;  // word - IDs
    private final String[] synsets;
    // private Digraph digraph;
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

        // TODO：check if it's a rooted DAG

        sap = new SAP(digraph);
    }


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
