package stream.forEach;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

class HeaderBuilder extends CountedCompleter<Integer> {
    private PacketSender packetSender;
    private Integer result;

    public HeaderBuilder(PacketSender p) {
        this.packetSender = p;
    }

    @Override
    public void compute() {
        this.setPendingCount(1);
        result = 1;
        tryComplete();
    }

    @Override
    public Integer getRawResult() {
        return result;
    }
}

class BodyBuilder extends CountedCompleter<Integer> {
    private PacketSender packetSender;
    private Integer result;

    public BodyBuilder(PacketSender p) {
        this.packetSender = p;
    }

    @Override
    public Integer getRawResult() {
        return result;
    }

    @Override
    public void compute() {
        this.setPendingCount(1);
        result = 2;
        tryComplete();
    }
}

@Slf4j
class PacketSender extends CountedCompleter<Integer> {
    ArrayList<Object> objects = Lists.newArrayList();

    PacketSender() {
        super(null, 1);
    } // trigger on second completion

    public void compute() {
    } // never called

    // todo  learn  completeFuture
    public void onCompletion(CountedCompleter<?> caller) {

//        for (CountedCompleter<?> c = caller.firstComplete(); c != null; c = c.nextComplete()) {
//            Object o = null;
//            try {
//                o = c.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//            objects.add(o);
//        }
//
//        if (objects.size() == 2) {
//            objects.stream().forEach(s -> {
//                log.info("{}", ((Integer) s).toString());
//            });
//        }
    }

    private Boolean sendPacket() {
        log.info("sendPacket by network...");
        return true;
    }
}


class MapReducerTwo<E> extends CountedCompleter<E> { // version 2
    final Integer[] array;
    final MyMapper<E> mapper;
    final MyReducer<E> reducer;
    final int lo, hi;
    MapReducerTwo<E> forks, next; // record subtask forks in list
    Integer result;

    MapReducerTwo(CountedCompleter<?> p, Integer[] array, MyMapper<E> mapper,
                  MyReducer<E> reducer, int lo, int hi, MapReducerTwo<E> next) {
        super(p);
        this.array = array;
        this.mapper = mapper;
        this.reducer = reducer;
        this.lo = lo;
        this.hi = hi;
        this.next = next;
    }

    public void compute() {
        int l = lo, h = hi;
        while (h - l >= 2) {
            int mid = (l + h) >>> 1;
            addToPendingCount(1);
            (forks = new MapReducerTwo(this, array, mapper, reducer, mid, h, forks)).fork();
            h = mid;
        }
        if (h > l)
            result = mapper.apply(array[l]);
        // process completions by reducing along and advancing subtask links
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
            for (MapReducerTwo t = (MapReducerTwo) c, s = t.forks; s != null; s = t.forks = s.next)
                t.result = reducer.apply(t.result, s.result);
        }
    }

    public E getRawResult() {
        return (E) result;
    }

    public static <E> E mapReduce(Integer[] array, MyMapper<E> mapper, MyReducer<E> reducer) {
        return new MapReducerTwo<E>(null, array, mapper, reducer,
                0, array.length, null).invoke();
    }
}

/**
 * 1 + 1 + 1
 */
@Slf4j
class MyMapper<E> {
    Integer apply(Integer v) {
        return v;
    }
}

@Slf4j
class MyReducer<E> {
    java.lang.Integer apply(java.lang.Integer x, java.lang.Integer y) {
        return x + y;
    }
}

/**
 *
 */
class MapReducer<E> extends CountedCompleter<E> {
    final Integer[] array;
    final MyMapper<E> mapper;
    final MyReducer<E> reducer;
    final int lo, hi;
    MapReducer<E> sibling;
    Integer result;

    MapReducer(CountedCompleter<?> p, Integer[] array, MyMapper<E> mapper,
               MyReducer<E> reducer, int lo, int hi) {
        super(p);
        this.array = array;
        this.mapper = mapper;
        this.reducer = reducer;
        this.lo = lo;
        this.hi = hi;
    }

    public void compute() {
        if (hi - lo >= 2) {
            int mid = (lo + hi) >>> 1;
            MapReducer<E> left = new MapReducer(this, array, mapper, reducer, lo, mid);
            MapReducer<E> right = new MapReducer(this, array, mapper, reducer, mid, hi);
            left.sibling = right;
            right.sibling = left;
            setPendingCount(2); // only right is pending
            right.fork();
            left.fork();     // directly execute left
        } else {
            if (hi > lo)
                result = mapper.apply(array[lo]);

        }

//        for (CountedCompleter c = firstComplete();
//             c != null;
//             c = c.nextComplete()) {
//            c.quietlyComplete();
//            // ... process c ...
//        }
        tryComplete();
    }

    public void onCompletion(CountedCompleter<?> caller) {
        if (caller != this) {
            MapReducer<E> child = (MapReducer<E>) caller;
            MapReducer<E> sib = child.sibling;
            if (sib == null || sib.result == null)
                result = child.result;
            else
                result = reducer.apply(child.result, sib.result);
        }
    }

    public E getRawResult() {
        return (E) result;
    }

    public static <E> E mapReduce(Integer[] array, MyMapper<E> mapper, MyReducer<E> reducer) {
        return new MapReducer<E>(null, array, mapper, reducer,
                0, array.length).invoke();
    }
}

