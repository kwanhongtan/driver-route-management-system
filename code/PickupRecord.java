/*
 * Author: Tan Kwan Hong
 * Student ID: 23442423
 * Date: 27 May 2026
 * Description: Stores a completed pickup record for sorting and reports.
 */

public class PickupRecord
{
    private int requestId;
    private int passengerId;
    private int driverId;
    private String passengerName;
    private String pickupLocation;
    private String driverStartLocation;
    private int membershipTier;
    private int estimatedPickupTime;
    private double priority;

    public PickupRecord(int requestId, int passengerId, int driverId, String passengerName,
                        String pickupLocation, String driverStartLocation, int membershipTier,
                        int estimatedPickupTime, double priority)
                        {
        // This record is a snapshot for reporting. If a driver status or passenger
        // detail changes later, the completed pickup report still keeps its original data.
        this.requestId = requestId;
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.passengerName = passengerName;
        this.pickupLocation = pickupLocation;
        this.driverStartLocation = driverStartLocation;
        this.membershipTier = membershipTier;
        this.estimatedPickupTime = estimatedPickupTime;
        this.priority = priority;
    }

    public PickupRecord copy()
    {
        // Sorting benchmarks need clean copies so each algorithm starts from the same data.
        return new PickupRecord(requestId, passengerId, driverId, passengerName, pickupLocation,
                driverStartLocation, membershipTier, estimatedPickupTime, priority);
    }

    public String toString()
    {
        return "Pickup(" + requestId + ", passenger=" + passengerId + ", driver=" + driverId
                + ", T=" + estimatedPickupTime + ", priority=" + String.format("%.2f", priority) + ")";
    }

    public int getRequestId()
    {
        return requestId;
    }

    public void setRequestId(int requestId)
    {
        this.requestId = requestId;
    }

    public int getPassengerId()
    {
        return passengerId;
    }

    public void setPassengerId(int passengerId)
    {
        this.passengerId = passengerId;
    }

    public int getDriverId()
    {
        return driverId;
    }

    public void setDriverId(int driverId)
    {
        this.driverId = driverId;
    }

    public String getPassengerName()
    {
        return passengerName;
    }

    public void setPassengerName(String passengerName)
    {
        this.passengerName = passengerName;
    }

    public String getPickupLocation()
    {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation)
    {
        this.pickupLocation = pickupLocation;
    }

    public String getDriverStartLocation()
    {
        return driverStartLocation;
    }

    public void setDriverStartLocation(String driverStartLocation)
    {
        this.driverStartLocation = driverStartLocation;
    }

    public int getMembershipTier()
    {
        return membershipTier;
    }

    public void setMembershipTier(int membershipTier)
    {
        this.membershipTier = membershipTier;
    }

    public int getEstimatedPickupTime()
    {
        return estimatedPickupTime;
    }

    public void setEstimatedPickupTime(int estimatedPickupTime)
    {
        this.estimatedPickupTime = estimatedPickupTime;
    }

    public double getPriority()
    {
        return priority;
    }

    public void setPriority(double priority)
    {
        this.priority = priority;
    }
}
