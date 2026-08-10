package gh.edu.ug.cs.ugmaintenance;

import java.io.IOException;
import java.util.Scanner;

import gh.edu.ug.cs.ugmaintenance.algorithms.utils.HashTableBenchmark;
import gh.edu.ug.cs.ugmaintenance.datastructures.hashtable.HashTableDemo;

/**
 * Console menu for the UG Campus Maintenance Service Optimizer (Section 8.iv:
 * the examiner must be able to run demonstrations without editing source).
 *
 * <p>Member demos are registered here as they are completed. Currently
 * available:</p>
 * <ul>
 *   <li>1 - Hash Table / Set / Map demo (member 9, Cheryl A. A. Kwakye)</li>
 *   <li>2 - Hash table efficiency experiment (M10, writes a CSV)</li>
 * </ul>
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("  UG CAMPUS MAINTENANCE SERVICE OPTIMIZER");
        System.out.println("  Group 17 - One Stack | Joint DSA Project");
        System.out.println("==============================================================");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("Main menu:");
            System.out.println("  1. Hash Table / Set / Map demo (member 9)");
            System.out.println("  2. Hash table efficiency experiment (M10)");
            System.out.println("  0. Exit");
            System.out.print("  Choose an option: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> HashTableDemo.runDemo();
                case "2" -> runBenchmark();
                case "0" -> {
                    System.out.println("Goodbye.");
                    running = false;
                }
                default -> System.out.println("  Invalid option \"" + input + "\". Choose 1, 2 or 0.");
            }
        }
        scanner.close();
    }

    private static void runBenchmark() {
        try {
            HashTableBenchmark.runBenchmark();
        } catch (IOException e) {
            System.out.println("  Could not write the experiment CSV: " + e.getMessage());
        }
    }
}
