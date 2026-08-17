/*
 * Author: Tan Kwan Hong
 * Student ID: 23442423
 * Date: 26 May 2026
 * Description: Linked-list node used by the graph adjacency-list structure.
 */

public class ListNode
{
    private Object value;
    private ListNode next;
    private ListNode prev;

    public ListNode(Object value)
    {
        this.value = value;
        this.next = null;
        this.prev = null;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public ListNode getNext()
    {
        return next;
    }

    public void setNext(ListNode next)
    {
        this.next = next;
    }

    public ListNode getPrev()
    {
        return prev;
    }

    public void setPrev(ListNode prev)
    {
        this.prev = prev;
    }
}
