/*
 * Author: Tan Kwan Hong
 * Date: 27 May 2026
 * Description: Implements an open-addressing hash table for passenger and driver lookup.
 */

public class HashTable
{
    private static final double LOAD_FACTOR_LIMIT = 0.70;

    private static class HashEntry
    {
        private String key;
        private Object value;
        // I keep a separate state value because null alone is not enough after
        // deletion. A deleted bucket still has to let probing move past it.
        private int state; // 0 = empty, 1 = used, -1 = deleted

        HashEntry()
        {
            key = "";
            value = null;
            state = 0;
        }

        public String getKey()
        {
            return key;
        }

        public void setKey(String key)
        {
            this.key = key;
        }

        public Object getValue()
        {
            return value;
        }

        public void setValue(Object value)
        {
            this.value = value;
        }

        public int getState()
        {
            return state;
        }

        public void setState(int state)
        {
            this.state = state;
        }
    }

    public static class SearchResult
    {
        private Object value;
        private int comparisons;

        SearchResult(Object value, int comparisons)
        {
            this.value = value;
            this.comparisons = comparisons;
        }

        public Object getValue()
        {
            return value;
        }

        public void setValue(Object value)
        {
            this.value = value;
        }

        public int getComparisons()
        {
            return comparisons;
        }

        public void setComparisons(int comparisons)
        {
            this.comparisons = comparisons;
        }
    }

    private HashEntry[] hashArray;
    private int tableSize;
    private int count;
    private String tableName;
    private int operationCount;

    public HashTable(int size, String tableName)
    {
        // I pass in a prime table size because it usually spreads the keys better
        // when modulo hashing is used.
        tableSize = size;
        this.tableName = tableName;
        initialiseTable(tableSize);
        count = 0;
        operationCount = 0;
    }

    public int hashFunction(String key)
    {
        if (key == null || key.trim().isEmpty())
        {
            throw new IllegalArgumentException("Hash key must be non-empty.");
        }

        int hashVal = 0;

        for (int i = 0; i < key.length(); i++)
        {
            hashVal = (hashVal * 31) + key.charAt(i);
        }

        // The multiplier helps different key strings spread across the table.
        return Math.abs(hashVal) % tableSize;
    }

    public String insert(String key, Object value)
    {
        if (value == null)
        {
            throw new IllegalArgumentException("Cannot insert null value.");
        }

        int existingIndex = findIndex(key);
        String message;

        // If the key is already present, I update the value instead of adding a
        // duplicate key.
        if (existingIndex != -1)
        {
            hashArray[existingIndex].setValue(value);
            message = "UPDATED key " + key + " at bucket " + existingIndex;
        }
        else
        {
            if (((double)(count + 1) / tableSize) > LOAD_FACTOR_LIMIT)
            {
                // Grow before the table gets crowded enough to make probing slow.
                resize();
            }

            int index = hashFunction(key);
            int originalIndex = index;
            boolean inserted = false;
            int comparisons = 0;

            // Linear probing is easy to show in the output: on a collision, I move
            // one bucket to the right until an empty or deleted slot appears.
            while (inserted == false)
            {
                comparisons++;

                if (hashArray[index].getState() == 0 || hashArray[index].getState() == -1)
                {
                    // Both never-used buckets and deleted buckets can hold a new value.
                    hashArray[index].setKey(key);
                    hashArray[index].setValue(value);
                    hashArray[index].setState(1);
                    count++;
                    inserted = true;
                }
                else
                {
                    // Collision case: move to the next bucket and wrap to 0 if needed.
                    index = (index + 1) % tableSize;

                    if (index == originalIndex)
                    {
                        throw new RuntimeException("Hash table is full.");
                    }
                }
            }

            operationCount = operationCount + comparisons;
            message = "INSERTED key " + key + " at bucket " + index;
        }
        return message;
    }

    public SearchResult search(String key)
    {
        int index = hashFunction(key);
        int originalIndex = index;
        boolean found = false;
        boolean giveUp = false;
        int comparisons = 0;

        while (found == false && giveUp == false)
        {
            comparisons++;

            if (hashArray[index].getState() == 0)
            {
                // Once a never-used bucket appears, this key cannot be later in the
                // same probe chain, so the search can stop.
                giveUp = true;
            }
            else if (hashArray[index].getState() == 1 && hashArray[index].getKey().equals(key))
            {
                // This is the successful hit case.
                found = true;
            }
            else
            {
                // Deleted buckets and occupied non-matches do not stop the search,
                // because the wanted key may be further along the probe chain.
                index = (index + 1) % tableSize;

                if (index == originalIndex)
                {
                    giveUp = true;
                }
            }
        }

        operationCount = operationCount + comparisons;

        SearchResult result;
        if (found == true)
        {
            result = new SearchResult(hashArray[index].getValue(), comparisons);
        }
        else
        {
            result = new SearchResult(null, comparisons);
        }
        return result;
    }

