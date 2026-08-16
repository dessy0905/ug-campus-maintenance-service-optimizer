package gh.edu.ug.cs.ugmaintenance.datastructures.redblacktree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Measures the performance of the custom Red-Black Tree.
 *
 * Results are exported to:
 *
 * performance_redblacktree.csv
 */
public class RedBlackTreePerformance {

    private static final int[] INPUT_SIZES = {
            100,
            500,
            1000,
            5000,
            10000,
            20000
    };

    private static final String OUTPUT_FILE =
            "performance_redblacktree.csv";


    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );

        System.out.println(
                " RED-BLACK TREE PERFORMANCE ANALYSIS"
        );

        System.out.println(
                "=============================================="
        );


        try (
                BufferedWriter writer =
                        new BufferedWriter(
                                new FileWriter(
                                        OUTPUT_FILE
                                )
                        )
        ) {

            writer.write(
                    "InputSize,"
                            + "AvgInsertNsPerKey,"
                            + "AvgSearchNsPerKey,"
                            + "AvgDeleteNsPerKey,"
                            + "TreeHeight"
            );

            writer.newLine();


            for (int inputSize : INPUT_SIZES) {

                runExperiment(
                        inputSize,
                        writer
                );
            }


            System.out.println(
                    "\nPerformance results written to:"
            );

            System.out.println(
                    OUTPUT_FILE
            );


        } catch (IOException exception) {

            System.err.println(
                    "Unable to write performance file."
            );

            exception.printStackTrace();
        }
    }


    private static void runExperiment(
            int inputSize,
            BufferedWriter writer)
            throws IOException {


        RedBlackTree<Integer, Integer> tree =
                new RedBlackTree<>();


        /*
         * Generate unique keys.
         */
        List<Integer> keys =
                new ArrayList<>();

        for (int i = 1;
             i <= inputSize;
             i++) {

            keys.add(i);
        }


        /*
         * Shuffle so that performance testing
         * does not depend on sorted input.
         *
         * A fixed seed makes experiments
         * reproducible.
         */
        Collections.shuffle(
                keys,
                new Random(204L + inputSize)
        );


        // =====================================================
        // INSERT PERFORMANCE
        // =====================================================

        long insertStart =
                System.nanoTime();

        for (Integer key : keys) {

            tree.put(
                    key,
                    key
            );
        }

        long insertEnd =
                System.nanoTime();

        long totalInsertTime =
                insertEnd - insertStart;

        double averageInsertTime =
                (double) totalInsertTime
                        / inputSize;


        // =====================================================
        // SEARCH PERFORMANCE
        // =====================================================

        long searchStart =
                System.nanoTime();

        for (Integer key : keys) {

            tree.get(key);
        }

        long searchEnd =
                System.nanoTime();

        long totalSearchTime =
                searchEnd - searchStart;

        double averageSearchTime =
                (double) totalSearchTime
                        / inputSize;


        /*
         * Record height before deletion
         * removes the nodes.
         */
        int treeHeight =
                tree.height();


        // =====================================================
        // DELETE PERFORMANCE
        // =====================================================

        long deleteStart =
                System.nanoTime();

        for (Integer key : keys) {

            tree.remove(key);
        }

        long deleteEnd =
                System.nanoTime();

        long totalDeleteTime =
                deleteEnd - deleteStart;

        double averageDeleteTime =
                (double) totalDeleteTime
                        / inputSize;


        // =====================================================
        // WRITE CSV
        // =====================================================

        writer.write(
                inputSize
                        + ","
                        + String.format(
                        "%.2f",
                        averageInsertTime
                )
                        + ","
                        + String.format(
                        "%.2f",
                        averageSearchTime
                )
                        + ","
                        + String.format(
                        "%.2f",
                        averageDeleteTime
                )
                        + ","
                        + treeHeight
        );

        writer.newLine();


        // =====================================================
        // CONSOLE OUTPUT
        // =====================================================

        System.out.println(
                "\nInput Size: "
                        + inputSize
        );

        System.out.printf(
                "Average Insert: %.2f ns%n",
                averageInsertTime
        );

        System.out.printf(
                "Average Search: %.2f ns%n",
                averageSearchTime
        );

        System.out.printf(
                "Average Delete: %.2f ns%n",
                averageDeleteTime
        );

        System.out.println(
                "Tree Height: "
                        + treeHeight
        );
    }
}