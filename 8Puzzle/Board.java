import java.lang.Math;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    
    private int[] board;    // one dimensional array represents the board
    private int n;               // board dimension
    private int blank;       // the position of blank block in previous search node
    
    public Board(int[][] blocks) { 
        /* construct a board from an n-by-n array of blocks */
        n = blocks[0].length;
        int sz = n * n;
        board = new int[sz];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++) {
                if (blocks[i][j] == 0)
                    blank = i * n + j;
                board[i * n + j] = blocks[i][j];
            }
        }
    }
    
    private Board(Board that) {
        this.n = that.n;
        this.blank = that.blank;
        this.board = new int[this.n * this.n];
        for(int i = 0; i < n * n; i++)
            this.board[i] = that.board[i];
    }
    
    public int dimension() {
        /* board dimension n */
        return n;
    }

    public int hamming() {
        /* number of blocks out of place */
        int res = 0;
        for(int i = 0; i < n * n; i++){
            if(board[i] != i + 1 && i != blank)
                res++;
        }
        return res;
    }

    public int manhattan() {
        /* sum of manhattan distances between blocks and goal */
        int res = 0;
        for(int i = 0; i < n * n; i++){
            if(board[i] != i + 1 && i != blank) {
                int wrong_row = i / n;
                int wrong_col = i % n;
                int right_row = (board[i] - 1) / n;
                int right_col = (board[i] - 1) % n;
                res += Math.abs(right_row - wrong_row) + Math.abs(right_col - wrong_col);
            }
        }
        return res;
    }
    
    public boolean isGoal() {
        /* is this board the goal board? */
        return hamming() == 0;
    }
    
    public Board twin() {
        /* a board that is obtained by exchanging any pair of blocks */
        Board modifyBoard = new Board(this);
        int i = blank - 1 >= 0 ? blank - 1 : n * n - 1;
        int j = blank + 1 < n * n ? blank + 1 : 0;
//        do {
//            i = StdRandom.uniform(n*n);
//            j = StdRandom.uniform(n*n);
//        } while (i == blank || j == blank || i == j);
        modifyBoard.swap(i, j);
        return modifyBoard;
    }
    
    public boolean equals(Object y) {
        /* does this board equal y? */
        if(y == this) return true;
        if(y == null) return false;
        if(y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if(this.n != that.n) return false;
        for(int i = 0; i < n * n; i++){
            if(this.board[i] != that.board[i])
                return false;
        }
        return true;
    }
    
    public Iterable<Board> neighbors() {
        /* all neighboring boards */
        Stack<Board> neighbors = new Stack<>();
        int blank_row = blank / n;
        int blank_col = blank % n;
        boolean[] hasNeighbor = {blank_row>0, blank_row<n-1, blank_col>0, blank_col<n-1};
        int[] neighborIndex = {blank-n, blank+n, blank-1, blank+1};
        for (int i = 0; i < 4; i++) {
            if (hasNeighbor[i]) {
                Board temp = new Board(this);
                temp.swap(blank, neighborIndex[i]);
                neighbors.push(temp);
            }
        }
        return neighbors;
    }
    
    private void swap(int i,int j) {
        /* swap board[i] and board[j] */
        if (blank == i) blank = j;
        else if (blank == j) blank = i;
        int temp = board[j];
        board[j] = board[i];
        board[i] = temp;
    }
    
    public String toString() {
        /* string representation of this board (in the output format specified below) */
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++)
                s.append(String.format("%2d ", board[i * n + j]));
            s.append("\n");
        }
        return s.toString();
    }
    
    public static void main(String[] args) { 
        int[][] blocks = {{0,1,3},{4,2,5},{7,8,6}};
        Board example = new Board(blocks);
        Board first_neighbor = new Board(blocks);
        for(Board each: example.neighbors()){
            first_neighbor = each;
            break;
        }
        StdOut.println(example.twin());
        StdOut.println(first_neighbor);
        for(Board each: first_neighbor.neighbors()){
            StdOut.println(each);
//            StdOut.println(each.predecessor);
//            StdOut.println(each.predecessor.predecessor);
        }
    }
}
