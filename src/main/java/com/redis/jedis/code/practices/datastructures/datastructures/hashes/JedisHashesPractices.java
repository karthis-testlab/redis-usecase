package com.redis.jedis.code.practices.datastructures.datastructures.hashes;

import redis.clients.jedis.Jedis;

public class JedisHashesPractices {

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        jedis.hset("stu#3", "name", "Fin");
        jedis.hset("stu#3", "age", "15");
        jedis.hset("stu#3", "class", "8");
        System.out.println(jedis.hmget("stu#3", "name"));
    }

}