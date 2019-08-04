import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

/**
 * Shortest ancestral path
 *
 * Created by yixu on 2019/1/30.
 */
public class SAP {

    private final Digraph digraph;
    // private final Map<String, int[]> cache;     // v + "-" + w - [ancestor, length]

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new java.lang.IllegalArgumentException();
        digraph = new Digraph(G);
        // cache = new HashMap<>();
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        if (v == w) return 0;
/*
        int[] c;
        if ((c = cache.get(v + "-" + w)) != null || (c = cache.get(w + "-" + v)) != null)
            return c[1];
*/
        Bag<Integer> bagV = new Bag<>(), bagW = new Bag<>();
        bagV.add(v);
        bagW.add(w);
        return length(bagV, bagW);
    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        if (v == w) return v;
/*
        int[] c;
        if ((c = cache.get(v + "-" + w)) != null || (c = cache.get(w + "-" + v)) != null)
            return c[0];
*/
        Bag<Integer> bagV = new Bag<>(), bagW = new Bag<>();
        bagV.add(v);
        bagW.add(w);
        return ancestor(bagV, bagW);
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> vs, Iterable<Integer> ws) {
        return sapHelper(vs, ws)[1];
    }


    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> vs, Iterable<Integer> ws) {
        return sapHelper(vs, ws)[0];
    }


/*
    // return [ancestor, length]
    private int[] sapHelper(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertexes(v);
        validateVertexes(w);

        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(digraph, w);

        int ancestor = -1, length = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (pathV.hasPathTo(i) && pathW.hasPathTo(i)) {
                if (pathV.distTo(i) + pathW.distTo(i) < length) {
                    ancestor = i;
                    length = pathV.distTo(i) + pathW.distTo(i);
                }
            }
        }

        return new int[]{ancestor, ancestor == -1 ? -1 : length};
    }
*/


    // return [ancestor, length], bfs in lockstep
    private int[] sapHelper(Iterable<Integer> vs, Iterable<Integer> ws) {
        validateVertexes(vs);
        validateVertexes(ws);


        int n = digraph.V(), INFINITY = Integer.MAX_VALUE;
        int ancestor = -1, length = INFINITY;
/*
        // use cache
        int[] c;
        for (int v : vs) {
            for (int w : ws) {
                if (v == w)
                    return new int[]{v, 0};

                if ((c = cache.get(v + "-" + w)) != null || (c = cache.get(w + "-" + v)) != null) {
                    if (c[1] < length) {
                        ancestor = c[0];
                        length = c[1];
                    }
                }
            }
        }
*/
        boolean[] markedV = new boolean[n];
        int[] distToV = new int[n];
        boolean[] markedW = new boolean[n];
        int[] distToW = new int[n];
        for (int i = 0; i < n; i++) {
            distToV[i] = INFINITY;
            distToW[i] = INFINITY;
        }

        Queue<Integer> queueV = new Queue<>();
        for (int v : vs) {
            markedV[v] = true;
            distToV[v] = 0;
            queueV.enqueue(v);
        }
        Queue<Integer> queueW = new Queue<>();
        for (int w : ws) {
            markedW[w] = true;
            distToW[w] = 0;
            queueW.enqueue(w);
        }


        while (!queueV.isEmpty() || !queueW.isEmpty()) {
            for (int i = queueV.size(); i > 0; i--) {
                int v = queueV.dequeue();

                if (markedW[v] && (distToV[v] + distToW[v]) < length) {
                    ancestor = v;
                    length = distToV[v] + distToW[v];
                }

                for (int w : digraph.adj(v)) {
                    if (!markedV[w]) {
                        distToV[w] = distToV[v] + 1;
                        markedV[w] = true;
                        if (distToV[w] <= length)    // bfs optimization
                            queueV.enqueue(w);
                    }
                }
            }

            for (int i = queueW.size(); i > 0; i--) {
                int v = queueW.dequeue();

                if (markedV[v] && (distToV[v] + distToW[v]) < length) {
                    ancestor = v;
                    length = distToV[v] + distToW[v];
                }

                for (int w : digraph.adj(v)) {
                    if (!markedW[w]) {
                        distToW[w] = distToW[v] + 1;
                        markedW[w] = true;
                        if (distToW[w] <= length)    // bfs optimization
                            queueW.enqueue(w);
                    }
                }
            }
        }
/*
        // add to cache
        if (ancestor != -1) {
            int[] res = new int[]{ancestor, length};
            for (int v : vs) {
                for (int w : ws) {
                    cache.put(v + "-" + w, res);
                }
            }
            return res;
        } else {
            return new int[]{-1, -1};
        }
*/
        return new int[]{ancestor, ancestor == -1 ? -1 : length};
    }



    private void validateVertex(int v) {
        if (v < 0 || v >= digraph.V())
            throw new java.lang.IllegalArgumentException();
    }


    private void validateVertexes(Iterable<Integer> v) {
        if (v == null)
            throw new java.lang.IllegalArgumentException();

        int n = digraph.V();
        for (Integer i : v) {
            if (i == null || i < 0 || i >= n)
                throw new java.lang.IllegalArgumentException();
        }
    }


    public static void main(String[] args) {
        // do unit testing of this class
    }
}
