import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * A randomized queue is similar to data stack or queue, except that the item removed
 * is chosen uniformly at random from items in the data structure.
 *
 * @author suyako
 */
public class RandomizedQueue<Item> implements Iterable<Item>{
    private Item[] data;
    private int N;
    
    public RandomizedQueue(){ 
        /* construct an empty randomized queue */
        data = (Item[]) new Object[2];
        //data = Item[2];
        N = 0;
    }

    public boolean isEmpty(){
        /* is the randomized queue empty */
        return N == 0;
    }

    public int size(){
        /*return the number of items on the randomized queue */
        return N;
    }

    private void resize(int capacity){
        /* adjust the size of list when it reaches its capacity or 3/4 of its data has been removed */
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++)
            temp[i] = data[i];
        data = temp;
    }

    public void enqueue(Item item){
        /* add the item */
        if(item == null) throw new IllegalArgumentException();
        if(N == data.length) resize(2 * data.length);
        data[N++] = item;
    }

    public Item dequeue(){
        /* remove and return a random item */
        if(isEmpty()) throw new NoSuchElementException();
        int m = StdRandom.uniform(N);
        Item res = data[m];
        data[m] = data[--N];  // assign the last item to data[m]
        //data[N+1] = null;
        if (N > 0 && N == data.length / 4) resize(data.length / 2);
        return res;
    }

    public Item sample(){
        /* return a random item but do not remove it */
        if(isEmpty()) throw new NoSuchElementException();
        int m = StdRandom.uniform(N);
        return data[m];
    }

    public Iterator<Item> iterator(){
        /* return an independent iterator over items in random order*/
        return new RandomListIterator();
    }

    private class RandomListIterator implements Iterator<Item>{
        public boolean hasNext(){return N > 0;}
        public void remove(){throw new UnsupportedOperationException();}
        public Item next(){
            if(!hasNext()) throw new NoSuchElementException();
            return dequeue();
        }
    }

    public static void main(String[] args) { 
        RandomizedQueue<String> randomizedqueue = new RandomizedQueue<String>();
        String[] collection = {"A","B","C","D","E","F","G","H"};
        for(String s : collection)
            randomizedqueue.enqueue(s);
        for(String s : randomizedqueue)
            StdOut.println(s);
    }
}
