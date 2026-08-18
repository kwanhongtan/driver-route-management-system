/*
 * Author: Tan Kwan Hong
 * Date: 27 May 2026
 * Description: Stores one passenger record used by lookup and scheduling.
 */

public class PassengerRecord
{
    private String key;
    // I keep a separate String key because the hash table works with String keys.
    private int passengerId;
    private String name;
    private String pickupLocation;
    private int membershipTier;

    public PassengerRecord(int passengerId, String name, String pickupLocation, int membershipTier)
    {
        // I check the data before storing it, because bad passenger data would
        // make the later lookup and scheduling output confusing.
        if (passengerId <= 0)
        {
            throw new IllegalArgumentException("PassengerID must be positive.");
        }
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Passenger name must be non-empty.");
        }
        if (pickupLocation == null || pickupLocation.trim().isEmpty())
        {
            throw new IllegalArgumentException("PickupLocation must be non-empty.");
        }
        if (membershipTier < 1 || membershipTier > 5)
        {
            throw new IllegalArgumentException("MembershipTier must be from 1 to 5.");
        }
        // I only store the values after all checks pass.
        this.key = "" + passengerId;
        this.passengerId = passengerId;
        this.name = name;
        this.pickupLocation = pickupLocation;
        this.membershipTier = membershipTier;
    }

    public String toString()
    {
        return "Passenger(" + passengerId + ", " + name + ", pickup=" + pickupLocation + ", tier=" + membershipTier + ")";
    }

    public String getKey()
    {
        // ZipRideApp uses this when inserting the passenger into the hash table.
        return key;
    }

    public int getPassengerId()
    {
        return passengerId;
    }

    public void setPassengerId(int passengerId)
    {
        if (passengerId <= 0)
        {
            throw new IllegalArgumentException("PassengerID must be positive.");
        }
        this.passengerId = passengerId;
        // If the ID changes, I update the key as well so lookup stays consistent.
        this.key = "" + passengerId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Passenger name must be non-empty.");
        }
        this.name = name;
    }

    public String getPickupLocation()
    {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation)
    {
        if (pickupLocation == null || pickupLocation.trim().isEmpty())
        {
            throw new IllegalArgumentException("PickupLocation must be non-empty.");
        }
        this.pickupLocation = pickupLocation;
    }

    public int getMembershipTier()
    {
        return membershipTier;
    }

    public void setMembershipTier(int membershipTier)
    {
        // Tier 1 is highest priority and tier 5 is lowest, matching the brief.
        if (membershipTier < 1 || membershipTier > 5)
        {
            throw new IllegalArgumentException("MembershipTier must be from 1 to 5.");
        }
        this.membershipTier = membershipTier;
    }
}
