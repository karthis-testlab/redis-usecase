package com.redis.jedis.code.practices.datastructures.datastructures.lists;

import redis.clients.jedis.Jedis;

import java.util.List;

public class JedisListPractices {

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        System.out.println(jedis.lpush("queue#tasks", "firstTask"));
        jedis.lpush("queue#tasks", "secondTask");
        String task = jedis.rpop("queue#tasks");
        //List<String> tasks = jedis.lrange("queue#tasks", 0, 10);
        System.out.println(task);
    }

}