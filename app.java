public class app {
    public static void main(String[] args) {
        /**
         * Note that each size will run n times,
         * which means the test time will take some time for large size and runs
         */
        new Comparison(
                new int[] { 1000, 10000, 100000, 1000000 }, // Test sizes
                100) // Number of runs to average the result for each size
                .run()
                .print();
    }
}
