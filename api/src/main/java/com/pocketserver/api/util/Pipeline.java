package com.pocketserver.api.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Queues;

import java.util.Deque;
import java.util.Iterator;
import java.util.Spliterator;

public final class Pipeline<T> implements Iterable<T> {
    private final Deque<T> deque;

    Pipeline(int size) {
        this.deque = Queues.newLinkedBlockingDeque(size);
    }

    public static <V> Pipeline<V> of() {
        return Pipeline.of(128);
    }

    public static <V> Pipeline<V> of(int size) {
        Preconditions.checkArgument(size > 0, "size should be greater than zero");
        return new Pipeline<>(size);
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

    @Override
    public Iterator<T> iterator() {
        return deque.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return deque.spliterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Pipeline.class.getSimpleName()).append("[");
        for (Iterator<T> iterator = iterator(); iterator.hasNext(); ) {
            builder.append(iterator.next().toString());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.append("]").toString();
    }

    private void checkNotNull(T elem) {
        Preconditions.checkNotNull(elem, "elem must not be null");
    }
}
