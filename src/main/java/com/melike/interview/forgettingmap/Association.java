package com.melike.interview.forgettingmap;

import java.util.concurrent.atomic.AtomicInteger;

public class Association<K,V> {

    private final K key;
    private V content;
    private Association next;
    private AtomicInteger usedCount = new AtomicInteger(0);

    public Association(K key, V content) {
        this.key = key;
        this.content = content;
    }

    public K getKey() {
        return key;
    }

    public V getContent() {
        return content;
    }

    public Association next() {
        return next;
    }

    public void next(Association next) {
        this.next = next;
    }

    public void setContent(V content) {
        this.content = content;
    }

    public int getUsedCount() {
        return usedCount.get();
    }

    public int incrementUsedCount(){
        return usedCount.incrementAndGet();
    }

    @Override
    public String toString() {
        Association<K,V> temp = this;
        StringBuilder sb = new StringBuilder();
        while (temp != null){
            sb.append(temp.key + " -> " + temp.content +
                    " hashCode: " + temp.getKey().hashCode() +
                    " usedCount: " + temp.getUsedCount() + " , ");
            temp = temp.next;
        }
        return sb.toString();
    }
}
