package wroldmap;

import java.util.ArrayList;

public class MinHeap<T extends Comparable<T>> {
    private ArrayList<T> heap;

    public MinHeap() {
        heap = new ArrayList<>();
    }

    public void insert(T element) {
        heap.add(element);
        int index = heap.size() - 1;
        heapifyUp(index);
    }

    public T extractMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }

        T min = heap.get(0);
        int lastIdx = heap.size() - 1;
        heap.set(0, heap.get(lastIdx));
        heap.remove(lastIdx);

        if (!isEmpty()) {
            heapifyDown(0);
        }

        return min;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parentIndex)) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private void heapifyDown(int index) {
        int leftChildIdx;
        int rightChildIdx;
        int smallestChildIdx;

        while (true) {
            leftChildIdx = 2 * index + 1;
            rightChildIdx = 2 * index + 2;
            smallestChildIdx = index;

            if (leftChildIdx < heap.size() && heap.get(leftChildIdx).compareTo(heap.get(smallestChildIdx)) < 0) {
                smallestChildIdx = leftChildIdx;
            }

            if (rightChildIdx < heap.size() && heap.get(rightChildIdx).compareTo(heap.get(smallestChildIdx)) < 0) {
                smallestChildIdx = rightChildIdx;
            }

            if (smallestChildIdx != index) {
                swap(index, smallestChildIdx);
                index = smallestChildIdx;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
