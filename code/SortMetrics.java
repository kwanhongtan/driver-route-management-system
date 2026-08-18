/*
 * Author: Tan Kwan Hong
 * Date: 28 May 2026
 * Description: Stores comparison and move counts for sorting tests.
 */

public class SortMetrics
{
    private int comparisons;
    private int moves;

    public int getComparisons()
    {
        return comparisons;
    }

    public void setComparisons(int comparisons)
    {
        this.comparisons = comparisons;
    }

    public int getMoves()
    {
        return moves;
    }

    public void setMoves(int moves)
    {
        this.moves = moves;
    }

    public void addComparison()
    {
        // Each time the sorting code compares pickup times, record it here.
        comparisons++;
    }

    public void addComparisons(int amount)
    {
        comparisons = comparisons + amount;
    }

    public void addMove()
    {
        // Moves count record assignments and swap steps, giving another way to
        // compare merge sort and quick sort besides time.
        moves++;
    }

    public void addMoves(int amount)
    {
        moves = moves + amount;
    }
}
