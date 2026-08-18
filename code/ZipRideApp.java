/*
 * Author: Tan Kwan Hong
 * Date: 28 May 2026
 * Description: Runs the integrated ZipRide assignment demonstration.
 */

import java.util.Scanner;

public class ZipRideApp
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        // I use a small menu so I can show one module at a time during the demo,
        // or run the whole assignment in order.
        while (exit == false)
        {
            printMenu();
            if (!scanner.hasNextLine())
            {
                exit = true;
                continue;
            }
            String choice = scanner.nextLine().trim();
            System.out.println("");

            if ("1".equals(choice))
            {
                module1(DataSetup.buildCityGraph());
            }
            else if ("2".equals(choice))
            {
                module2(new HashTable(53, "PassengerTable"), new HashTable(53, "DriverTable"),
                        DataSetup.passengers(), DataSetup.drivers());
            }
            else if ("3".equals(choice))
            {
                Graph graph = DataSetup.buildCityGraph();
                HashTable passengerTable = new HashTable(53, "PassengerTable");
                HashTable driverTable = new HashTable(53, "DriverTable");
                PassengerRecord[] passengers = DataSetup.passengers();
                DriverRecord[] drivers = DataSetup.drivers();
                preloadTables(passengerTable, driverTable, passengers, drivers);
                module3(graph, passengerTable, driverTable);
            }
            else if ("4".equals(choice))
            {
                module4(DataSetup.buildCityGraph(), DataSetup.passengers(), DataSetup.drivers());
            }
            else if ("5".equals(choice))
            {
                runFullDemo();
            }
            else if ("0".equals(choice))
            {
                System.out.println("Exiting ZipRide Dispatch System demo.");
                exit = true;
            }
            else
            {
                System.out.println("Invalid choice. Please enter 0, 1, 2, 3, 4, or 5.");
                System.out.println("");
            }
        }

        scanner.close();
    }

    private static void printMenu()
    {
        System.out.println("=== ZIPRIDE DISPATCH SYSTEM MENU ===");
        System.out.println("1. Module 1 - Graph route planning");
        System.out.println("2. Module 2 - Hash table lookup");
        System.out.println("3. Module 3 - Heap pickup scheduling");
        System.out.println("4. Module 4 - Sorting pickup records");
        System.out.println("5. Run all modules");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    private static void runFullDemo()
    {
        // I build fresh data for the full run so old driver statuses do not affect
        // the next demo run.
        Graph graph = DataSetup.buildCityGraph();
        HashTable passengerTable = new HashTable(53, "PassengerTable");
        HashTable driverTable = new HashTable(53, "DriverTable");
        PassengerRecord[] passengers = DataSetup.passengers();
        DriverRecord[] drivers = DataSetup.drivers();

        module1(graph);
        module2(passengerTable, driverTable, passengers, drivers);
        module3(graph, passengerTable, driverTable);
        module4(graph, passengers, drivers);
    }

    private static void preloadTables(HashTable passengerTable, HashTable driverTable,
                                      PassengerRecord[] passengers, DriverRecord[] drivers)
    {
        // Module 3 needs the hash tables, so this loads them quietly when the user
        // chooses Module 3 directly from the menu.
        for (int i = 0; i < passengers.length; i++)
        {
            passengerTable.insert(passengers[i].getKey(), passengers[i]);
        }

        for (int i = 0; i < drivers.length; i++)
        {
            driverTable.insert(drivers[i].getKey(), drivers[i]);
        }
    }

    private static void module1(Graph graph)
    {
        // Module 1 shows the graph, BFS levels, DFS cycle detection, and Dijkstra.
        System.out.println("=== MODULE 1: GRAPH-BASED ROUTE PLANNING ===");
        System.out.println("Graph structure:");
        System.out.println(graph.display());
        System.out.println("");
        System.out.println("BFS from CBD:");
        System.out.println(graph.bfsLevels("CBD"));
        System.out.println("Note: IsolatedDepot is intentionally unreachable from CBD.");

        Graph.CycleResult cycle = graph.detectCycle("CBD");
        System.out.println("DFS cycle detected: " + cycle.hasCycle);
        if (cycle.hasCycle)
        {
            System.out.println("Cycle members: " + cycle.cycle);
        }

        Graph.PathResult airport = graph.dijkstra("CBD", "Airport");
        System.out.println("Dijkstra CBD to Airport: time=" + airport.time
                + " minutes, path=" + airport.path);

        Graph.PathResult isolated = graph.dijkstra("CBD", "IsolatedDepot");
        if (!isolated.reachable)
        {
            System.out.println("Dijkstra CBD to IsolatedDepot: unreachable");
        }
        System.out.println("");
    }

    private static void module2(HashTable passengerTable,
                                  HashTable driverTable,
                                  PassengerRecord[] passengers,
                                  DriverRecord[] drivers)
    {
        // Module 2 loads both tables and then shows lookup, deletion, load factor,
        // and one clear collision example.
        System.out.println("=== MODULE 2: HASH-BASED LOOKUP ===");
        System.out.println("Passenger insert log:");

        for (int i = 0; i < passengers.length; i++)
        {
            String line = passengerTable.insert(passengers[i].getKey(), passengers[i]);
            // I print only part of the insert log so the demo output stays readable.
            if (i < 4 || i >= passengers.length - 3)
            {
                System.out.println(line);
            }
            if (i == 3)
            {
                System.out.println("...");
            }
            if ((i + 1) % 10 == 0)
            {
                // The brief asks for load factor after every 10 inserts.
                System.out.println("Load factor after " + (i + 1) + " passenger inserts: "
                        + String.format("%.3f", passengerTable.loadFactor()));
            }
        }

        System.out.println("Driver insert log:");
        for (int i = 0; i < drivers.length; i++)
        {
            String line = driverTable.insert(drivers[i].getKey(), drivers[i]);
            // Same compact logging style for the driver table.
            if (i < 4 || i >= drivers.length - 3)
            {
                System.out.println(line);
            }
            if (i == 3)
            {
                System.out.println("...");
            }
            if ((i + 1) % 10 == 0)
            {
                System.out.println("Load factor after " + (i + 1) + " driver inserts: "
                        + String.format("%.3f", driverTable.loadFactor()));
            }
        }

        PassengerRecord collision = new PassengerRecord(155, "Collision Example", "CBD", 3);
        System.out.println("Collision example using table size 53:");
        System.out.println(passengerTable.insert(collision.getKey(), collision));
        // 101 and 155 hash to the same bucket, so this makes linear probing visible.
        int bucket = passengerTable.hashFunction("101");
        System.out.println("Probe sequence from bucket " + bucket + " after inserting keys 101 and 155: "
                + passengerTable.bucketState(bucket));

        int[] searchKeys = {101, 120, 999};
        for (int i = 0; i < searchKeys.length; i++)
        {
            HashTable.SearchResult result = passengerTable.search("" + searchKeys[i]);
            PassengerRecord passenger = (PassengerRecord)result.getValue();
            if (passenger == null)
            {
                System.out.println("Search passenger " + searchKeys[i] + ": MISS, comparisons="
                        + result.getComparisons());
            }
            else
            {
                System.out.println("Search passenger " + searchKeys[i] + ": HIT "
                        + passenger + ", comparisons=" + result.getComparisons());
            }
        }

        int[] driverSearchKeys = {201, 999};
        for (int i = 0; i < driverSearchKeys.length; i++)
        {
            HashTable.SearchResult result = driverTable.search("" + driverSearchKeys[i]);
            DriverRecord driver = (DriverRecord)result.getValue();
            if (driver == null)
            {
                System.out.println("Search driver " + driverSearchKeys[i] + ": MISS, comparisons="
                        + result.getComparisons());
            }
            else
            {
                System.out.println("Search driver " + driverSearchKeys[i] + ": HIT "
                        + driver + ", comparisons=" + result.getComparisons());
            }
        }

        System.out.println(passengerTable.delete("155"));
        HashTable.SearchResult afterDelete = passengerTable.search("155");
        String deleteSearchStatus;
        if (afterDelete.getValue() == null)
        {
            deleteSearchStatus = "MISS";
        }
        else
        {
            deleteSearchStatus = "HIT";
        }
        System.out.println("Search passenger 155 after delete: " + deleteSearchStatus
                + ", comparisons=" + afterDelete.getComparisons());
        System.out.println(driverTable.delete("999"));
        System.out.println(passengerTable.summary());
        System.out.println(driverTable.summary());
        System.out.println("");
    }

    private static void module3(Graph graph, HashTable passengerTable,
                                  HashTable driverTable)
    {
        // Module 3 inserts ten requests and prints the heap after each operation.
        // The request order repeats several pickup locations, so once the closest
        // driver is made Busy, later requests must use Dijkstra to find another
        // available driver with a non-trivial travel time.
        System.out.println("=== MODULE 3: HEAP-BASED PICKUP SCHEDULING ===");
        Scheduler scheduler = new Scheduler(graph, passengerTable, driverTable);
        int[] passengerIds = {101, 111, 103, 113, 105, 114, 108, 120, 102, 112};

        for (int i = 0; i < passengerIds.length; i++)
        {
            System.out.println(scheduler.insertPassengerRequest(passengerIds[i]));
            System.out.println("Heap after insert: " + scheduler.heap.stateString());
        }

        System.out.println("Edge case - invalid passenger:");
        System.out.println(scheduler.insertPassengerRequest(999));

        System.out.println("Edge case - no available driver:");
        HashTable offlineDriverTable = new HashTable(7, "OfflineDriverTable");
        offlineDriverTable.insert("301", new DriverRecord(301, "Offline Test Driver", "CBD", "Offline"));
        Scheduler noDriverScheduler = new Scheduler(graph, passengerTable, offlineDriverTable);
        System.out.println(noDriverScheduler.insertPassengerRequest(101));

        System.out.println("Edge case - tier update before scheduling:");
        PassengerRecord tierChangePassenger = new PassengerRecord(180, "Tier Change Demo", "Airport", 5);
        passengerTable.insert(tierChangePassenger.getKey(), tierChangePassenger);
        tierChangePassenger.setMembershipTier(1);
        System.out.println("Passenger 180 updated to tier " + tierChangePassenger.getMembershipTier()
                + " before request insertion.");
        System.out.println(scheduler.insertPassengerRequest(180));
        System.out.println("Heap after tier-update insert: " + scheduler.heap.stateString());

        PickupRequest peek = scheduler.heap.peek();
        if (peek != null)
        {
            System.out.println("Peek highest priority: " + peek.label());
        }

        for (int i = 0; i < 5; i++)
        {
            // Five extractions show that the highest-priority request leaves first.
            PickupRequest request = scheduler.extractNextDispatch();
            System.out.println("Extracted dispatch: " + request.label());
            System.out.println("Heap after extraction: " + scheduler.heap.stateString());
        }
        System.out.println("");
    }

    private static void module4(Graph graph, PassengerRecord[] passengers, DriverRecord[] drivers)
    {
        // Module 4 first shows a small sorted sample, then prints the benchmark table.
        System.out.println("=== MODULE 4: SORTING PICKUP RECORDS ===");
        PickupRecord[] sample = Sorting.generateDataset(graph, passengers, drivers, 12);
        SortResult merge = Sorting.mergeSort(sample);
        SortResult quick = Sorting.quickSort(sample);

        System.out.println("Merge sort first 12 sorted by EstimatedPickupTime:");
        String recordLine = "";
        PickupRecord[] mergeRecords = merge.getRecords();
        PickupRecord[] quickRecords = quick.getRecords();
        for (int i = 0; i < mergeRecords.length; i++)
        {
            recordLine = recordLine + "R" + mergeRecords[i].getRequestId() + ":T"
                    + mergeRecords[i].getEstimatedPickupTime();
            if (i < mergeRecords.length - 1)
            {
                recordLine = recordLine + ", ";
            }
        }
        System.out.println(recordLine);

        System.out.println("Merge sorted correctly: " + Sorting.isSorted(mergeRecords)
                + ", comparisons=" + merge.getMetrics().getComparisons());
        System.out.println("Quick sorted correctly: " + Sorting.isSorted(quickRecords)
                + ", comparisons=" + quick.getMetrics().getComparisons());

        System.out.println("Condition correctness preview on 100 records:");
        String[] previewConditions = {"random", "nearly_sorted", "reversed"};
        PickupRecord[] previewBase = Sorting.generateDataset(graph, passengers, drivers, 100);
        for (int i = 0; i < previewConditions.length; i++)
        {
            PickupRecord[] prepared = Sorting.prepareCondition(previewBase, previewConditions[i]);
            SortResult previewMerge = Sorting.mergeSort(prepared);
            SortResult previewQuick = Sorting.quickSort(prepared);
            System.out.println(previewConditions[i] + ": merge_sorted="
                    + Sorting.isSorted(previewMerge.getRecords()) + ", quick_sorted="
                    + Sorting.isSorted(previewQuick.getRecords()) + ", first/last T="
                    + timeRange(previewMerge.getRecords()));
        }

        String benchmark = Sorting.benchmark(graph, passengers, drivers);
        System.out.println("Benchmark table:");
        System.out.println(benchmark);
    }

    private static String timeRange(PickupRecord[] records)
    {
        String range = "empty";
        if (records.length > 0)
        {
            range = records[0].getEstimatedPickupTime() + "/"
                    + records[records.length - 1].getEstimatedPickupTime();
        }
        return range;
    }
}
