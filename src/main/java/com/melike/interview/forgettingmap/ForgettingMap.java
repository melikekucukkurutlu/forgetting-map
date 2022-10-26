package com.melike.interview.forgettingmap;

import java.util.concurrent.atomic.AtomicInteger;

public class ForgettingMap<K, V> {
    private final int capacity;
    private AtomicInteger size = new AtomicInteger(0);
    private final Association<K, V>[] table;


    public ForgettingMap(int capacity) {
        this.capacity = capacity;
        this.table = new Association[capacity];
    }

    /**
     * if key is already exists, only content will be updated.
     * if key is not exists and map size is not full, new association will be added.
     * if key is not exists and map size is full, the least used association will be deleted and new association will be added.
     */
    public void add(K key, V content) {

        int index = findIndex(key); //index of the bucket

        if (table[index] != null) {

            Association<K, V> association = table[index];

            //Add an existing key to head of bucket
            //Content will be updated
            if (association.getKey().equals(key)) {
                association.setContent(content);
                return;
            }

            //Add an existing key to tail of bucket
            //Content will be updated
            while (association.next() != null) {
                association = association.next();

                if (association.getKey().equals(key)) {
                    association.setContent(content);
                    return;
                }
            }

            //Add a new key to tail of bucket, not full map
            if (size() < capacity) {
                association.next(new Association(key, content));
                size.incrementAndGet();
                return;
            }

        } else { //table[index] is equal to null

            //Add a new key to head of bucket, not full map
            if (size() < capacity) {
                table[index] = new Association<>(key, content);
                size.incrementAndGet();
                return;
            }
        }
        //if below code is run, it is certain that the capacity is full and new association will be added
        //synchronized is used for locking table when an item will be deleted
        synchronized (table) {
            //find the least used
            K leastUsedKey = findLeastUsed();
            //remove the least used
            remove(leastUsedKey);
            //decrease the size of map
            size.decrementAndGet();
            //add new item
            add(key, content);
        }
    }


    /**
     * When find method is called, used count will be increased for association if exists.
     */
    public V find(K key) {
        int index = findIndex(key); //find index of the bucket
        Association<K, V> association = table[index]; //get head item of the bucket
        //check head item if it is null
        if (association == null) {
            return null;
        }
        //check items of the bucket
        while (association != null) {
            if (association.getKey().equals(key)) {
                //key is founded
                association.incrementUsedCount();
                return association.getContent();
            }
            association = association.next();
        }
        return null;
    }

    /**
     * This method will find the least used association
     */
    private K findLeastUsed() {

        int leastUsedCount = Integer.MAX_VALUE; //init the least used count to max value
        K leastKey = null;

        //check each element in the map to find the least used element
        for (Association<K, V> association : table) {
            if (association != null) {
                if (association.getUsedCount() < leastUsedCount) {
                    leastUsedCount = association.getUsedCount();
                    leastKey = association.getKey();
                }
                while (association.next() != null) {
                    association = association.next();
                    if (association.getUsedCount() < leastUsedCount) {
                        leastUsedCount = association.getUsedCount();
                        leastKey = association.getKey();
                    }
                }
            }
        }
        return leastKey;
    }


    /**
     * If add method is called for adding new association while map capacity is full,
     * remove method will delete the least used association.
     */
    private void remove(K key) {

        int index = findIndex(key); //find index of the bucket
        Association<K, V> a = table[index]; //get head item of the bucket

        //check head item
        if (a.getKey().equals(key)) {
            //delete
            table[index] = a.next();
            a.next(null);
            return;
        }

        //check tail items
        Association<K, V> prev = a;
        a = a.next();
        while (a != null) {
            if (a.getKey().equals(key)) {
                //delete
                prev.next(a.next());
                a.next(null);
                return;
            }
            prev = a;
            a = a.next();
        }
    }


    public int size() {
        return size.get();
    }


    /**
     * find index of the bucket
     */
    private int findIndex(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                sb.append(i + " " + table[i] + "\n");
            } else {
                sb.append(i + " " + "null" + "\n");
            }
        }
        return sb.toString();
    }
}
