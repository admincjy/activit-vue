package com.wl.wlp2ploansystem.publicsubsystem.entities;

public class KeyValuePair<K,V> {
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    private  K key;

    public KeyValuePair() {
    }
    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    private  V value;
}

