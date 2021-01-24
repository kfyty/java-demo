package com.kfyty.demo.leetcode;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/12/31 17:28
 * @email kfyty725@hotmail.com
 */
public class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}

    public Node(int val) {
        this.val = val;
    }

    public Node(int val, Node left, Node right, Node next) {
        this.val = val;
        this.left = left;
        this.right = right;
        this.next = next;
    }
}
