import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {

    private class searchNode implements Comparable<searchNode> {
        private Board curr;
        private searchNode predecessor;
        private int moves;
        private int priority;

        public searchNode(Board bd, searchNode pre, int m) {
            curr = bd;
            predecessor = pre;
            moves = m;
            priority = m + bd.manhattan();
        }

        public boolean isGoal() {
            return curr.isGoal();
        }

        public Iterable<searchNode> neighbors() {
            Stack<searchNode> neighbors = new Stack<>();
            for (Board neighbor: curr.neighbors()) {
                if (moves == 0 || !predecessor.getCurr().equals(neighbor)) {
                    searchNode e = new searchNode(neighbor, this, moves+1);
                    neighbors.push(e);
                }
            }
            return neighbors;
        }

        public int moves() {
            return moves;
        }

        public Board getCurr() {
            return curr;
        }

        public searchNode getPredecessor() {
            return predecessor;
        }

        public int compareTo(searchNode this, searchNode that){
            if (this.priority < that.priority) return -1;
            if (this.priority == that.priority) return 0;
            else return 1;
        }
    }
    private searchNode initialNode;
    private boolean solvable;
    private int moves;

    public Solver(Board initial) {
        /* find a solution to the initial board */
        if (initial == null) throw new IllegalArgumentException();
        initialNode = new searchNode(initial, null, 0);
        searchNode initialTwinNode = new searchNode(initial.twin(), null, 0);
        solvable = false;
        moves = -1;
        MinPQ<searchNode> PQ1 = new MinPQ<>();
        MinPQ<searchNode> PQ2 = new MinPQ<>();
        PQ1.insert(initialNode);
        PQ2.insert(initialTwinNode);
        while (!initialNode.isGoal() && !initialTwinNode.isGoal()) {
            for(searchNode each : initialNode.neighbors())
                PQ1.insert(each);
            initialNode = PQ1.delMin();
            //StdOut.println(initial.predecessor());
            for(searchNode each : initialTwinNode.neighbors())
                PQ2.insert(each);
            initialTwinNode = PQ2.delMin();
        }
        if (initialNode.isGoal()) {
            solvable = true;
            moves = initialNode.moves();
        }
    }

    public boolean isSolvable() {
        return this.solvable;
    }

    public int moves() {
        return this.moves;
    }

    public Iterable<Board> solution() {
        if(!this.isSolvable()) return null;
        Stack<Board> solution = new Stack<>();
        while(initialNode != null) {
            solution.push(initialNode.getCurr());
            initialNode = initialNode.getPredecessor();
        }
        return solution;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

    // solve the puzzle
        Solver solver = new Solver(initial);

    // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
