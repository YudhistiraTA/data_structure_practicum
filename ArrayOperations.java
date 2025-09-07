class ArrayOperations implements IIterable {
    private int[] array;
    private boolean sorted = false;
    private int size;
    private int cap;

    public ArrayOperations(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("Invalid array size");
        this.size = size;
        this.cap = size + 100; // Extra capacity for insertions
        array = new int[cap];
        for (int i = 0; i < size; i++) {
            // Fill with random numbers [0, 100]
            array[i] = (int) (Math.random() * 100);
        }
    }

    public void traverse() {
        for (int i = 0; i < size; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }

    public void suppressedTraverse() {
        for (int i = 0; i < size; i++) {
            // no-op
        }
    }

    private void resize() {
        cap += 100;
        int[] newArray = new int[cap];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    public void insert(int index, int v) {
        if (index < 0 || index > size)
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");
        if (size >= cap)
            resize();
        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = v;
        size++;

        // Due to arbitrary insertion,
        // the array is no longer guaranteed to be sorted
        sorted = false;
    }

    public void delete(int index) {
        if (index < 0 || index >= size)
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");

        array[--size] = 0;

        // Deletion should not change the order of elements
        // No change to this.sorted
    }

    public void smartInsert(int v) {
        if (!sorted)
            sort();
        if (size >= cap)
            resize();
        int i = 0;
        while (i < size && array[i] < v) {
            i++;
        }
        System.arraycopy(array, i, array, i + 1, size - i);
        array[i] = v;
        size++;
        // Array is guaranteed to be sorted
        sorted = true;
    }

    public void sort() {
        // Avoid operation if already sorted
        if (!sorted) {
            java.util.Arrays.sort(array);
            sorted = true;
        }
    }

    public int linearSearch(int v) {
        for (int i = 0; i < size; i++) {
            if (array[i] == v) {
                return i;
            }
        }
        return -1;
    }

    public int binarySearch(int v) {
        if (!sorted)
            sort();
        // Uses java.util.Arrays
        int index = java.util.Arrays.binarySearch(array, v);
        return index >= 0 ? index : -1;
    }

    public int finalValue() {
        return array[size - 1];
    }
}