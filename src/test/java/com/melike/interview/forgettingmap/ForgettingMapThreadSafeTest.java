package com.melike.interview.forgettingmap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class ForgettingMapThreadSafeTest {

    @Test
    @DisplayName("is add method thread safe when map is not full")
    void threadSafe_addMethod_notFullMap() throws InterruptedException {

        int capacity = 10000;
        int size = capacity / 2;
        ExecutorService service = Executors.newFixedThreadPool(size);
        CountDownLatch latch = new CountDownLatch(size);

        ForgettingMap<Integer, String> testMap = new ForgettingMap<>(capacity);

        for (int i = 0; i < size; i++) {
            int index = i;
            service.execute(
                    () -> {
                        waitOneSecond();
                        testMap.add(index, index + ". association");
                        latch.countDown();
                    }
            );
        }
        latch.await();

        assertEquals(testMap.size(), size);
    }


    @Test
    @DisplayName("is add method thread safe when map is full")
    void threadSafe_addMethod_fullMap() throws InterruptedException {
        int capacity = 100;
        //initialize map with full capacity
        ForgettingMap<Integer, Integer> testMap = new ForgettingMap<>(capacity);
        for (int i = 0; i < capacity; i++) {
            testMap.add(i, i);
        }

        ExecutorService service = Executors.newFixedThreadPool(capacity);
        CountDownLatch latch = new CountDownLatch(capacity);

        for (int i = 0; i < capacity; i++) {
            int index = i;
            service.execute(
                    () -> {
                        //add new keys
                        testMap.add(index+capacity, index+capacity);
                        latch.countDown();
                    }
            );
        }
        latch.await();
        //size must not be changed
        assertEquals(testMap.size(), capacity);

    }

    @Test
    @DisplayName("is find method thread safe")
    void threadSafe_findMethod() throws InterruptedException {
        int capacity = 10000;
        //initialize map with full capacity
        ForgettingMap<Integer, Integer> testMap = new ForgettingMap<>(capacity);
        for (int i = 0; i < capacity; i++) {
            testMap.add(i, i);
        }

        ExecutorService service = Executors.newFixedThreadPool(capacity);
        CountDownLatch latch = new CountDownLatch(capacity);

        //initialize array for storing find method results
        Integer[] results = new Integer[capacity];

        for (int i = 0; i < capacity; i++) {
            int index = i;
            service.execute(
                    () -> {
                        //searching existing associations with find methods
                        results[index]=testMap.find(index);
                        latch.countDown();
                    }
            );
        }
        latch.await();

        //all the associations must be founded and stored to results array
        for (int i = 0; i < results.length; i++) {
            assertEquals(i,results[i]);
        }

    }

    /**
     * Wait 1 second.
     */
    private void waitOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}