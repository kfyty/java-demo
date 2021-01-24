package com.kfyty.demo.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class LinkedArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Serializable {

    private static final int MIN_CAPACITY = 2;

    private final int capacity;

    private transient int size;

    private transient LinkedArrayNode root;

    public LinkedArrayList() {
        this(MIN_CAPACITY);
    }

    public LinkedArrayList(int capacity) {
        super();
        this.size = 0;
        this.capacity = Math.max(capacity, MIN_CAPACITY);
        this.root = new LinkedArrayNode();
    }

    public LinkedArrayList(Collection<E> collections) {
        this();
        this.addAll(collections);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iter();
    }

    @Override
    public boolean add(E e) {
        this.add(size(), e);
        return true;
    }

    @Override
    public E get(int index) {
        return this.root.get(index);
    }

    @Override
    public E set(int index, E element) {
        return this.root.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        this.root.add(index, element);
        this.size++;
    }

    @Override
    public E remove(int index) {
        E remove = this.root.remove(index);
        size--;
        return remove;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.size = s.readInt();
        this.root = new LinkedArrayNode(size);
        for (int i = 0; i < size; i++) {
            this.root.element[i] = s.readObject();
        }
        this.root.nodeSize = this.size;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);
        LinkedArrayNode node = root;
        while(node != null) {
            for (int i = 0; i < node.nodeSize; i++) {
                s.writeObject(node.element[i]);
            }
            node = node.next;
        }
    }

    private class Iter implements Iterator<E> {
        int cursor;
        int curSize;
        int curNodeCursor;
        LinkedArrayNode curNode;

        Iter() {
            this.curSize = LinkedArrayList.this.size;
            this.curNode = LinkedArrayList.this.root;
        }

        @Override
        public boolean hasNext() {
            return cursor < LinkedArrayList.this.size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            if(curSize != LinkedArrayList.this.size) {
                throw new ConcurrentModificationException();
            }
            while(curNode != null && curNodeCursor >= curNode.nodeSize) {
                curNodeCursor = 0;
                curNode = curNode.next;
            }
            cursor++;
            return curNode == null ? null : (E) curNode.element[curNodeCursor++];
        }

        @Override
        public void remove() {
            this.cursor--;
            curNode.remove(--curNodeCursor);
            this.curSize--;
            LinkedArrayList.this.size--;
        }
    }

    private class LinkedArrayNode {
        Object[] element;
        int nodeSize;
        LinkedArrayNode next;

        LinkedArrayNode() {
            this(capacity);
        }

        LinkedArrayNode(int capacity) {
            this.nodeSize = 0;
            this.element = new Object[capacity];
        }

        @SuppressWarnings("unchecked")
        protected E get(int index) {
            this.checkRange(index);
            if(index >= nodeSize) {
                return this.next == null ? null : this.next.get(index - nodeSize);
            }
            return (E) this.element[index];
        }

        @SuppressWarnings("unchecked")
        protected E set(int index, E e) {
            this.checkRange(index);
            if(index >= nodeSize) {
                return this.next.set(index - nodeSize, e);
            }
            E result = (E) element[index];
            element[index] = e;
            return result;
        }

        @SuppressWarnings("unchecked")
        protected E remove(int index) {
            this.checkRange(index);
            if(index >= nodeSize) {
                return this.next == null ? null : this.next.remove(index - nodeSize);
            }
            E result = (E) element[index];
            if(index < nodeSize - 1) {
                System.arraycopy(element, index + 1, element, index, nodeSize - 1 - index);
            }
            element[--nodeSize] = null;
            return result;
        }

        protected void add(int index, E e) {
            this.checkRange(index);
            if(index >= nodeSize) {
                this.add2Next(index, e);
                return;
            }
            if(this.nodeSize >= capacity) {
                this.newNextIfNecessary();
            }
            if(this.nodeSize > 0 && index < this.nodeSize && index < capacity - 1) {
                int tempSize = this.nodeSize == capacity ? this.nodeSize - 1 : this.nodeSize;
                System.arraycopy(element, index, element, index + 1, tempSize - index);
            }
            this.element[index] = e;
            if(this.nodeSize < capacity) {
                this.nodeSize++;
            }
        }

        private void checkRange(int index) {
            if(index < 0 || index > size()) {
                throw new ArrayIndexOutOfBoundsException("Size: " + size() + ", index: " + index);
            }
        }

        private void newNextIfNecessary() {
            if(this.next == null) {
                this.next = new LinkedArrayNode();
            } else if(next.nodeSize < capacity) {
                System.arraycopy(next.element, 0, next.element, 1, next.nodeSize);
            } else {
                LinkedArrayNode temp = new LinkedArrayNode();
                temp.next = this.next;
                this.next = temp;
            }
            this.next.element[0] = this.element[nodeSize - 1];
            this.next.nodeSize++;
        }

        private void add2Next(int index, E e) {
            if(this.nodeSize < capacity && index < capacity) {
                this.shiftLeft(index, e);
                return;
            }
            if(this.next == null) {
                this.next = new LinkedArrayNode();
            }
            this.next.add(index - nodeSize, e);
        }

        private void shiftLeft(int index, E e) {
            if(this.next == null) {
                this.element[nodeSize++] = e;
                return;
            }
            int move = index - nodeSize;
            if(next.nodeSize < move) {
                this.next.add(index - nodeSize, e);
                return;
            }
            this.doShiftLeft(move);
            this.element[nodeSize++] = e;
        }

        private void doShiftLeft(int move) {
            System.arraycopy(next.element, 0, this.element, this.nodeSize, move);
            this.nodeSize += move;
            if(next.nodeSize <= move) {
                Arrays.fill(next.element, 0, next.nodeSize, null);
                this.next = this.next.next;
                return;
            }
            System.arraycopy(next.element, move, next.element, 0, next.nodeSize - move);
            Arrays.fill(next.element, next.nodeSize - move, next.nodeSize, null);
            next.nodeSize -= move;
        }
    }
}
