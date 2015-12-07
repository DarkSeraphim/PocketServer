package com.pocketserver.api.util;

import java.util.stream.IntStream;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class PipelineTest {
    @Test
    public void testOrder() {
        Pipeline<String> pipeline = Pipeline.of(2);
        pipeline.addFirst("one").addFirst("two");
        Assert.assertThat("pipeline maintains correct order", pipeline, Matchers.contains("two", "one"));
        Assert.assertThat("pipeline maintains correct order", pipeline, Matchers.hasItems("one", "two"));
    }

    @Test
    public void testRemove() {
        int size = 5;
        Pipeline<Integer> pipeline = Pipeline.of(size);
        IntStream.range(0, size).forEach(pipeline::addFirst);
        Integer[] oddNumbers = IntStream.range(0, size).boxed().filter(i -> i % 2 != 0).toArray(Integer[]::new);
        pipeline.removeIf(i -> i % 2 == 0);
        Assert.assertThat("pipeline should only contains odd numbers", pipeline, Matchers.hasItems(oddNumbers));
    }
}