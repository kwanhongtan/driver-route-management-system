/*
 * Author: Tan Kwan Hong
 * Date: 27 May 2026
 * Description: Stores a pending pickup request and calculates its priority score.
 */

public class PickupRequest
{
    private int requestId;
    private PassengerRecord passenger;
    private DriverRecord driver;
    private int estimatedPickupTime;
    private String path;
    private double priority;

    public PickupRequest(int requestId, PassengerRecord passenger, DriverRecord driver, int estimatedPickupTime, String path)
    {
        // A scheduled request only makes sense when it has both sides of the match:
        // the passenger asking for pickup and the driver assigned to reach them.
        if (passenger == null || driver == null)
        {
            throw new IllegalArgumentException("Passenger and driver must be provided.");
        }
        if (estimatedPickupTime <= 0)
        {
            throw new IllegalArgumentException("EstimatedPickupTime must be positive.");
        }
        this.requestId = requestId;
        this.passenger = passenger;
        this.driver = driver;
        this.estimatedPickupTime = estimatedPickupTime;
        this.path = path;
        // The max heap dispatches larger scores first, so this formula is stored
        // directly in the request.
        this.priority = (6 - passenger.getMembershipTier()) + (1000.0 / estimatedPickupTime);
    }

    public PickupRecord toPickupRecord()
    {
        // When a request becomes part of reporting, copy the important details into
        // a PickupRecord so sorting can work on a simple snapshot.
        return new PickupRecord(requestId, passenger.getPassengerId(), driver.getDriverId(), passenger.getName(),
                passenger.getPickupLocation(), driver.getCurrentLocation(), passenger.getMembershipTier(),
                estimatedPickupTime, priority);
    }

    public String label()
    {
        // This shorter label keeps the heap trace readable after each insert/extract.
        return "R" + requestId + ":P" + passenger.getPassengerId() + "/D" + driver.getDriverId()
                + "/T" + estimatedPickupTime + "/" + String.format("%.2f", priority);
    }

    public int getRequestId()
    {
        return requestId;
    }

    public void setRequestId(int requestId)
    {
        this.requestId = requestId;
    }

    public PassengerRecord getPassenger()
    {
        return passenger;
    }

    public void setPassenger(PassengerRecord passenger)
    {
        if (passenger == null)
        {
            throw new IllegalArgumentException("Passenger must be provided.");
        }
        this.passenger = passenger;
        // Changing the passenger can change the membership tier, so update priority too.
        recalculatePriority();
    }

    public DriverRecord getDriver()
    {
        return driver;
    }

    public void setDriver(DriverRecord driver)
    {
        if (driver == null)
        {
            throw new IllegalArgumentException("Driver must be provided.");
        }
        this.driver = driver;
    }

    public int getEstimatedPickupTime()
    {
        return estimatedPickupTime;
    }

    public void setEstimatedPickupTime(int estimatedPickupTime)
    {
        if (estimatedPickupTime <= 0)
        {
            throw new IllegalArgumentException("EstimatedPickupTime must be positive.");
        }
        this.estimatedPickupTime = estimatedPickupTime;
        // Changing T changes 1000 / T, so priority must be refreshed.
        recalculatePriority();
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public double getPriority()
    {
        return priority;
    }

    public void setPriority(double priority)
    {
        this.priority = priority;
    }

    private void recalculatePriority()
    {
        // This is the assignment formula: better tier and shorter pickup time both
        // increase the request's dispatch priority.
        priority = (6 - passenger.getMembershipTier()) + (1000.0 / estimatedPickupTime);
    }
}
