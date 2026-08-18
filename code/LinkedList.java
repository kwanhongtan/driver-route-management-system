/*
 * Author: Tan Kwan Hong
 * Date: 26 May 2026
 * Description: Simple double-ended linked list used by the graph adjacency-list structure.
 */

import java.util.NoSuchElementException;

public class LinkedList
{
    private ListNode head;
    private ListNode tail;

    public LinkedList()
    {
        head = null;
        tail = null;
    }

    public boolean isEmpty()
    {
        return head == null;
    }

    public void insertFirst(Object value)
    {
        ListNode newNode = new ListNode(value);

        if (isEmpty())
        {
            head = newNode;
            tail = newNode;
        }
        else
        {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }
    }

    public void insertLast(Object value)
    {
        ListNode newNode = new ListNode(value);

        if (isEmpty())
        {
            head = newNode;
            tail = newNode;
        }
        else
        {
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    public Object removeFirst()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("List is empty.");
        }

        Object value = head.getValue();

        if (head == tail)
        {
            head = null;
            tail = null;
        }
        else
        {
            head = head.getNext();
            head.setPrev(null);
        }

        return value;
    }

    public boolean remove(Object value)
    {
        ListNode current = head;
        boolean removed = false;

        while (current != null && removed == false)
        {
            Object currentValue = current.getValue();
            if ((currentValue == null && value == null)
                    || (currentValue != null && currentValue.equals(value)))
            {
                if (current == head && current == tail)
                {
                    head = null;
                    tail = null;
                }
                else if (current == head)
                {
                    head = head.getNext();
                    head.setPrev(null);
                }
                else if (current == tail)
                {
                    tail = tail.getPrev();
                    tail.setNext(null);
                }
                else
                {
                    current.getPrev().setNext(current.getNext());
                    current.getNext().setPrev(current.getPrev());
                }
                removed = true;
            }
            else
            {
                current = current.getNext();
            }
        }

        return removed;
    }

    public int size()
    {
        int count = 0;
        ListNode current = head;

        while (current != null)
        {
            count++;
            current = current.getNext();
        }

        return count;
    }

    public Object[] toArray()
    {
        Object[] values = new Object[size()];
        ListNode current = head;
        int index = 0;

        while (current != null)
        {
            values[index] = current.getValue();
            index++;
            current = current.getNext();
        }

        return values;
    }

    public ListNode getHead()
    {
        return head;
    }
}
