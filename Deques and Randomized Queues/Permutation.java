import java.lang.Integer;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * takes an integer k as a command-line argument
 * reads in a sequence of strings from standard input
 * prints exactly k of them, uniformly at random.
 *
 * @author suyako
 */
public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomize_deque = new RandomizedQueue<String>();
        while(!StdIn.isEmpty()){
            randomize_deque.enqueue(StdIn.readString());
        }
        for(int i = 0; i < k; i++)
            StdOut.println(randomize_deque.dequeue());
    }
}
