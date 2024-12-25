package com.emsi;

public class Main {
    public static void main(String[] args) {
        var serverStartTime = System.nanoTime();

        var delayedService = new DelayedRemoteService(serverStartTime, 5);
        var delayedServiceCircuitBreaker = new DefaultCircuitBreaker(delayedService, 3000, 2,
                2000 * 1000 * 1000);

        var quickService = new QuickRemoteService();
        var quickServiceCircuitBreaker = new DefaultCircuitBreaker(quickService, 3000, 2,
                2000 * 1000 * 1000);

        //Create an object of monitoring service which makes both local and remote calls
        var monitoringService = new MonitoringService(delayedServiceCircuitBreaker,
                quickServiceCircuitBreaker);

        //Fetch response from local resource
        System.out.println(monitoringService.localResourceResponse());

        //Fetch response from delayed service 2 times, to meet the failure threshold
        System.out.println(monitoringService.delayedServiceResponse());
        System.out.println(monitoringService.delayedServiceResponse());

        //Fetch current state of delayed service circuit breaker after crossing failure threshold limit
        //which is OPEN now
        System.out.println(delayedServiceCircuitBreaker.getState());

        //Meanwhile, the delayed service is down, fetch response from the healthy quick service
        System.out.println(monitoringService.quickServiceResponse());
        System.out.println(quickServiceCircuitBreaker.getState());

        //Wait for the delayed service to become responsive
        try {
            System.out.println("Waiting for delayed service to become responsive");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("An error occurred: " + e);
        }
        //Check the state of delayed circuit breaker, should be HALF_OPEN
        System.out.println(delayedServiceCircuitBreaker.getState());

        //Fetch response from delayed service, which should be healthy by now
        System.out.println(monitoringService.delayedServiceResponse());
        //As successful response is fetched, it should be CLOSED again.
        System.out.println(delayedServiceCircuitBreaker.getState());
    }
}