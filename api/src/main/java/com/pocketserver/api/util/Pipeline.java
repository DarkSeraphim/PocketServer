package com.pocketserver.api.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Queues;

import java.util.Deque;
import java.util.Iterator;
import java.util.Spliterator;

public final class Pipeline<T> implements Iterable<T> {
    private final Deque<T> deque;

    public Pipeline() {
        this.deque = Queues.newLinkedBlockingDeque();
    }

    public Pipeline<T> addFirst(T elem) {
        checkNotNull(elem);
        deque.addFirst(elem);
        return this;
    }

    public Pipeline<T> addLast(T elem) {
        checkNotNull(elem);
        deque.addLast(elem);
        return this;
    }

    public Pipeline<T> add(T elem) {
        checkNotNull(elem);
        return addLast(elem);
    }

    public Pipeline<T> remove(T elem) {
        checkNotNull(elem);
        for (Iterator<T> elements = deque.descendingIterator(); elements.hasNext(); ) {
            if (elements.next() == elem) {
                elements.remove();
            }
        }
        return this;
    }

    public Pipeline<T> removeIf(Predicate<T> predicate) {
        Preconditions.checkNotNull(predicate, "predicate must not be null");
        for (Iterator<T> elements = deque.descendingIterator(); elements.hasNext(); ) {
            if (predicate.apply(elements.next())) {
                elements.remove();
            }
        }
        return this;
    }

    public Iterator<T> iterator() {
        return deque.iterator();
    }

    public Spliterator<T> spliterator() {
        return deque.spliterator();
    }

    private void checkNotNull(T elem) {
        Preconditions.checkNotNull(elem, "elem must not be null");
    }
}
