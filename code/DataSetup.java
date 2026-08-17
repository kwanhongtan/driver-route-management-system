/*
 * Author: Tan Kwan Hong
 * Student ID: 23442423
 * Date: 28 May 2026
 * Description: Provides fixed city, passenger, and driver test data for the demo.
 */

public class DataSetup
{
    public static Graph buildCityGraph()
    {
        Graph graph = new Graph();
        // I use a mix of city places and suburbs here so the graph feels like a
        // small road network instead of just random letters.
        String[] locations = {"CBD", "Airport", "University", "SuburbNorth", "SuburbSouth",
                "ShoppingMall", "Hospital", "IndustrialPark", "Stadium", "Harbour", "IsolatedDepot"};
        for (int i = 0; i < locations.length; i++)
        {
            graph.addLocation(locations[i]);
        }
        // I leave IsolatedDepot disconnected on purpose so the unreachable path
        // case can be shown in the demo.
        graph.addRoad("CBD", "University", 8);
        graph.addRoad("CBD", "ShoppingMall", 6);
        graph.addRoad("CBD", "Hospital", 7);
        graph.addRoad("University", "SuburbNorth", 10);
        graph.addRoad("SuburbNorth", "Hospital", 5);
        graph.addRoad("Hospital", "ShoppingMall", 4);
        graph.addRoad("ShoppingMall", "SuburbSouth", 9);
        graph.addRoad("SuburbSouth", "Airport", 12);
        graph.addRoad("Airport", "Harbour", 11);
        graph.addRoad("Harbour", "IndustrialPark", 7);
        graph.addRoad("IndustrialPark", "Stadium", 6);
        graph.addRoad("Stadium", "CBD", 14);
        return graph;
    }

    public static PassengerRecord[] passengers()
    {
        // I spread the passengers across all membership tiers and pickup locations
        // so the hash table and scheduler have enough variety to test.
        return new PassengerRecord[]
        {
                new PassengerRecord(101, "Aisha Rahman", "CBD", 1),
                new PassengerRecord(102, "Ben Tan", "Airport", 3),
                new PassengerRecord(103, "Chloe Lim", "University", 2),
                new PassengerRecord(104, "Daniel Wong", "SuburbNorth", 5),
                new PassengerRecord(105, "Elena Smith", "ShoppingMall", 1),
                new PassengerRecord(106, "Farid Ali", "Hospital", 4),
                new PassengerRecord(107, "Grace Lee", "SuburbSouth", 2),
                new PassengerRecord(108, "Hannah Ng", "IndustrialPark", 3),
                new PassengerRecord(109, "Isaac Khoo", "Stadium", 5),
                new PassengerRecord(110, "Jia Chen", "Harbour", 1),
                new PassengerRecord(111, "Kumar Raj", "CBD", 2),
                new PassengerRecord(112, "Lily Ong", "Airport", 4),
                new PassengerRecord(113, "Marcus Teo", "University", 1),
                new PassengerRecord(114, "Nora Abdullah", "ShoppingMall", 5),
                new PassengerRecord(115, "Oscar Yap", "Hospital", 3),
                new PassengerRecord(116, "Priya Das", "SuburbNorth", 2),
                new PassengerRecord(117, "Quinn Ho", "SuburbSouth", 1),
                new PassengerRecord(118, "Rachel Foo", "Stadium", 4),
                new PassengerRecord(119, "Samuel Goh", "Harbour", 3),
                new PassengerRecord(120, "Tara Singh", "IndustrialPark", 2)
        };
    }

    public static DriverRecord[] drivers()
    {
        // I mix the driver statuses so the scheduler has to ignore Busy and Offline
        // drivers, but those records can still be searched in the hash table.
        return new DriverRecord[]
        {
                new DriverRecord(201, "Adam Driver", "CBD", "Available"),
                new DriverRecord(202, "Bella Cruz", "Airport", "Available"),
                new DriverRecord(203, "Caleb Ford", "University", "Busy"),
                new DriverRecord(204, "Dina Park", "SuburbNorth", "Available"),
                new DriverRecord(205, "Evan Stone", "ShoppingMall", "Offline"),
                new DriverRecord(206, "Fiona Tay", "Hospital", "Available"),
                new DriverRecord(207, "Gavin Low", "SuburbSouth", "Busy"),
                new DriverRecord(208, "Helen Wu", "IndustrialPark", "Available"),
                new DriverRecord(209, "Ivan Ho", "Stadium", "Available"),
                new DriverRecord(210, "Jenny Chan", "Harbour", "Offline"),
                new DriverRecord(211, "Ken Adams", "CBD", "Available"),
                new DriverRecord(212, "Laura Tan", "Airport", "Busy"),
                new DriverRecord(213, "Mika Ito", "University", "Available"),
                new DriverRecord(214, "Noah Lee", "ShoppingMall", "Available"),
                new DriverRecord(215, "Olivia Koh", "Hospital", "Offline"),
                new DriverRecord(216, "Peter Lim", "SuburbNorth", "Available"),
                new DriverRecord(217, "Qian Xu", "SuburbSouth", "Available"),
                new DriverRecord(218, "Rina Patel", "Stadium", "Busy"),
                new DriverRecord(219, "Sean Ng", "Harbour", "Available"),
                new DriverRecord(220, "Tina Yu", "IndustrialPark", "Available")
        };
    }
}
