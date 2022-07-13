package run.predicate;

import java.util.function.Predicate;

public class CustomRetryExceptionPredicate implements Predicate<Throwable> {
    @Override
    public boolean test(Throwable throwable) {
        return throwable instanceof RuntimeException;
    }
}
