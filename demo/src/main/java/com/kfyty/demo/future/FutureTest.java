package com.kfyty.demo.future;

import com.kfyty.demo.future.service.AnyService;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class FutureTest {
    private final AnyService service = new AnyService();

    @Test
    public void testSupplyAsync() throws Exception {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> service.anyMethod("supply async"));
        System.out.println(completableFuture.get());
    }

    @Test
    public void testAsyncResult() throws Exception {
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> service.anyMethod("supply async1"));
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> service.anyMethod("supply async2"));
        long l = System.currentTimeMillis();
        CompletableFuture.allOf(cf1, cf2).join();
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(cf1.get());
        System.out.println(cf2.get());
//        executorService.shutdown();
    }

    @Test
    public void testExceptionally() throws Exception {
        CompletableFuture<String> completableFuture = CompletableFuture
                                                        .supplyAsync(() -> service.anyExceptionMethod("exception"))
                                                        .exceptionally(Throwable::getMessage);
        System.out.println(completableFuture.get());
    }

    @Test
    public void testWhenCompleteAsync() throws Exception {
        CompletableFuture<String> completableFuture = CompletableFuture
                                                        .supplyAsync(() -> service.anyMethod("when complete"))
                                                        .whenCompleteAsync((s, e) -> System.out.println("completed: " + s + " e:" + e));
        System.out.println(completableFuture.get());
    }

    @Test
    public void testThenApply() throws Exception {
        CompletableFuture<String> completableFuture = CompletableFuture
                                                        .supplyAsync(() -> service.anyMethod("then apply"))
                                                        .thenApplyAsync(result -> result + " success !");
        System.out.println(completableFuture.get());
    }
}
