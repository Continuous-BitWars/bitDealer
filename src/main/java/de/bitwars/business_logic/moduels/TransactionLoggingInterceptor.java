package de.bitwars.business_logic.moduels;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor
@Transactional
public class TransactionLoggingInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLoggingInterceptor.class);

    private static int counter = 0;

    @AroundInvoke
    public Object logTransaction(InvocationContext context) throws Exception {
        counter++;
        LOGGER.debug("Method {} is starting a @Transactional! Open Transactions: {}", context.getMethod().getName(), counter);
        try {
            return context.proceed();
        } finally {
            counter--;
            LOGGER.debug("Method {} has completed the @Transactional! Open Transactions: {}", context.getMethod().getName(), counter);
        }
    }
}