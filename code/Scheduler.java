/*
 * Author: Tan Kwan Hong
 * Student ID: 23442423
 * Date: 28 May 2026
 * Description: Connects passenger lookup, driver lookup, graph routes, and heap scheduling.
 */

public class Scheduler
{
    public final PickupMaxHeap heap;
    private Graph graph;
    private HashTable passengerTable;
    private HashTable driverTable;
    private int nextRequestId;

    public Scheduler(Graph graph, HashTable passengerTable, HashTable driverTable)
    {
        // This is where the modules meet: passenger data comes from the passenger
        // table, driver data comes from the driver table, and travel time comes
        // from the graph.
        this.graph = graph;
        this.passengerTable = passengerTable;
        this.driverTable = driverTable;
        this.heap = new PickupMaxHeap();
        this.nextRequestId = 1;
    }

    public String insertPassengerRequest(int passengerId)
    {
        // I look up the passenger first because the request needs the pickup
        // location and membership tier.
        HashTable.SearchResult passengerResult = passengerTable.search("" + passengerId);
        PassengerRecord passenger = (PassengerRecord)passengerResult.getValue();
        String message;
        if (passenger == null)
        {
            message = "Request rejected: passenger " + passengerId + " was not found.";
        }
        else
        {
            // I only consider available drivers here. Busy and Offline drivers stay
            // in the hash table, but they are filtered out for matching.
            DriverRecord[] drivers = driverTable.availableDrivers();
            DriverRecord bestDriver = null;
            int bestTime = 0;
            String bestPath = "";

            // I try every available driver because the nearest one is only known
            // after Dijkstra calculates the travel time.
            for (int i = 0; i < drivers.length; i++)
            {
                int time = 0;
                String path = "";
                boolean reachable = true;

                if (drivers[i].getCurrentLocation().equals(passenger.getPickupLocation()))
                {
                    // The driver is already there. I use T = 1 instead of 0 because
                    // the priority formula divides by T.
                    time = 1;
                    path = drivers[i].getCurrentLocation();
                }
                else
                {
                    // This is the main link to Module 1: route planning gives the
                    // EstimatedPickupTime used in the priority formula.
                    Graph.PathResult route = graph.dijkstra(drivers[i].getCurrentLocation(), passenger.getPickupLocation());
                    if (!route.reachable)
                    {
                        reachable = false;
                    }
                    else
                    {
                        time = route.time;
                        path = route.path;
                    }
                }

                if (reachable == true && (bestDriver == null || time < bestTime))
                {
                    // I replace the best driver whenever I find a shorter reachable route.
                    bestDriver = drivers[i];
                    bestTime = time;
                    bestPath = path;
                }
            }

            if (bestDriver == null)
            {
                message = "Request queued: no available reachable driver for passenger " + passengerId + ".";
            }
            else
            {
                // Once the nearest driver is known, I create the request. The request
                // calculates its own priority from membership tier and pickup time.
                PickupRequest request = new PickupRequest(nextRequestId++, passenger, bestDriver, bestTime, bestPath);
                bestDriver.setAvailabilityStatus("Busy");
                // The heap does not choose the nearest driver. It only decides which
                // prepared request should be dispatched first.
                heap.insert(request);
                message = "Request " + request.getRequestId() + ": passenger " + passengerId + " assigned nearest driver "
                        + bestDriver.getDriverId() + ", T=" + bestTime + ", path=" + bestPath
                        + ", priority=" + String.format("%.2f", request.getPriority());
            }
        }
        return message;
    }

    public PickupRequest extractNextDispatch()
    {
        // extractPriority removes the root of the max heap, which is the most urgent request.
        PickupRequest request = heap.extractPriority();
        if (request != null)
        {
            // The driver was reserved during insertion and remains busy after dispatch.
            request.getDriver().setAvailabilityStatus("Busy");
        }
        return request;
    }
}
