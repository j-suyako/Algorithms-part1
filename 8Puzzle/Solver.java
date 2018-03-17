import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {
    private class searchNode implements Comparable<searchNode> {
        private Board board;
        private searchNode predecessor;
        private int moves;
        private int priority;

        public searchNode(Board bd, searchNode pre, int m) {
            this.board = bd;
            this.predecessor = pre;
            this.moves = m;
            this.priority = m + bd.manhattan();
        }

        public boolean isGoal() {
            return this.board.isGoal();
        }

        public Iterable<searchNode> neighbors() {
            Stack<searchNode> neighbors = new Stack<>();
            for (Board neighbor: board.neighbors()) {
                if (moves == 0 || !predecessor.board.equals(neighbor)) {
                    searchNode e = new searchNode(neighbor, this, moves+1);
                    neighbors.push(e);
                }
            }
            return neighbors;
        }

        public int compareTo(searchNode that) {
            if (this.priority < that.priority) return -1;
            if (this.priority > that.priority) return 1;
            else return Integer.compare(that.moves, this.moves);
        }
    }
    private Stack<Board> solution;
    private boolean solvable;
    private int moves;

    public Solver(Board initial) {
        /* find a solution to the initial board */
        if (initial == null) throw new IllegalArgumentException();
        searchNode currNode = new searchNode(initial, null, 0);
        searchNode currTwinNode = new searchNode(initial.twin(), null, 0);
        solution = new Stack<>();
        solvable = false;
        moves = -1;
        MinPQ<searchNode> PQ1 = new MinPQ<>();
        MinPQ<searchNode> PQ2 = new MinPQ<>();
        PQ1.insert(currNode);
        PQ2.insert(currTwinNode);
        int countInsert = 1;
        int countDelMin = 0;
        while (!currNode.isGoal() && !currTwinNode.isGoal()) {
            for(searchNode each : currNode.neighbors()) {
                PQ1.insert(each);
                ++countInsert;
            }
            currNode = PQ1.delMin();
            ++countDelMin;
            //StdOut.println(initial.predecessor());
            for(searchNode each : currTwinNode.neighbors()) {
                PQ2.insert(each);
                ++countInsert;
            }
            currTwinNode = PQ2.delMin();
            ++countDelMin;
        }
        if (currNode.isGoal()) {
            solvable = true;
            moves = currNode.moves;
            while (currNode != null) {
                solution.push(currNode.board);
                currNode = currNode.predecessor;
            }
        }
        else {
            solution = null;
        }
    }

    public boolean isSolvable() {
        return this.solvable;
    }

    public int moves() {
        return this.moves;
    }

    public Iterable<Board> solution() {
        Queue<Board> copyOfSolution = new Queue<>();
        if (solution != null) {
            for (Board each : solution)
                copyOfSolution.enqueue(each);
            return copyOfSolution;
        }
        else return null;
    }

    public static void main(String[] var0) {
        main("C:\\Users\\JXT\\IdeaProjects\\8Puzzle\\test\\puzzle21.txt");
    }

    private static void main(String path) {
        In in = new In(path);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);
//        solver.solution();

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
