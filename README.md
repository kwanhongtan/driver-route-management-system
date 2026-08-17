# ZipRide Dispatch System

Author: Tan Kwan Hong
Language: Java

## What This Project Is

This is my Java version of the ZipRide Dispatch System. I built it as one connected demo instead of four completely separate programs. The graph stores the city road network, the hash tables store passengers and drivers, the heap schedules pickup requests, and the sorting module prepares the reporting output.

The four required modules are:

1. Graph route planning with adjacency lists, BFS, DFS cycle detection, and Dijkstra shortest path.
2. Hash table lookup for passengers and drivers using linear probing.
3. Max-heap pickup scheduling using passenger tier and estimated pickup time.
4. Merge sort and quick sort benchmarks for pickup records.

For the core data structures, I did not use Java built-in graph libraries, map/dictionary classes, priority queues, `Collections.sort`, or `Arrays.sort`.

## Folder Structure

```text
code/
  Key files:
    ZipRideApp.java      main Java demo with menu
    DataSetup.java       fixed graph, passenger, and driver test data
    Graph.java           Module 1 graph, BFS, DFS, and Dijkstra
    HashTable.java       Module 2 hash table with linear probing
    PickupMaxHeap.java   Module 3 array-based max heap
    Scheduler.java       connects graph, hash table, and heap scheduling
    Sorting.java         Module 4 merge sort, quick sort, and benchmarks

  Supporting classes:
    PassengerRecord.java stores passenger ID, name, pickup location, and tier
    DriverRecord.java    stores driver ID, name, current location, and status
    PickupRequest.java   stores scheduled request and calculates priority
    PickupRecord.java    stores pickup data used for sorting/reporting
    GraphNode.java       represents one graph location and its adjacency list
    GraphEdge.java       represents one weighted road between graph nodes
    LinkedList.java      custom linked list used by graph, queue, and stack
    ListNode.java        node class for the custom linked list
    Queue.java           custom FIFO queue used by BFS
    Stack.java           custom LIFO stack used by DFS
    SortMetrics.java     tracks sorting comparisons and moves
    SortResult.java      stores sorted records with their metrics
sample_input/
  city_graph_edges.csv
  passengers.csv
  drivers.csv
sample_output/
  java_sample_run.txt
  java_benchmark_results.csv
report/
  report.md
  report.pdf
  output_screenshot/
    Module1Output.png
    Module2Output.png
    Module3Output.png
    Module4Output.png
  zipride_uml.png
```

## How To Run

From the project root:

```bash
cd code
javac *.java
java ZipRideApp
```

The program opens a small menu. Choose `5` to run the full assignment demo, or choose `1` to `4` to show one module at a time.

## What To Show In The Demo

Module 1 shows the graph structure, BFS levels from `CBD`, DFS cycle detection, Dijkstra from `CBD` to `Airport`, and an unreachable path test to `IsolatedDepot`.

Module 2 inserts 20 passengers and 20 drivers, prints load factor after every 10 inserts, shows search hits and misses, deletes a passenger, and prints a collision example using passenger keys `101` and `155`.

Module 3 creates 10 pickup requests, including repeated pickup locations so some later requests must use non-zero Dijkstra travel times after nearby drivers become busy. For each request, it looks up the passenger, scans available drivers, uses Dijkstra to find the nearest driver, calculates priority, and inserts the request into the max heap. The heap array is printed after every insert and extraction. It also shows invalid passenger, no-available-driver, and tier-update edge cases.

Module 4 sorts pickup records by `EstimatedPickupTime`. It runs merge sort and quick sort on 100, 500, and 1000 records under random, nearly sorted, and reversed conditions, then prints correctness checks and timing/operation-count rows.

## Report

The report PDF is here:

```text
report/report.pdf
```

The UML screenshot used inside the report is here:

```text
report/zipride_uml.png
```

The module output screenshots used inside the report are here:

```text
report/output_screenshot/
```
=======
# driver-route-management-system

