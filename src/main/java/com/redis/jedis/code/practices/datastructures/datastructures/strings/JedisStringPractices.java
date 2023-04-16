package com.redis.jedis.code.practices.datastructures.datastructures.strings;

import redis.clients.jedis.Jedis;

public class JedisStringPractices {

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        jedis.set("events/city/rome", "32,15,223,828");
        //jedis.set("events/city/budapest", "32,15,223,829");
        String cachedResponse = jedis.get("events/city/budapest");
        System.out.println(cachedResponse);
    }

}