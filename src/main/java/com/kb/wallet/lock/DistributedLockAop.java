package com.kb.wallet.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

  private static final String REDISSON_LOCK_PREFIX = "LOCK: ";
  private static final long RETRY_DELAY = 2L;
  private static final long MAX_RETRY_COUNT = 5L;

  private final RedissonClient redissonClient;
  private final AopForTransaction aopForTransaction;

  @Around("@annotation(com.kb.wallet.lock.DistributedLock)")
  public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = joinPoint.getTarget()
        .getClass()
        .getMethod(signature.getMethod().getName(), signature.getMethod().getParameterTypes());

    DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

    String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
    RLock rLock = redissonClient.getLock(key);

    boolean locked = false;
    try {
      locked = rLock.tryLock(0, distributedLock.leaseTime(), distributedLock.timeUnit());
      if (!locked) {
        log.info("Lock acquisition failed: {}", key);
        throw new IllegalStateException("Duplicate booking request");
      }

      return aopForTransaction.proceed(joinPoint);

    } finally {
      if (locked && rLock.isHeldByCurrentThread()) {
        rLock.unlock();
      }
    }
  }
}

/*
package com.kb.wallet.lock;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.aspectj.lang.ProceedingJoinPoint;
    import org.aspectj.lang.annotation.Around;
    import org.aspectj.lang.annotation.Aspect;
    import org.aspectj.lang.reflect.MethodSignature;
    import org.redisson.api.RLock;
    import org.redisson.api.RedissonClient;
    import org.springframework.stereotype.Component;
    import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

  private static final String REDISSON_LOCK_PREFIX = "LOCK: ";
  private static final long RETRY_DELAY = 2L;
  private static final long MAX_RETRY_COUNT = 5L;

  private final RedissonClient redissonClient;
  private final AopForTransaction aopForTransaction;

  @Around("@annotation(com.kb.wallet.lock.DistributedLock)")
  public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = joinPoint.getTarget()
        .getClass()
        .getMethod(signature.getMethod().getName(), signature.getMethod().getParameterTypes());

    DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

    String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
        signature.getParameterNames(),
        joinPoint.getArgs(),
        distributedLock.key());

    RLock rLock = redissonClient.getLock(key);
    int retryCount = 0;

    while (retryCount < MAX_RETRY_COUNT) {
      try {
        boolean available = rLock.tryLock(
            distributedLock.waitTime(),
            distributedLock.leaseTime(),
            distributedLock.timeUnit());

        if (available) {
          log.info("🔒 Lock acquired: {}", key);
          try {
            return aopForTransaction.proceed(joinPoint);
          } finally {
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
              log.info("🔓 Lock released: {}", key);
              rLock.unlock();
            }
          }
        } else {
          log.warn("⚠️ Lock acquisition failed, retrying... ({}/{})", retryCount + 1, MAX_RETRY_COUNT);
          Thread.sleep(RETRY_DELAY * 100);
          retryCount++;
        }
      } catch (InterruptedException e) {
        log.error("❌ DistributedLock interrupted");
        throw new InterruptedException(e.getMessage());
      }
    }
    throw new IllegalStateException("❌ DistributedLock lock failed after retries");
  }
}*/
