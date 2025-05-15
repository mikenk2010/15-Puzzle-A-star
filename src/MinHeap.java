public class MinHeap {
    Node[] heap;
    int size;

    MinHeap(int capacity) {
        heap = new Node[capacity];
        size = 0;
    }

    void insert(Node node) {
        heap[size] = node;
        int position = size++;
        while (position > 0 && heap[position].priority < heap[(position - 1) / 2].priority) {
            swap(position, (position - 1) / 2);
            position = (position - 1) / 2;
        }
    }

    Node remove() {
        Node top = heap[0];
        heap[0] = heap[--size];
        heapify(0);
        return top;
    }

    void heapify(int index) {
        int position = index, left = 2 * index + 1, right = 2 * index + 2;

        if (left < size && heap[left].priority < heap[position].priority) position = left;
        if (right < size && heap[right].priority < heap[position].priority) position = right;

        if (position != index) {
            swap(index, position);
            heapify(position);
        }
    }

    boolean isEmpty() {
        return size == 0;
    }

    void swap(int i, int j) {
        Node temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}