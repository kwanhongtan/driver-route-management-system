/*
 * Author: Tan Kwan Hong
 * Student ID: 23442423
 * Date: 28 May 2026
 * Description: Implements merge sort, quick sort, and sorting benchmarks.
 */

import java.util.Random;

public class Sorting
{
    private static final int MEDIAN3_PIVOT = 1;
    private static final double NEARLY_PERCENT = 0.10;
    private static final int RANDOM_TIMES = 100;

    public static SortResult mergeSort(PickupRecord[] input)
    {
        SortMetrics metrics = new SortMetrics();
        // I sort a copy so merge sort and quick sort can start from the same data.
        PickupRecord[] records = copy(input);
        if (records.length > 1)
        {
            mergeSortRecurse(records, 0, records.length - 1, metrics);
        }
        return new SortResult(records, metrics);
    }

    public static SortResult quickSort(PickupRecord[] input)
    {
        SortMetrics metrics = new SortMetrics();
        // Quick sort also gets its own copy for the same reason.
        PickupRecord[] records = copy(input);
        if (records.length > 1)
        {
            quickSortRecurse(records, 0, records.length - 1, metrics);
        }
        return new SortResult(records, metrics);
    }

    public static boolean isSorted(PickupRecord[] records)
    {
        boolean sorted = true;
        // I check sorted output because the benchmark only matters if the result is correct.
        for (int i = 1; i < records.length && sorted == true; i++)
        {
            if (records[i - 1].getEstimatedPickupTime() > records[i].getEstimatedPickupTime())
            {
                sorted = false;
            }
        }
        return sorted;
    }

    public static PickupRecord[] generateDataset(Graph graph, PassengerRecord[] passengers, DriverRecord[] drivers, int size)
    {
        PickupRecord[] dataset = new PickupRecord[size];
        // I generate benchmark records from the real passengers, drivers, and graph
        // routes so Module 4 still depends on the earlier modules.
        for (int i = 0; i < size; i++)
        {
            PassengerRecord passenger = passengers[i % passengers.length];
            DriverRecord driver = drivers[(i * 7 + 3) % drivers.length];
            int time;
            if (driver.getCurrentLocation().equals(passenger.getPickupLocation()))
            {
                // Same-location pickup is basically immediate, but I use 1 minute
                // so the priority formula never divides by zero.
                time = 1;
            }
            else
            {
                // EstimatedPickupTime comes from Dijkstra, linking sorting back to the graph.
                Graph.PathResult route = graph.dijkstra(driver.getCurrentLocation(), passenger.getPickupLocation());
                if (route.reachable)
                {
                    time = route.time;
                }
                else
                {
                    time = 999;
                }
            }
            double priority = (6 - passenger.getMembershipTier()) + (1000.0 / time);
            dataset[i] = new PickupRecord(i + 1, passenger.getPassengerId(), driver.getDriverId(), passenger.getName(),
                    passenger.getPickupLocation(), driver.getCurrentLocation(), passenger.getMembershipTier(), time, priority);
        }
        return dataset;
    }

    public static PickupRecord[] prepareCondition(PickupRecord[] base, String condition)
    {
        SortResult sorted = mergeSort(base);
        PickupRecord[] prepared = sorted.getRecords();
        PickupRecord[] result;
        if ("random".equals(condition))
        {
            // Start sorted, then do many swaps to make a random case. The fixed
            // Random seed keeps the
            // benchmark reproducible, as described in Oracle's Random documentation.
            Random random = new Random(42);
            for (int i = 0; i < RANDOM_TIMES * prepared.length; i++)
            {
                int x = random.nextInt(prepared.length);
                int y = random.nextInt(prepared.length);
                swap(prepared, x, y);
            }
            result = prepared;
        }
        else if ("nearly_sorted".equals(condition))
        {
            // I only move about 10 percent of the data here. The same fixed seed
            // makes this condition reproducible across benchmark runs.
            Random random = new Random(42);
            for (int i = 0; i < prepared.length * NEARLY_PERCENT / 2.0; i++)
            {
                int x = random.nextInt(prepared.length);
                int y = random.nextInt(prepared.length);
                swap(prepared, x, y);
            }
            result = prepared;
        }
        else if ("reversed".equals(condition))
        {
            // I reverse the sorted data by swapping from both ends.
            for (int i = 0; i < prepared.length / 2; i++)
            {
                swap(prepared, i, prepared.length - i - 1);
            }
            result = prepared;
        }
        else
        {
            throw new IllegalArgumentException("Unknown condition.");
        }
        return result;
    }

    public static String benchmark(Graph graph, PassengerRecord[] passengers, DriverRecord[] drivers)
    {
        int[] sizes = {100, 500, 1000};
        String[] conditions = {"random", "nearly_sorted", "reversed"};
        String out = "Algorithm,Size,Condition,Milliseconds,Comparisons,Moves,Sorted\n";
        // This loop covers every required size and input condition.
        for (int i = 0; i < sizes.length; i++)
        {
            PickupRecord[] base = generateDataset(graph, passengers, drivers, sizes[i]);
            for (int j = 0; j < conditions.length; j++)
            {
                PickupRecord[] prepared = prepareCondition(base, conditions[j]);
                out = out + benchmarkLine("MergeSort", sizes[i], conditions[j], prepared, true);
                out = out + benchmarkLine("QuickSort", sizes[i], conditions[j], prepared, false);
            }
        }
        return out.trim();
    }

