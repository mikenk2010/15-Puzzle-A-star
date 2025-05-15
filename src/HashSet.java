public class HashSet {
    String[] table;
    int capacity;
    int size;

    HashSet(int cap) {
        capacity = cap;
        table = new String[capacity];
        size = 0;
    }

    private int hash(String key) {
        int h = 0;
        for (int i = 0; i < key.length(); i++) {
            h = (31 * h + key.charAt(i)) % capacity;
        }
        return h;
    }

    boolean contains(String key) {
        int index = hash(key);
        int start = index;

        while (table[index] != null) {
            if (table[index].equals(key)) return true;
            index = (index + 1) % capacity;
            if (index == start) break; // full loop
        }

        return false;
    }

    void add(String key) {
        if (contains(key)) return;
        int index = hash(key);
        while (table[index] != null) {
            index = (index + 1) % capacity;
        }
        table[index] = key;
        size++;
    }
}
