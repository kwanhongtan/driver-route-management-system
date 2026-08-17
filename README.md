# ZipRide Dispatch System

A Java-based ride-hailing dispatch system developed for the COMP1002 Data Structures & Algorithms course at Curtin University Malaysia.

## Overview

ZipRide Dispatch System is designed to manage passengers, drivers, routes, and pickup requests using fundamental data structures and algorithms.

The system integrates four main components:

- **Graph-Based Route Planning**
- **Hash-Based Passenger and Driver Lookup**
- **Heap-Based Pickup Scheduling**
- **Sorting of Pickup Records**

## Features

### Graph-Based Route Planning

The city road network is represented using a weighted adjacency-list graph.

Implemented:

- Breadth-First Search (BFS)
- Depth-First Search (DFS) with cycle detection
- Dijkstra's shortest-path algorithm

Dijkstra's algorithm is used to estimate driving time between locations for driver selection and pickup scheduling.

### Passenger and Driver Lookup

A custom hash table using linear probing is used to store and retrieve passenger and driver records.

Features include:

- Insert
- Search
- Delete
- Collision handling
- Load factor tracking
- Input validation

### Pickup Scheduling

Pickup requests are managed using an array-based max heap.

The scheduling process:

1. Retrieves available drivers.
2. Calculates estimated pickup time using the graph.
3. Selects the nearest available driver.
4. Calculates pickup priority using passenger membership tier and estimated pickup time.
5. Dispatches higher-priority requests first.

### Pickup Record Sorting

Pickup records are sorted using:

- Merge Sort
- Quick Sort

The project also compares their performance under different input conditions and dataset sizes.

## Data Structures & Algorithms

This project implements:

- Adjacency List
- Hash Table with Linear Probing
- Max Heap
- Linked List
- Queue
- Stack
- Breadth-First Search (BFS)
- Depth-First Search (DFS)
- Dijkstra's Shortest-Path Algorithm
- Merge Sort
- Quick Sort

The core data structures and algorithms were implemented without relying on Java's built-in implementations for the main logic.

## Technologies

- Java

## Project Structure

```text
code/
├── ZipRideApp.java
├── DataSetup.java
├── Graph.java
├── HashTable.java
├── PickupMaxHeap.java
├── Scheduler.java
├── Sorting.java
├── PassengerRecord.java
├── DriverRecord.java
├── PickupRequest.java
├── PickupRecord.java
├── GraphNode.java
├── GraphEdge.java
├── LinkedList.java
├── ListNode.java
├── Queue.java
├── Stack.java
├── SortMetrics.java
└── SortResult.java

sample_input/
├── city_graph_edges.csv
├── passengers.csv
└── drivers.csv

sample_output/
├── java_sample_run.txt
└── java_benchmark_results.csv