    public String delete(String key)
    {
        int index = hashFunction(key);
        int originalIndex = index;
        boolean found = false;
        boolean giveUp = false;
        int comparisons = 0;

        while (found == false && giveUp == false)
        {
            comparisons++;

            if (hashArray[index].getState() == 0)
            {
                // Same rule as search(): a never-used bucket means the key was not inserted.
                giveUp = true;
            }
            else if (hashArray[index].getState() == 1 && hashArray[index].getKey().equals(key))
            {
                found = true;
            }
            else
            {
                index = (index + 1) % tableSize;

                if (index == originalIndex)
                {
                    giveUp = true;
                }
            }
        }

        operationCount = operationCount + comparisons;

        String message;
        if (found == true)
        {
            // I mark the bucket as deleted, not empty. Otherwise, searches for keys
            // placed after this bucket could stop too early.
            hashArray[index].setKey("");
            hashArray[index].setValue(null);
            hashArray[index].setState(-1);
            count--;
            message = "DELETED key " + key + " from bucket " + index + ", comparisons=" + comparisons;
        }
        else
        {
            message = "MISSING key " + key + " after probing from bucket " + hashFunction(key)
                    + ", comparisons=" + comparisons;
        }
        return message;
    }

    public double loadFactor()
    {
        // I print the load factor in the demo to show the table is not too crowded.
        double lf = (double)count / tableSize;
        return lf;
    }

    public String bucketState(int startIndex)
    {
        String output = "";
        int index = startIndex;
        int steps = 0;
        boolean stop = false;

        while (steps < tableSize && stop == false)
        {
            // This helper is mainly for the assignment trace, because it shows where
            // a collision travels through the table.
            output = output + index + ":";

            if (hashArray[index].getState() == 1)
            {
                output = output + hashArray[index].getKey();
            }
            else if (hashArray[index].getState() == -1)
            {
                output = output + "deleted";
            }
            else
            {
                output = output + "empty";
                stop = true;
            }

            if (stop == false)
            {
                output = output + " -> ";
            }

            index = (index + 1) % tableSize;
            steps++;
        }

        return output;
    }

    public DriverRecord[] availableDrivers()
    {
        DriverRecord[] temp = new DriverRecord[count];
        int used = 0;

        // Drivers are spread across the table, so I scan the buckets to collect the
        // ones currently marked Available.
        for (int i = 0; i < tableSize; i++)
        {
            if (hashArray[i].getState() == 1 && hashArray[i].getValue() instanceof DriverRecord)
            {
                DriverRecord driver = (DriverRecord)hashArray[i].getValue();

                if (driver.getAvailabilityStatus().equals("Available"))
                {
                    temp[used] = driver;
                    used++;
                }
            }
        }

        DriverRecord[] drivers = new DriverRecord[used];

        // The temporary array may have unused null slots, so copy only the filled part.
        for (int i = 0; i < used; i++)
        {
            drivers[i] = temp[i];
        }

        return drivers;
    }

    public String summary()
    {
        return tableName + " records=" + count + ", table_size=" + tableSize
                + ", load_factor=" + String.format("%.3f", loadFactor())
                + ", operation_count=" + operationCount;
    }

    private void initialiseTable(int size)
    {
        hashArray = new HashEntry[size];

        for (int i = 0; i < size; i++)
        {
            // I create every bucket upfront so insert/search/delete can just check
            // the bucket state without extra null checks.
            hashArray[i] = new HashEntry();
        }
    }

    private void resize()
    {
        HashEntry[] oldArray = hashArray;
        int oldSize = tableSize;
        int newSize = nextPrime(tableSize * 2);

        tableSize = newSize;
        initialiseTable(tableSize);
        count = 0;

        // Rebuild the table with only active entries. Deleted slots are left
        // behind, which also shortens future probe chains after a resize.
        for (int i = 0; i < oldSize; i++)
        {
            if (oldArray[i].getState() == 1)
            {
                insertRehashed(oldArray[i].getKey(), oldArray[i].getValue());
            }
        }
    }

    private void insertRehashed(String key, Object value)
    {
        int index = hashFunction(key);
        boolean inserted = false;

        while (inserted == false)
        {
            if (hashArray[index].getState() == 0)
            {
                hashArray[index].setKey(key);
                hashArray[index].setValue(value);
                hashArray[index].setState(1);
                count++;
                inserted = true;
            }
            else
            {
                index = (index + 1) % tableSize;
            }
        }
    }

    private int nextPrime(int start)
    {
        int candidate = start;
        if (candidate % 2 == 0)
        {
            candidate++;
        }

        while (!isPrime(candidate))
        {
            candidate = candidate + 2;
        }

        return candidate;
    }

    private boolean isPrime(int number)
    {
        boolean prime = number >= 2;

        if (number > 2 && number % 2 == 0)
        {
            prime = false;
        }

        for (int divisor = 3; divisor * divisor <= number && prime == true; divisor = divisor + 2)
        {
            if (number % divisor == 0)
            {
                prime = false;
            }
        }

        return prime;
    }

    private int findIndex(String key)
    {
        int index = hashFunction(key);
        int originalIndex = index;
        boolean found = false;
        boolean giveUp = false;

        // I use the same probe path as search(). This stays private because insert
        // only needs it to decide whether to update or add.
        while (found == false && giveUp == false)
        {
            if (hashArray[index].getState() == 0)
            {
                giveUp = true;
            }
            else if (hashArray[index].getState() == 1 && hashArray[index].getKey().equals(key))
            {
                found = true;
            }
            else
            {
                index = (index + 1) % tableSize;

                if (index == originalIndex)
                {
                    giveUp = true;
                }
            }
        }

        int result;
        if (found == true)
        {
            result = index;
        }
        else
        {
            result = -1;
        }
        return result;
    }
}
