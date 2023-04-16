package com.redis.jedis.code.practices.datastructures.usecases;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class RedisManager {

    private Jedis jedis;

    public RedisManager(String hostName, int portNumber) {

        jedis = new Jedis(hostName, portNumber);
    }

    public RedisManager() {
        this("localhost", 6379);
    }

    public void pushToTable(String tableName, String key) {
        synchronized (tableName) {
            jedis.hset(tableName, key, "");
        }
    }

    public String popFromTable(String tableName) {
        String firstKey;
        synchronized (tableName) {
            Set<String> keys = jedis.hkeys(tableName);
            firstKey = keys.iterator().next();
            jedis.hdel(tableName, firstKey);
        }
        return firstKey;
    }

    public String getRandomRecord(String tableName) {
        Set<String> keys = jedis.hkeys(tableName);
        Random random = new Random();
        int index = random.nextInt(0, keys.size() - 1);
        ArrayList<String> list = new ArrayList<String>(keys);
        return list.get(index);
    }

    public int getCountOfRecords(String tableName) {
        return jedis.hgetAll(tableName).size();
    }

    public void close() {
        jedis.close();
    }

}