/*
 * Author: Tan Kwan Hong
 * Date: 26 May 2026
 * Description: Simple stack used by iterative DFS with the custom linked-list structure.
 */

import java.util.NoSuchElementException;

public class Stack
{
    private LinkedList stack;

    public Stack()
    {
        stack = new LinkedList();
    }

    public boolean isEmpty()
    {
        return stack.isEmpty();
    }

    public void push(Object value)
    {
        // Stack pushes to the front so the newest item is removed first.
        stack.insertFirst(value);
    }

    public Object pop()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Stack is empty.");
        }

        // Stack removes from the front, giving last-in-first-out order for DFS.
        return stack.removeFirst();
    }
}
