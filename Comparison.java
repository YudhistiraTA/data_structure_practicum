
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