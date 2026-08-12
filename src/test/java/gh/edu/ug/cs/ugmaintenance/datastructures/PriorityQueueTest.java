package gh.edu.ug.cs.ugmaintenance.datastructures;

import gh.edu.ug.cs.ugmaintenance.datastructures.queue.PriorityQueue;

public class PriorityQueueTest {

    public static void main(String[] args) {

        System.out.println("=== UG Maintenance Optimizer ===");
        System.out.println("=== Priority Queue Test ===");
        System.out.println();

        testOffer();
        testPeek();
        testPoll();
        testPriorityOrder();
        testSize();
        testEmptyQueue();

        System.out.println();
        System.out.println("All Priority Queue tests passed!");
    }

    private static void testOffer() {

        System.out.println("--- Test 1: Offer ---");

        PriorityQueue<Integer> queue =
                new PriorityQueue<>();

        queue.offer(3);
        queue.offer(5);
        queue.offer(1);
        queue.offer(4);
        queue.offer(2);

        System.out.println("Elements added successfully.");
        System.out.println("Queue size: " + queue.size());

        if (queue.size() != 5) {
            throw new RuntimeException(
                    "Offer test failed."
            );
        }

        System.out.println("Offer test passed.");
        System.out.println();
    }

    private static void testPeek() {

        System.out.println("--- Test 2: Peek ---");

        PriorityQueue<Integer> queue =
                new PriorityQueue<>();

        queue.offer(3);
        queue.offer(5);
        queue.offer(1);
        queue.offer(4);

        int highest = queue.peek();

        System.out.println(
                "Highest priority element: " + highest
        );

        if (highest != 5) {
            throw new RuntimeException(
                    "Peek test failed. Expected 5."
            );
        }

        // Peek should NOT remove the element
        if (queue.size() != 4) {
            throw new RuntimeException(
                    "Peek should not remove an element."
            );
        }

        System.out.println("Peek test passed.");
        System.out.println();
    }

    private static void testPoll() {

        System.out.println("--- Test 3: Poll ---");

        PriorityQueue<Integer> queue =
                new PriorityQueue<>();

        queue.offer(3);
        queue.offer(5);
        queue.offer(1);
        queue.offer(4);
        queue.offer(2);

        System.out.println(
                "Polling elements in priority order:"
        );

        while (!queue.isEmpty()) {

            System.out.println(
                    "Polled: " + queue.poll()
            );
        }

        System.out.println("Poll test passed.");
        System.out.println();
    }

    private static void testPriorityOrder() {

        System.out.println("--- Test 4: Priority Order ---");

        PriorityQueue<Integer> queue =
                new PriorityQueue<>();

        queue.offer(3);
        queue.offer(5);
        queue.offer(1);
        queue.offer(4);
        queue.offer(2);

        int[] expected = {
                5, 4, 3, 2, 1
        };

        for (int expectedValue : expected) {

            int actual = queue.poll();

            System.out.println(
                    "Expected: " + expectedValue
                            + " | Actual: " + actual
            );

            if (actual != expectedValue) {
                throw new RuntimeException(
                        "Priority order test failed."
                );
            }
        }

        System.out.println(
                "Priority order test passed."
        );

        System.out.println();
    }

    private static void testSize() {

        System.out.println("--- Test 5: Size ---");

        PriorityQueue<Integer> queue =
                new PriorityQueue<>();

        if (queue.size() != 0) {
            throw new RuntimeException(
                    "Initial size should be 0."
            );
        }

        queue.offer(5);
        queue.offer(3);

        if (queue.size() != 2) {
            throw new RuntimeException(
                    "Size should be 2."
            );
        }

        queue.poll();

        if (queue.size() != 1) {
            throw new RuntimeException(
                    "Size should be 1 after poll."
            );
        }

        System.out.println("Size test passed.");
        System.out.println();
    }

    private static void testEmptyQueue() {

        System.out.println("--- Test 6: Empty Queue ---");

        PriorityQueue<Integer> queue =
                new PriorityQueue<>();

        if (!queue.isEmpty()) {
            throw new RuntimeException(
                    "Queue should initially be empty."
            );
        }

        System.out.println(
                "isEmpty(): " + queue.isEmpty()
        );

        System.out.println("Empty queue test passed.");
        System.out.println();
    }
}