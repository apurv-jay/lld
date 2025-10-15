package com.MultiThreading;


import java.util.concurrent.*;

class Testing{
    String task(){
        String s = "Thread is " + Thread.currentThread().getName();
        return s;
    }
}

public class MultiThreading1 {
    public static void main(String [] args){
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Testing test = new Testing();
//        Callable<String> work = () ->{return test.task();};
//        Future<String> future = executor.submit(work);


       Future<String> future = executor.submit(()->test.task());

        try {
            System.out.println(future.get());
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }
        executor.close();
    }
}
