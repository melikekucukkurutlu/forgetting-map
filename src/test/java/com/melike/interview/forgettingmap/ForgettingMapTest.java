package com.melike.interview.forgettingmap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForgettingMapTest {

    @Test
    @DisplayName("Add a new key to head of bucket when map is not full")
    void addNewKey_toHeadOfIndex_notFullMap() {
        ForgettingMap<Integer, String> testMap = new ForgettingMap<>(5);
        testMap.add(1, "first association");
        testMap.add(2, "second association");

        assertEquals(2, testMap.size());
        assertEquals("first association", testMap.find(1));
        assertEquals("second association", testMap.find(2));
    }

    @Test
    @DisplayName("Add a new key to tail of bucket when map is not full")
    void addNewKey_toTailOfIndex_notFullMap() {
        ForgettingMap<Integer, String> testMap = new ForgettingMap<>(5);
        testMap.add(1, "first association");
        testMap.add(6, "second association");

        assertEquals(2, testMap.size());
        assertEquals("first association", testMap.find(1));
        assertEquals("second association", testMap.find(6));
    }

    @Test
    @DisplayName("Add an existing key to head of bucket when map is not full")
    void addExistingKey_toHeadOfIndex_notFullMap() {
        ForgettingMap<Integer, String> testMap = new ForgettingMap<>(5);
        testMap.add(1, "first association");
        testMap.add(1, "second association");

        assertEquals(1, testMap.size());
        testMap.add(1, "second association");
    }

    @Test
    @DisplayName("Add an existing key to tail of bucket when map is not full")
    void addExistingKey_toTailOfIndex_notFullMap() {
        //initialize map
        ForgettingMap<Integer, String> testMap = new ForgettingMap<>(5);
        testMap.add(1, "first association");
        testMap.add(6, "second association");
        testMap.add(6, "third association"); //add same key

        //adding same key should not change the size
        assertEquals(2, testMap.size());

        assertEquals("first association", testMap.find(1));

        //contents must be updated
        assertEquals("third association", testMap.find(6));
    }

    @Test
    @DisplayName("Add existing keys when map is full")
    void addExistingKey_fullMap() {
        //initialize map
        ForgettingMap<Integer, String> testMap = new ForgettingMap<>(5);
        for(int i=0; i< 5; i++){
            testMap.add(i, i+". association");
        }

        //add same keys with different content
        for(int i=0; i< 5; i++){
            testMap.add(i, i+". new association");
        }

        System.out.println(testMap);

        //adding same key should not change the size
        assertEquals(5, testMap.size());

        //contents must be updated
        for(int i=0; i< testMap.size();i++){
            assertEquals(i+". new association", testMap.find(i));
        }
    }

    @Test
    @DisplayName("Add a new key to head of bucket when map is full")
    void addNewKey_toHeadOfIndex_FullMap() {
        // initialize map
        ForgettingMap<Integer, String> testMap = new ForgettingMap<>(5);
        for(int i=0; i< 5; i++){
            testMap.add(i, i+". association");
        }

        //Keys 1-4 used count will be 2
        for(int i = 1; i < 5; i ++){
            testMap.find(i);
            testMap.find(i);
        }

        //Key 0 used count will be 1
        assertNotNull(testMap.find(0));

        //there is no key 5 yet
        assertNull(testMap.find(5));

        //add key 5
        testMap.add(5,"new association");

        //Key 0 must be deleted because it had the least count
        assertNull(testMap.find(0));

        //Key 5 must be added
        assertNotNull(testMap.find(5));
    }

    @Test
    @DisplayName("Add a new key to tail of bucket when map is full")
    void addNewKey_toTailOfIndex_FullMap() {
        // initialize map
        ForgettingMap<Integer, String> testMap = new ForgettingMap<>(5);
        testMap.add(6, "head association");
        testMap.add(7, "head association");
        testMap.add(8, "head association");
        testMap.add(9, "head association");
        testMap.add(1, "tail association");

        //Keys 6-9 used count will be 2
        for(int i = 6; i <= 9; i ++){
            testMap.find(i);
            testMap.find(i);
        }

        testMap.add(11, "new tail association");

        //key 1 must be deleted
        assertNull(testMap.find(1));

        //key 11 must be added
        assertEquals("new tail association", testMap.find(11));
    }

    @Test
    @DisplayName("Add a new key to tail(except first tail element) of bucket when map is full")
    void addNewKey_toTailOfIndex_FullMap2() {
        // initialize map
        ForgettingMap<Integer, String> testMap = new ForgettingMap<>(5);
        testMap.add(6, "head association of index 1");
        testMap.add(7, "head association of index 2");
        testMap.add(8, "head association of index 3");
        testMap.add(11, "tail association of index 1");
        testMap.add(1, "tail association of index 1");

        //Keys 6,7,8,11 used count will be 2
        for(int i = 6; i <= 11; i ++){
            testMap.find(i);
            testMap.find(i);
        }

        testMap.add(16, "new tail association of bucket 1");

        //key 1 must be deleted
        assertNull(testMap.find(1));

        //key 16 must be added
        assertEquals("new tail association of bucket 1", testMap.find(16));
    }

}