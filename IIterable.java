
interface IIterable {
    // Traverse and print each element
    // Can blow up call stack for testing with large iterables or large runs
    IIterable traverse();

    // Only traverse without writing output
    // Used to purely measure traversal and ignore I/O cost
    IIterable suppressedTraverse();

    // Insert an element at a specific index
    IIterable insert(int index, int v);

    IIterable sort();

    // Insert a value in its correct position
    // Iterable will be sorted if it is not sorted already
    IIterable smartInsert(int v);

    int linearSearch(int v);

    int binarySearch(int v);

    // Returns the value of the final element
    // Used as worst case scenario for the search methods
    int finalValue();
}