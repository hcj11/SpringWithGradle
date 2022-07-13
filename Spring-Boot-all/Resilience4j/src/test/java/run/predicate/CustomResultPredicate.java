package run.predicate;

import java.util.function.Predicate;

public class CustomResultPredicate implements Predicate<Object> {

    @Override
    public boolean test(Object obj) {
        return obj instanceof String;
    }
}