class Searcher<E> extends CountedCompleter<E> {
    final E[] array;
    final AtomicReference<E> result;
    final int lo, hi;

    Searcher(CountedCompleter<?> p, E[] array, AtomicReference<E> result, int lo, int hi) {
        super(p);
        this.array = array;
        this.result = result;
        this.lo = lo;
        this.hi = hi;
    }

    public E getRawResult() {
        return result.get();
    }

    public void compute() { // similar to ForEach version 3
        int l = lo, h = hi;
        while (result.get() == null && h >= l) {
            if (h - l >= 2) {
                int mid = (l + h) >>> 1;
                addToPendingCount(1);
                new Searcher(this, array, result, mid, h).fork();
                h = mid;
            } else {
                E x = array[l];
                if (matches(x) && result.compareAndSet(null, x))
                    quietlyCompleteRoot(); // root task is now joinable
                break;
            }
        }
        tryComplete(); // normally complete whether or not found
    }

    boolean matches(E e) {
        if (e.toString().equalsIgnoreCase("7")) {
            return true;
        } else {
            return false;
        }
    } // return true if found

    public static <E> E search(E[] array) {
        return new Searcher<E>(null, array, new AtomicReference<E>(), 0, array.length).invoke();
    }

    public static <E> E searchItem(E[] array) throws ExecutionException, InterruptedException {
        E e = new Searcher<E>(null, array, new AtomicReference<E>(), 0, array.length).fork().get();
        return e;
    }
}

@Slf4j
class MyOperation<E> {
    void apply(E e) {
        log.info("{}", e.toString());
    }

}

@Slf4j
class ForEachVersionFour<E> extends CountedCompleter<Void> {

    public static <E> void forEach(E[] array, MyOperation<E> op) {
        new ForEach<E>(null, array, op, 0, array.length).invoke();
    }

    final E[] array;
    final MyOperation<E> op;
    final int lo;
    final int hi;

    ForEachVersionFour(CountedCompleter<?> p, E[] array, MyOperation<E> op, int lo, int hi) {
        super(p);
        this.array = array;
        this.op = op;
        this.lo = lo;
        this.hi = hi;
    }

    public void compute() { // version 3
        int l = lo, h = hi;
        while (h - l >= 2) {
            int mid = (l + h) >>> 1;
            addToPendingCount(1);
            new ForEach(this, array, op, l, mid).fork(); // left child
            l = mid;
        }
        if (h > l)
            op.apply(array[l]);
        propagateCompletion();
    }
}

@Slf4j
class ForEachVersionThere<E> extends CountedCompleter<Void> {

    public static <E> void forEach(E[] array, MyOperation<E> op) {
        new ForEach<E>(null, array, op, 0, array.length).invoke();
    }

    final E[] array;
    final MyOperation<E> op;
    final int lo;
    final int hi;

    ForEachVersionThere(CountedCompleter<?> p, E[] array, MyOperation<E> op, int lo, int hi) {
        super(p);
        this.array = array;
        this.op = op;
        this.lo = lo;
        this.hi = hi;
    }

    public void compute() { // version 3
        int l = lo, h = hi;
        while (h - l >= 2) {
            int mid = (l + h) >>> 1;
            addToPendingCount(1);
            new ForEach(this, array, op, mid, h).fork(); // right child
            h = mid;
        }
        if (h > l)
            op.apply(array[l]);
        propagateCompletion();
    }
}


@Slf4j
class ForEachVersionTwo<E> extends CountedCompleter<Void> {

    public static <E> void forEach(E[] array, MyOperation<E> op) {
        new ForEach<E>(null, array, op, 0, array.length).invoke();
    }

    final E[] array;
    final MyOperation<E> op;
    final int lo, hi;

    ForEachVersionTwo(CountedCompleter<?> p, E[] array, MyOperation<E> op, int lo, int hi) {
        super(p);
        this.array = array;
        this.op = op;
        this.lo = lo;
        this.hi = hi;
    }

    public void compute() { // version 2
        if (hi - lo >= 2) {
            int mid = (lo + hi) >>> 1;
            setPendingCount(1); // must set pending count before fork
            new ForEachVersionTwo(this, array, op, mid, hi).fork(); // right child
            new ForEachVersionTwo(this, array, op, lo, mid).invoke(); // left child
        } else if (hi > lo)
            op.apply(array[lo]);
        tryComplete();
    }
}

@Slf4j
class ForEach<E> extends CountedCompleter<Void> {

    public static <E> void forEach(E[] array, MyOperation<E> op) {
        new ForEach<E>(null, array, op, 0, array.length).invoke();
    }

    final E[] array;
    final MyOperation<E> op;
    final int lo, hi;

    ForEach(CountedCompleter<?> p, E[] array, MyOperation<E> op, int lo, int hi) {
        super(p);
        this.array = array;
        this.op = op;
        this.lo = lo;
        this.hi = hi;
    }

    public void compute() { // version 1
        if (hi - lo >= 2) {
            int mid = (lo + hi) >>> 1;
            setPendingCount(2); // must set pending count before fork
            new ForEach(this, array, op, mid, hi).fork(); // right child
            new ForEach(this, array, op, lo, mid).fork(); // left child
        } else if (hi > lo)
            op.apply(array[lo]);
        tryComplete();
    }
}