package com.chen.base.bloomfilter;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @author chenkun
 * @Description
 * @date 2022/4/1 下午4:51
 */
public class Client {
    //后面hash函数会用到，用来生成不同的hash值，可以随便给，但别给奇数
    private final int[] ints = {6, 8, 16, 38, 58, 68};
    private Integer currentBeanCount = 0;
    //你的布隆过滤器容量
    private int DEFAULT_SIZE = Integer.MAX_VALUE;
    //bit数组，用来存放结果
    private final BitSet bitSet = new BitSet(DEFAULT_SIZE);

    public Client() {
    }

    public Client(int size) {
        if (size <= (2 << 8)) {
            throw new RuntimeException("size is too small");
        }
        DEFAULT_SIZE = size;
    }

    //获取当前过滤器的对象数量
    public Integer getCurrentBeanCount() {
        return currentBeanCount;
    }

    //计算出key的hash值，并将对应下标置为true
    public void push(Object key) {
        Arrays.stream(ints).forEach(i -> bitSet.set(hash(key, i)));
        currentBeanCount++;
    }

    //判断key是否存在，true不一定说明key存在，但是false一定说明不存在
    public boolean contain(Object key) {
        boolean result = true;
        for (int i : ints) {
            result = result && bitSet.get(hash(key, i));
        }
        return result;
    }

    //hash算法，借鉴了hashmap的算法
    private int hash(Object key, int i) {
        int h;
        int index = key == null ? 0 : (DEFAULT_SIZE - 1 - i) & ((h = key.hashCode()) ^ (h >>> 16));
        return index > 0 ? index : -index;
    }

    public static void main(String[] args) {
        Client bf = new Client();
        bf.push("张学友");
        bf.push("郭德纲");
        bf.push("蔡徐鸡");
        bf.push(666);
        System.out.println(bf.contain("张学友"));//true
        System.out.println(bf.contain("张学友 "));//false
        System.out.println(bf.contain("张学友1"));//false
        System.out.println(bf.contain("郭德纲"));//true
        System.out.println(bf.contain("蔡徐老母鸡"));//false
        System.out.println(bf.contain(666));//true
        System.out.println(bf.contain(888));//false
    }

}
