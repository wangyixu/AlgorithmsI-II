import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
     ASSESSMENT SUMMARY

     Compilation:  PASSED
     API:          PASSED

     Spotbugs:     PASSED
     PMD:          PASSED
     Checkstyle:   PASSED

     Correctness:  23/23 tests passed
     Memory:       4/4 tests passed
     Timing:       1/1 tests passed

     Aggregate score: 100.00%
 *
 * Created by yixu on 2019/7/28.
 */
public class BaseballElimination {

    private final int n; // numOfTeams

    private final String[] teams;
    private final Map<String, Integer> teamToIndex;

    private final int[] win;
    private final int[] lose;
    private final int[] remain;
    private final int[][] games;

    private final List<List<String>> subsets;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        // read and store input
        In in = new In(filename);
        n = in.readInt();

        teams = new String[n];
        teamToIndex = new HashMap<>();

        win = new int[n];
        lose = new int[n];
        remain = new int[n];
        games = new int[n][n];

        for (int i = 0; i < n; i++) {
            teams[i] = in.readString();
            teamToIndex.put(teams[i], i);

            win[i] = in.readInt();
            lose[i] = in.readInt();
            remain[i] = in.readInt();

            for (int j = 0; j < n; j++) {
                games[i][j] = in.readInt();
            }
        }

        // calculate and store results
        subsets = new ArrayList<>(n);
        List<String> subset;
        for (int i = 0; i < n; i++) {

            subset = new ArrayList<>();

            // construct right side FlowNetwork
            int numOfAgainst = ((n - 1) * (n - 2)) >> 1;
            FlowNetwork G = new FlowNetwork(numOfAgainst + n + 1);
            int s = i, t = n;
            int capacity;
            boolean isObviouslyEliminated = false;
            for (int j = 0; j < n; j++) {   // n-1 team vertexes
                if (j != i) {
                    capacity = win[i] + remain[i] - win[j];
                    if (capacity < 0) {
                        subset.add(teams[j]);
                        isObviouslyEliminated = true;
                    } else {
                        G.addEdge(new FlowEdge(j, t, capacity));
                    }
                }
            }

            // construct left side FlowNetwork and use FordFulkerson to compute
            if (!isObviouslyEliminated) {
                int idx = n + 1;
                for (int j = 0; j < n; j++) {
                    if (j == i) continue;
                    for (int k = j + 1; k < n; k++) {
                        if (k != i) {
                            G.addEdge(new FlowEdge(s, idx, games[j][k]));
                            G.addEdge(new FlowEdge(idx, k, Double.POSITIVE_INFINITY));
                            G.addEdge(new FlowEdge(idx, j, Double.POSITIVE_INFINITY));
                            idx++;
                        }
                    }
                }

                FordFulkerson maxflow = new FordFulkerson(G, s, t);
//                System.out.println(G.toString());

                for (FlowEdge edge : G.adj(s)) {
                    // eliminated
                    if (Double.compare(edge.flow(), edge.capacity()) != 0) {
                        for (int j = 0; j < n; j++) {
                            // flow == capacity, can't arrange anymore
                            if (j != i && maxflow.inCut(j)) {
                                subset.add(teams[j]);
                            }
                        }
                        break;
                    }
                }
            }

            subsets.add(subset.isEmpty() ? null : subset);
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    // number of wins for given team
    public int wins(String team) {
        validateTeam(team);
        return win[teamToIndex.get(team)];
    }

    private void validateTeam(String team) {
        if (!teamToIndex.containsKey(team))
            throw new IllegalArgumentException();
    }

    // number of losses for given team
    public int losses(String team) {
        validateTeam(team);
        return lose[teamToIndex.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validateTeam(team);
        return remain[teamToIndex.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return games[teamToIndex.get(team1)][teamToIndex.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validateTeam(team);
        return subsets.get(teamToIndex.get(team)) != null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        return subsets.get(teamToIndex.get(team));
    }

    public static void main(String[] args) {
//        BaseballElimination division = new BaseballElimination(args[0]);
        BaseballElimination division = new BaseballElimination("teams5.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
