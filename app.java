import java.util.Arrays;

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
    public IIterable traverse() {
        for (int v : array) {
            System.out.printf("%d ", v);
        }
        System.out.println();
        return this;
    }

    @Override
    public IIterable suppressedTraverse() {
        for (int _ : array) {
            // no-op
        }
        return this;
    }

    @Override
    public IIterable insert(int index, int v) {
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
        return this;
    }

    @Override
    public IIterable smartInsert(int v) {
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
        return this;
    }

    @Override
    public IIterable sort() {
        // Avoid operation if already sorted
        if (!sorted) {
            Arrays.sort(array);
            sorted = true;
        }
        return this;
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
        int index = Arrays.binarySearch(array, v);
        return index >= 0 ? index : -1;
    }

    @Override
    public int finalValue() {
        return array[array.length - 1];
    }
}

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

class Comparison {
    private static enum TestType {
        TRAVERSE,
        INSERT,
        SORT,
        SMART_INSERT,
        LINEAR_SEARCH,
        BINARY_SEARCH
    }

    private static enum StructureType {
        ARRAY,
        ARRAY_LIST
    }

    // ANSI escape codes for colored output in print method
    private static class TextColor {
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_GREEN = "\u001B[32m";;
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_RED = "\u001B[31m";
    }

    // 2D array where i is the test size and j is the structure type
    // For example, UnitTest[1][0] could be test of size 10_000 for ArrayOperations
    // and UnitTest[1][1] for ArrayListOperations
    private UnitTest[][] results;

    // Nested class for each run of size and type
    private static class UnitTest {
        private int size;
        private StructureType type; // array or list
        private long[] executionTimes;
        private int runs;

        public UnitTest(int size, int runs, StructureType type) {
            this.size = size;
            this.runs = runs;
            this.type = type;
            executionTimes = new long[TestType.values().length];
        }

        // Helper function to create the appropriate data structure
        // This is why IIterable interface was necessary
        private IIterable createDataStructure(int size, StructureType type) {
            switch (type) {
                case ARRAY:
                    return new ArrayOperations(size);
                case ARRAY_LIST:
                    return new ArrayListOperations(size);
                default:
                    throw new IllegalArgumentException("Invalid structure type");
            }
        }

        public UnitTest run() {
            // Run all tests n number of times
            // to collect reliable average data
            for (int i = 0; i < runs; i++) {
                IIterable data = createDataStructure(size, type); // Isolated instance for each run
                int finalValue = data.finalValue(); // Get final value for search tests
                timeOperation(() -> data.suppressedTraverse(), TestType.TRAVERSE.ordinal());
                timeOperation(() -> data.insert(size / 2, 50), TestType.INSERT.ordinal());
                timeOperation(() -> data.sort(), TestType.SORT.ordinal());
                timeOperation(() -> data.smartInsert(50), TestType.SMART_INSERT.ordinal());
                timeOperation(() -> data.linearSearch(finalValue), TestType.LINEAR_SEARCH.ordinal());
                timeOperation(() -> data.binarySearch(finalValue), TestType.BINARY_SEARCH.ordinal());
            }
            for (int i = 0; i < executionTimes.length; i++) {
                executionTimes[i] /= runs; // Average collected times
            }
            return this;
        }

        // Closure runner
        private void timeOperation(Runnable operation, int index) {
            long start = System.nanoTime();
            operation.run();
            long end = System.nanoTime();
            executionTimes[index] += end - start; // Collect time
        }
    }

    public Comparison(int[] sizes, int runs) {
        results = new UnitTest[sizes.length][StructureType.values().length];
        for (int i = 0; i < sizes.length; i++)
            for (int j = 0; j < StructureType.values().length; j++)
                results[i][j] = new UnitTest(sizes[i], runs, StructureType.values()[j]).run();
    }

    public Comparison run() {
        for (int i = 0; i < results.length; i++)
            for (int j = 0; j < results[i].length; j++)
                results[i][j].run();
        return this;
    }

    public void print() {
        System.out.println("\nTest Results");
        // Get all TestTypes for column headers
        TestType[] testTypes = TestType.values();

        for (int i = 0; i < results.length; i++) {
            int[] fastest = new int[testTypes.length];
            for (int k = 0; k < fastest.length; k++) {
                fastest[k] = 0;
                for (int j = 1; j < results[i].length; j++) {
                    if (results[i][j].executionTimes[k] < results[i][fastest[k]].executionTimes[k]) {
                        fastest[k] = j;
                    }
                }
            }
            // Print table header
            String title = String.format("Size: %d", results[i][0].size);
            System.out.printf("  %s%-15s%s", TextColor.ANSI_CYAN, title,
                    TextColor.ANSI_YELLOW);
            for (TestType type : testTypes) {
                System.out.printf("%-15s", type);
            }
            System.out.println(TextColor.ANSI_RESET);

            // Print data rows
            for (int j = 0; j < results[i].length; j++) {
                System.out.printf("  %s%-15s%s", TextColor.ANSI_YELLOW, results[i][j].type, TextColor.ANSI_RESET);
                for (int k = 0; k < testTypes.length; k++) {
                    System.out.printf("%s%-15d%s", fastest[k] == j ? TextColor.ANSI_GREEN : TextColor.ANSI_RESET,
                            results[i][j].executionTimes[k], TextColor.ANSI_RESET);
                }
                System.out.println();
            }
            // Print delta between longest and shortest data type
            System.out.printf("  %s%-15s%s", TextColor.ANSI_YELLOW, "DELTA", TextColor.ANSI_RESET);
            for (int k = 0; k < testTypes.length; k++) {
                long shortestTime = results[i][0].executionTimes[k];
                long longestTime = results[i][0].executionTimes[k];
                for (int j = 1; j < results[i].length; j++) {
                    shortestTime = Math.min(shortestTime, results[i][j].executionTimes[k]);
                    longestTime = Math.max(longestTime, results[i][j].executionTimes[k]);
                }
                System.out.printf("%s%-15d%s",
                        shortestTime < longestTime / 2 ? TextColor.ANSI_RED : TextColor.ANSI_RESET,
                        Math.abs(longestTime - shortestTime), TextColor.ANSI_RESET);
            }
            System.out.println();
            System.out.println(); // Add spacing between different sizes
        }
    }
}

public class app {
    public static void main(String[] args) {
        new Comparison(
                new int[] { 1000, 10000, 100000, 1000000 }, // Test sizes
                100) // Number of runs to average the result for each size
                .run()
                .print();
        /**
         * Note that each size will run n times,
         * which means the test time will take some time for large size and runs
         */
    }
}
