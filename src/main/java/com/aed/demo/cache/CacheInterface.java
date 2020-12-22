package com.aed.demo.cache;



public interface CacheInterface {

    public Object getEntry(String key);
    public void putEntry(String name, int attempts);
   
}
