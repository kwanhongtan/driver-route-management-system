/*
 * Author: Tan Kwan Hong
 * Date: 26 May 2026
 * Description: Simple queue used by BFS with the custom linked-list structure.
 */

import java.util.NoSuchElementException;

public class Queue
{
    private LinkedList queue;

    public Queue()
    {
        queue = new LinkedList();
    }

    public boolean isEmpty()
    {
        return queue.isEmpty();
    }

    public void enqueue(Object value)
    {
        // Queue adds to the back.
        queue.insertLast(value);
    }

    public Object dequeue()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Queue is empty.");
        }

        // Queue removes from the front, giving first-in-first-out order for BFS.
        return queue.removeFirst();
    }
}
