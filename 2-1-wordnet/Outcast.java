import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 *
 * Created by yixu on 2019/4/26.
 */
public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }


    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String res = "";
        int maxDistance = -1;

        for (String noun : nouns) {

            int sumDistance = 0;
            for (String other : nouns)
                sumDistance += wordnet.distance(noun, other);

            if (sumDistance > maxDistance) {
                maxDistance = sumDistance;
                res = noun;
            }
        }

        return res;
    }


    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