    private static String benchmarkLine(String name, int size, String condition,
                                        PickupRecord[] data, boolean merge)
                                        {
        long start = System.nanoTime();
        SortResult result;
        if (merge == true)
        {
            result = mergeSort(data);
        }
        else
        {
            result = quickSort(data);
        }
        long end = System.nanoTime();
        // I use System.nanoTime() for timing, then convert it to milliseconds.
        double ms = (end - start) / 1_000_000.0;
        return name + "," + size + "," + condition + ","
                + String.format("%.3f", ms) + "," + result.getMetrics().getComparisons() + ","
                + result.getMetrics().getMoves() + "," + isSorted(result.getRecords()) + "\n";
    }

    private static void mergeSortRecurse(PickupRecord[] records, int leftIdx, int rightIdx, SortMetrics metrics)
    {
        if (leftIdx < rightIdx)
        {
            int midIdx = (leftIdx + rightIdx) / 2;
            // Split until the range reaches single records, then merge back together.
            mergeSortRecurse(records, leftIdx, midIdx, metrics);
            mergeSortRecurse(records, midIdx + 1, rightIdx, metrics);
            merge(records, leftIdx, midIdx, rightIdx, metrics);
        }
    }

    private static void merge(PickupRecord[] records, int leftIdx, int midIdx, int rightIdx, SortMetrics metrics)
    {
        PickupRecord[] tempArr = new PickupRecord[rightIdx - leftIdx + 1];
        int ii = leftIdx;
        int jj = midIdx + 1;
        int kk = 0;

        // Merge into one temporary array, then copy the sorted range back.
        while (ii <= midIdx && jj <= rightIdx)
        {
            metrics.addComparison();
            if (records[ii].getEstimatedPickupTime() <= records[jj].getEstimatedPickupTime())
            {
                tempArr[kk] = records[ii];
                ii++;
            }
            else
            {
                tempArr[kk] = records[jj];
                jj++;
            }
            kk++;
            metrics.addMove();
        }

        while (ii <= midIdx)
        {
            tempArr[kk] = records[ii];
            ii++;
            kk++;
            metrics.addMove();
        }

        while (jj <= rightIdx)
        {
            tempArr[kk] = records[jj];
            jj++;
            kk++;
            metrics.addMove();
        }

        for (kk = 0; kk < tempArr.length; kk++)
        {
            records[leftIdx + kk] = tempArr[kk];
            metrics.addMove();
        }
    }

    private static void quickSortRecurse(PickupRecord[] records, int leftIdx, int rightIdx, SortMetrics metrics)
    {
        if (leftIdx < rightIdx)
        {
            // After partitioning, the pivot is fixed, so I sort the two sides.
            int pivotIdx = choosePivot(records, leftIdx, rightIdx, MEDIAN3_PIVOT, metrics);
            int newPivotIdx = Partition(records, leftIdx, rightIdx, pivotIdx, metrics);
            quickSortRecurse(records, leftIdx, newPivotIdx - 1, metrics);
            quickSortRecurse(records, newPivotIdx + 1, rightIdx, metrics);
        }
    }

    private static int choosePivot(PickupRecord[] records, int leftIdx, int rightIdx, int pivotType, SortMetrics metrics)
    {
        int pivotIdx;
        if (pivotType == MEDIAN3_PIVOT)
        {
            int midIdx = (leftIdx + rightIdx) / 2;
            pivotIdx = medianOfThree(records, leftIdx, midIdx, rightIdx, metrics);
        }
        else
        {
            pivotIdx = leftIdx;
        }
        return pivotIdx;
    }

    private static int Partition(PickupRecord[] records, int leftIdx, int rightIdx, int pivotIdx, SortMetrics metrics)
    {
        int pivotValue = records[pivotIdx].getEstimatedPickupTime();
        // I move the chosen pivot to the end temporarily so the scan is simple.
        swap(records, pivotIdx, rightIdx);
        metrics.addMoves(3);
        int currIdx = leftIdx;

        // currIdx marks the boundary of the section smaller than the pivot.
        for (int ii = leftIdx; ii < rightIdx; ii++)
        {
            metrics.addComparison();
            if (records[ii].getEstimatedPickupTime() < pivotValue)
            {
                swap(records, ii, currIdx);
                metrics.addMoves(3);
                currIdx++;
            }
        }
        swap(records, currIdx, rightIdx);
        metrics.addMoves(3);
        return currIdx;
    }

    private static int medianOfThree(PickupRecord[] records, int leftIdx, int midIdx, int rightIdx, SortMetrics metrics)
    {
        int a = records[leftIdx].getEstimatedPickupTime();
        int b = records[midIdx].getEstimatedPickupTime();
        int c = records[rightIdx].getEstimatedPickupTime();
        metrics.addComparisons(3);
        int medianIndex;
        // Median-of-three is safer than always picking the first or last item.
        if ((a <= b && b <= c) || (c <= b && b <= a))
        {
            medianIndex = midIdx;
        }
        else if ((b <= a && a <= c) || (c <= a && a <= b))
        {
            medianIndex = leftIdx;
        }
        else
        {
            medianIndex = rightIdx;
        }
        return medianIndex;
    }

    private static PickupRecord[] copy(PickupRecord[] input)
    {
        PickupRecord[] copied = new PickupRecord[input.length];
        // I copy each record so benchmark runs do not share mutable objects.
        for (int i = 0; i < input.length; i++)
        {
            copied[i] = input[i].copy();
        }
        return copied;
    }

    private static void swap(PickupRecord[] records, int a, int b)
    {
        // Small helper for swaps used by quick sort and input preparation.
        PickupRecord temp = records[a];
        records[a] = records[b];
        records[b] = temp;
    }
}
