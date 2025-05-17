public class MinHeap {
    // Array-based binary heap to store Node elements
    Node[] heap;

    // Current number of elements in the heap
    int size;

    // Constructor to initialize the heap with a fixed initial capacity
    public MinHeap(int capacity) {
        heap = new Node[capacity];
        size = 0;
    }

    // Insert a new node into the heap and maintain the min-heap property
    public void insert(Node node) {
        // If heap is full, double its capacity
        if (size == heap.length) resize();

        // Place the node at the end of the heap
        heap[size] = node;
        int pos = size++;

        // Bubble up: compare with parent and swap if needed
        while (pos > 0 && heap[pos].priority < heap[(pos - 1) / 2].priority) {
            swap(pos, (pos - 1) / 2);
            pos = (pos - 1) / 2;
        }
    }

    // Resize the heap array by doubling its capacity
    private void resize() {
        Node[] newHeap = new Node[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        heap = newHeap;
    }

    // Remove and return the node with the smallest priority (root of the heap)
    public Node remove() {
        Node top = heap[0];     // The root node (minimum)
        heap[0] = heap[--size]; // Move last element to root
        heap[size] = null;      // Help GC by nullifying removed slot
        heapify(0);      // Restore heap property
        return top;
    }

    // Recursive method to restore heap property from given index downwards
    private void heapify(int index) {
        int position = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        // Find the smallest among index, left, and right children
        if (left < size && heap[left].priority < heap[position].priority) position = left;
        if (right < size && heap[right].priority < heap[position].priority) position = right;

        // If smallest is not current node, swap and recurse
        if (position != index) {
            swap(index, position);
            heapify(position);
        }
    }

    // Check if the heap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Swap two nodes in the heap array
    public void swap(int i, int j) {
        Node temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}