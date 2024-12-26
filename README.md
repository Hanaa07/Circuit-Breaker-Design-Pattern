# Circuit Breaker Pattern

## Overview

The **Circuit Breaker** pattern is a design pattern used to prevent an application from repeatedly performing operations that are likely to fail. It monitors calls to a service and temporarily "opens" the circuit when the number of failures exceeds a threshold. The circuit will then either stay open or attempt recovery based on the configuration. This system handles retries and provides fallbacks to ensure the application can still operate while the underlying service is failing.

![image](https://github.com/user-attachments/assets/3e15fdc2-ca32-4262-8d2c-a565b0449a4c)

---

## Key Classes and Components

### 1. **App**

- **Attributes**:
  - `LOGGER`: A static `Logger` instance used for logging.

- **Methods**:
  - `App()`: Constructor for the `App` class.
  - `main(args: String[])`: The entry point of the application. This method is static and initiates the circuit breaker system.

### 2. **CircuitBreaker (Interface)**

Defines the core methods for any circuit breaker implementation:
- `attemptRequest()`: Attempts to perform a request and returns the result.
- `getState()`: Returns the current state of the circuit breaker (e.g., OPEN, CLOSED, HALF_OPEN).
- `recordFailure(response: String)`: Records a failure response.
- `recordSuccess()`: Records a successful request.
- `setState(state: State)`: Sets the state of the circuit breaker.

### 3. **DefaultCircuitBreaker**

A default implementation of the **CircuitBreaker** interface. It monitors failures and transitions between states based on thresholds and time periods.

- **Attributes**:
  - `failureCount`: The number of consecutive failures.
  - `failureThreshold`: The threshold for failure count to trigger state changes.
  - `futureTime`: The time after which the state can be retried.
  - `lastFailureResponse`: The response of the last failure.
  - `lastFailureTime`: The timestamp of the last failure.
  - `retryTimePeriod`: The time period between retry attempts.
  - `service`: The remote service being monitored by the circuit breaker.
  - `state`: The current state of the circuit breaker.
  - `timeout`: The timeout period for the request.

- **Methods**:
  - `attemptRequest()`: Attempts to call the service and returns the result. It also manages state transitions based on the number of failures.
  - `evaluateState()`: Evaluates the current state based on failure count and retry time.
  - `getState()`: Returns the current state of the circuit breaker.
  - `recordFailure(response: String)`: Records a failure and adjusts the failure count.
  - `recordSuccess()`: Records a successful request and resets the failure count.
  - `setState(state: State)`: Changes the state of the circuit breaker.

### 4. **DelayedRemoteService**

A service that introduces a delay in the response. It simulates a service that is slow to respond.

- **Attributes**:
  - `delay`: The amount of time to delay the response.
  - `serverStartTime`: The start time of the server (used to simulate delays).

- **Methods**:
  - `DelayedRemoteService()`: Constructor that initializes the service.
  - `DelayedRemoteService(serverStartTime: long, delay: int)`: Constructor with specific server start time and delay.
  - `call()`: Simulates a remote service call that takes a delayed response.

### 5. **MonitoringService**

This class monitors multiple services, such as a delayed remote service and a quick remote service. It uses circuit breakers to manage these services.

- **Attributes**:
  - `delayedService`: A reference to the delayed service wrapped in a circuit breaker.
  - `quickService`: A reference to the quick service wrapped in a circuit breaker.

- **Methods**:
  - `MonitoringService(delayedService: CircuitBreaker, quickService: CircuitBreaker)`: Constructor to initialize both the delayed and quick services.
  - `delayedServiceResponse()`: Calls the delayed service and handles the response using the circuit breaker.
  - `localResourceResponse()`: Returns the response from a local resource.
  - `quickServiceResponse()`: Calls the quick service and handles the response using the circuit breaker.

### 6. **QuickRemoteService**

A fast remote service implementation.

- **Methods**:
  - `QuickRemoteService()`: Constructor for the quick remote service.
  - `call()`: Simulates a fast response from a remote service.

### 7. **RemoteService (Interface)**

Defines the contract for any remote service.

- **Methods**:
  - `call()`: An abstract method that any remote service must implement, which performs the actual service call.

### 8. **State (Enum)**

Defines the states of the circuit breaker.

- **Values**:
  - `CLOSED`: The circuit is closed, and requests are allowed.
  - `HALF_OPEN`: The circuit is in a recovery state, and requests can be attempted.
  - `OPEN`: The circuit is open, and requests are blocked to prevent further failures.

- **Methods**:
  - `valueOf(name: String)`: Returns the corresponding `State` enum based on the string name.
  - `values()`: Returns all possible states of the circuit breaker.

---

## Relationships

- **DefaultCircuitBreaker** implements the **CircuitBreaker** interface.
- **DefaultCircuitBreaker** uses a **RemoteService** (either **DelayedRemoteService** or **QuickRemoteService**) to manage service calls.
- **MonitoringService** aggregates circuit breakers for both delayed and quick services to monitor and manage their states.
- **State** is used to represent the current state of the circuit breaker (e.g., OPEN, CLOSED, HALF_OPEN).

---

## Benefits of the Circuit Breaker Pattern

1. **Prevents System Overload**:
   - By monitoring failure rates and temporarily halting requests to failing services, the circuit breaker prevents cascading failures.

2. **Graceful Recovery**:
   - The system can recover gracefully by retrying failed services after a certain period, reducing downtime.

3. **Improved Resilience**:
   - The circuit breaker pattern improves the resilience of distributed systems by allowing the system to fail fast and fail safely, avoiding the "retry storm" problem.

---

## Conclusion

The Circuit Breaker pattern is essential for building resilient systems that depend on external services. By monitoring service health and preventing repeated failures, this pattern helps in ensuring system stability and availability. The implementation in this design leverages state transitions (OPEN, CLOSED, HALF_OPEN) and keeps track of failure counts to determine when to allow retries or fallback.
