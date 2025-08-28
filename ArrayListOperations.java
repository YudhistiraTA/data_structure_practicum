
class ArrayListOperations implements IIterable {
    // Uses java.util.ArrayList instead of manual linked list implementation
    private java.util.ArrayList<Integer> list;
    private boolean sorted = false;

    public ArrayListOperations(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("Invalid list size");
        list = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            // Fill with random numbers [0, 100]
            list.add((int) (Math.random() * 100));
        }
    }

    @Override
    public IIterable traverse() {
        for (int v : list) {
            System.out.printf("%d ", v);
        }
        System.out.println();
        return this;
    }

    @Override
    public IIterable suppressedTraverse() {
        for (int _ : list) {
            // no-op
        }
        return this;
    }

    @Override
    public IIterable insert(int index, int v) {
        if (index < 0 || index > list.size())
            throw new IndexOutOfBoundsException("Index out of bounds");
        list.add(index, v);

        // Due to arbitrary insertion,
        // the array is no longer guaranteed to be sorted
        sorted = false;
        return this;
    }

    @Override
    public IIterable smartInsert(int v) {
        if (!sorted) {
            sort();
        }
        int i = 0;
        while (i < list.size() && list.get(i) < v) {
            i++;
        }
        list.add(i, v);

        // List is guaranteed to be sorted
        sorted = true;
        return this;
    }

    @Override
    public IIterable sort() {
        java.util.Collections.sort(list);
        sorted = true;
        return this;
    }

    @Override
    public int linearSearch(int v) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == v) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int binarySearch(int v) {
        int index = java.util.Collections.binarySearch(list, v);
        return index >= 0 ? index : -1;
    }

    @Override
    public int finalValue() {
        return list.get(list.size() - 1);
    }
}