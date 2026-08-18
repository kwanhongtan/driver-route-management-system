/*
 * Author: Tan Kwan Hong
 * Date: 27 May 2026
 * Description: Stores one driver record used by lookup and scheduling.
 */

public class DriverRecord
{
    private String key;
    // I keep a separate String key because the hash table works with String keys.
    private int driverId;
    private String name;
    private String currentLocation;
    private String availabilityStatus;

    public DriverRecord(int driverId, String name, String currentLocation, String availabilityStatus)
    {
        // I check the driver data before storing it, because bad locations or
        // statuses would break dispatch decisions later.
        if (driverId <= 0)
        {
            throw new IllegalArgumentException("DriverID must be positive.");
        }
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Driver name must be non-empty.");
        }
        if (currentLocation == null || currentLocation.trim().isEmpty())
        {
            throw new IllegalArgumentException("CurrentLocation must be non-empty.");
        }
        if (!isValidStatus(availabilityStatus))
        {
            throw new IllegalArgumentException("AvailabilityStatus must be Available, Busy, or Offline.");
        }
        // At this point the record is safe to use in lookup and scheduling.
        this.key = "" + driverId;
        this.driverId = driverId;
        this.name = name;
        this.currentLocation = currentLocation;
        this.availabilityStatus = availabilityStatus;
    }

    private boolean isValidStatus(String status)
    {
        boolean valid = false;
        // I keep the status spelling strict so different spellings do not silently pass.
        if ("Available".equals(status) || "Busy".equals(status) || "Offline".equals(status))
        {
            valid = true;
        }
        return valid;
    }

    public String toString()
    {
        return "Driver(" + driverId + ", " + name + ", location=" + currentLocation + ", status=" + availabilityStatus + ")";
    }

    public String getKey()
    {
        // ZipRideApp uses this when inserting the driver into the hash table.
        return key;
    }

    public int getDriverId()
    {
        return driverId;
    }

    public void setDriverId(int driverId)
    {
        if (driverId <= 0)
        {
            throw new IllegalArgumentException("DriverID must be positive.");
        }
        this.driverId = driverId;
        // If the ID changes, I update the key as well so lookup stays consistent.
        this.key = "" + driverId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Driver name must be non-empty.");
        }
        this.name = name;
    }

    public String getCurrentLocation()
    {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation)
    {
        if (currentLocation == null || currentLocation.trim().isEmpty())
        {
            throw new IllegalArgumentException("CurrentLocation must be non-empty.");
        }
        this.currentLocation = currentLocation;
    }

    public String getAvailabilityStatus()
    {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus)
    {
        // Only the three statuses from the assignment are accepted here.
        if (!isValidStatus(availabilityStatus))
        {
            throw new IllegalArgumentException("AvailabilityStatus must be Available, Busy, or Offline.");
        }
        this.availabilityStatus = availabilityStatus;
    }
}
