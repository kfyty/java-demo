package com.kfyty.demo.test;

import com.kfyty.demo.utils.LinkedArrayList;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class LinkedArrayListTest {
    @Test
    public void test1() {
        List<Integer> list = new LinkedArrayList<>(3);
        list.add(1);
        list.add(3);
        list.add(1, 2);
        list.add(0, 0);
        System.out.println(list);
    }

    @Test
    public void test2() {
        List<Integer> list = new LinkedArrayList<>(3);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(2, 5);
        list.add(5, 6);
        System.out.println(list);
    }

    @Test
    public void test3() {
        List<Integer> list = new LinkedArrayList<>(3);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.remove(3);
        list.remove(1);
        System.out.println(list);
    }

    @Test
    public void test4() {
        List<String> list = new LinkedArrayList<>(3);
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.remove("4");
        System.out.println(list.contains("3"));
        System.out.println(list);
    }

    @Test
    public void test5() {
        int count = 1000000;
        Random random = new Random();
        List<Integer> list = new LinkedArrayList<>(5000);
//        List<Integer> list = new ArrayList<>(50000);
//        List<Integer> list = new LinkedList<>();
        long start = System.currentTimeMillis();
        list.add(1);
        for (int i = 0; i < count; i++) {
            list.add(random.nextInt(list.size()), random.nextInt(count));
        }
        for (int i = 0; i < count - 10; i++) {
            list.remove(random.nextInt(list.size()));
        }
        System.out.println(list);
        System.out.println(System.currentTimeMillis() - start + " ms");
    }
}
