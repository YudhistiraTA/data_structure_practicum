
interface IIterable {
    // Traverse and print each element
    // Can blow up call stack for testing with large iterables or large runs
    void traverse();

    // Only traverse without writing output
    // Used to purely measure traversal and ignore I/O cost
    void suppressedTraverse();

    // Insert an element at a specific index
    void insert(int index, int v);

    void delete(int index);

    void sort();

    // Insert a value in its correct position
    // Iterable will be sorted if it is not sorted already
    void smartInsert(int v);

    int linearSearch(int v);

    int binarySearch(int v);

    // Returns the value of the final element
    // Used as worst case scenario for the search methods
    int finalValue();
}