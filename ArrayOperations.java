class ArrayOperations implements IIterable {
    private int[] array;
    private boolean sorted = false;

    public ArrayOperations(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("Invalid array size");
        array = new int[size];
        for (int i = 0; i < size; i++) {
            // Fill with random numbers [0, 100]
            array[i] = (int) (Math.random() * 100);
        }
    }

    @Override
    public void traverse() {
        for (int v : array) {
            System.out.printf("%d ", v);
        }
        System.out.println();
    }

    @Override
    public void suppressedTraverse() {
        for (int _ : array) {
            // no-op
        }
    }

    @Override
    public void insert(int index, int v) {
        if (index < 0 || index > array.length)
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");

        // Clone array with increased size
        int[] newArray = new int[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, index);
        // Stop clone at index and insert value
        newArray[index] = v;
        // Clone the rest of the array
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        array = newArray;

        // Due to arbitrary insertion,
        // the array is no longer guaranteed to be sorted
        sorted = false;
    }

    @Override
    public void delete(int index) {
        if (index < 0 || index >= array.length)
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");

        // Clone array with decreased size
        int[] newArray = new int[array.length - 1];
        System.arraycopy(array, 0, newArray, 0, index);
        // Skip the deleted index
        System.arraycopy(array, index + 1, newArray, index, array.length - index - 1);
        array = newArray;

        // Deletion should not change the order of elements
        // No change to this.sorted
    }

    @Override
    public void smartInsert(int v) {
        if (!sorted)
            sort();
        int[] newArray = new int[array.length + 1];
        int i = 0;
        // Manual iteration to both copy and compare values
        // Can not use arraycopy because we need to find the correct position for
        // insertion
        while (i < array.length && array[i] < v) {
            newArray[i] = array[i];
            i++;
        }
        newArray[i] = v;
        // Use arraycopy for the rest of the array
        System.arraycopy(array, i, newArray, i + 1, array.length - i);
        array = newArray;

        // Array is guaranteed to be sorted
        sorted = true;
    }

    @Override
    public void sort() {
        // Avoid operation if already sorted
        if (!sorted) {
            java.util.Arrays.sort(array);
            sorted = true;
        }
    }

    @Override
    public int linearSearch(int v) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == v) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int binarySearch(int v) {
        // Uses java.util.Arrays
        int index = java.util.Arrays.binarySearch(array, v);
        return index >= 0 ? index : -1;
    }

    @Override
    public int finalValue() {
        return array[array.length - 1];
    }
}