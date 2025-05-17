public class HashSet {
    // Array to store unique strings
    private String[] table;

    // Current capacity of the table
    private int capacity;

    // Number of elements stored in the set
    private int size;

    // Load factor threshold to trigger resizing
    private static final double LOAD_FACTOR = 0.9;

    // Constructor to initialize the hash set with specified capacity
    public HashSet(int cap) {
        capacity = cap;
        table = new String[capacity];
        size = 0;
    }

    // Compute the hash index for a given key using linear probing (non-negative)
    private int hashIndex(String key) {
        int hash = key.hashCode();
        return (hash & 0x7FFFFFFF) % capacity; // Ensure non-negative index
    }

    // Check if a key is present in the set
    public boolean contains(String key) {
        int index = hashIndex(key);
        int start = index; // Track starting index to detect full cycle

        // Linear probing: search until null or key is found
        while (table[index] != null) {
            if (table[index].equals(key)) return true; // Key found
            index = (index + 1) % capacity; // Move to next slot
            if (index == start) break; // full cycle, key not found
        }

        return false; // Key not found
    }

    // Add a new key to the set (if not already present)
    public void add(String key) {
        if (contains(key)) return; // Avoid duplicates

        // Resize table if load factor exceeds threshold
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }

        int index = hashIndex(key);

        // Find an empty slot using linear probing
        while (table[index] != null) {
            index = (index + 1) % capacity;
        }

        table[index] = key; // Insert key
        size++;
    }

    // Resize the hash table to double the current capacity
    private void resize() {
        int newCapacity = capacity * 2;
        String[] oldTable = table;

        // Create new table and reset metadata
        table = new String[newCapacity];
        capacity = newCapacity;
        size = 0;

        // Reinsert all non-null keys from old table
        for (String key : oldTable) {
            if (key != null) {
                add(key); // rehash keys into new table
            }
        }
    }
}