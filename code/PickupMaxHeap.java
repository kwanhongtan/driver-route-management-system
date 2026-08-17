/*
 * Author: Tan Kwan Hong
 * Student ID: 23442423
 * Date: 27 May 2026
 * Description: Implements an array-based max heap for pickup request priority.
 */

public class PickupMaxHeap
{
    private PickupRequest[] heap;
    private int size;

    public PickupMaxHeap()
    {
        // This is a plain array-based heap, not Java's PriorityQueue, so the heap
        // logic is visible and matches the assignment requirement.
        heap = new PickupRequest[16];
        size = 0;
    }

    public void insert(PickupRequest request)
    {
        if (request == null)
        {
            throw new IllegalArgumentException("Cannot insert null request.");
        }
        ensureCapacity();
        // New heap items always start at the next free leaf position.
        heap[size] = request;
        // If this request has a higher priority than its parent, move it upward
        // until the max-heap order is correct again.
        trickleUp(size);
        size++;
    }

    public PickupRequest peek()
    {
        PickupRequest top = null;
        if (size == 0)
        {
            // Nothing has been scheduled yet, so there is no request to preview.
            top = null;
        }
        else
        {
            // In a max heap, the root is always the next request to dispatch.
            top = heap[0];
        }
        return top;
    }

    public PickupRequest extractPriority()
    {
        PickupRequest best = null;
        if (size == 0)
        {
            best = null;
        }
        else
        {
            best = heap[0];
            // Remove the root by replacing it with the last leaf, then repair the heap.
            heap[0] = heap[size - 1];
            heap[size - 1] = null;
            size--;
            // The replacement root may be too small, so push it down if needed.
            trickleDown(0);
        }
        return best;
    }

    public String stateString()
    {
        String result = "[";
        // Print just the used part of the heap array so the trace stays readable.
        for (int i = 0; i < size; i++)
        {
            result = result + heap[i].label();
            if (i < size - 1)
            {
                result = result + ", ";
            }
        }
        result = result + "]";
        return result;
    }

    private void trickleUp(int curIdx)
    {
        int parentIdx = (curIdx - 1) / 2;

        // Keep swapping with the parent while the child has the stronger priority.
        while (curIdx > 0 && heap[curIdx].getPriority() > heap[parentIdx].getPriority())
        {
            swap(curIdx, parentIdx);
            curIdx = parentIdx;
            parentIdx = (curIdx - 1) / 2;
        }
    }

    private void trickleDown(int curIdx)
    {
        int leftChild;
        int rightChild;
        int largeIdx;
        boolean keepGoing = true;

        while (keepGoing == true)
        {
            leftChild = curIdx * 2 + 1;
            rightChild = curIdx * 2 + 2;
            largeIdx = curIdx;

            // Check the left child first if it exists.
            if (leftChild < size && heap[leftChild].getPriority() > heap[largeIdx].getPriority())
            {
                largeIdx = leftChild;
            }

            // Then check the right child; whichever child is larger should move up.
            if (rightChild < size && heap[rightChild].getPriority() > heap[largeIdx].getPriority())
            {
                largeIdx = rightChild;
            }

            if (largeIdx != curIdx)
            {
                // Swap with the larger child and then check from that child position.
                swap(curIdx, largeIdx);
                curIdx = largeIdx;
            }
            else
            {
                // Both children are smaller, so this branch of the heap is fixed.
                keepGoing = false;
            }
        }
    }

    private void swap(int a, int b)
    {
        // A tiny manual swap keeps the heap movement easy to follow.
        PickupRequest temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
    }

    private void ensureCapacity()
    {
        if (size >= heap.length)
        {
            // If more requests arrive than expected, grow the array instead of failing.
            PickupRequest[] larger = new PickupRequest[heap.length * 2];
            for (int i = 0; i < heap.length; i++)
            {
                larger[i] = heap[i];
            }
            heap = larger;
        }
    }
}
