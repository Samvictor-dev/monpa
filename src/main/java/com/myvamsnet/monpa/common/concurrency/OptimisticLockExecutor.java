package com.myvamsnet.monpa.common.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class OptimisticLockExecutor {

    private static final int MAX_RETRIES = 3;

    public <T> T execute(Supplier<T> action) {

        int attempt = 1;

        while (true) {

            try {

                return action.get();

            } catch (ObjectOptimisticLockingFailureException ex) {

                log.warn(
                        "Optimistic locking conflict. Attempt {}/{}",
                        attempt,
                        MAX_RETRIES
                );

                if (attempt >= MAX_RETRIES) {

                    log.error(
                            "Optimistic locking failed after {} attempts.",
                            MAX_RETRIES
                    );

                    throw ex;
                }

                attempt++;

                try {
                    long delay = 50L * (1L << (attempt - 1));

                    Thread.sleep(delay);

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                    throw new IllegalStateException(
                            "Retry interrupted.",
                            e
                    );
                }

            }

        }

    }

}
