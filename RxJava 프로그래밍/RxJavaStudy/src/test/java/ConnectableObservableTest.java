import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class ConnectableObservableTest {

    /**
     * https://github.com/ReactiveX/RxJava/wiki/Connectable-Observable-Operators
     *
     * @throws InterruptedException
     */
    @Test
    public void TestConnectableObservable() throws InterruptedException {
        TestObserver<String> tester = new TestObserver<>();

        String[] dt = {"1", "3", "5"};
        Observable<String> balls = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(i -> dt[i])
                .take(dt.length);

        ConnectableObservable<String> source = balls.publish();
        source.subscribe(tester);
        source.subscribe(data -> System.out.println("Subscriber #1 => " + data));
        source.subscribe(data -> System.out.println("Subscriber #2 => " + data));
        source.connect();
        Thread.sleep(250);

        source.subscribe(data -> System.out.println("Subscriber #3 => " + data));
        Thread.sleep(100);

        tester.assertResult("1","3","5");

    }
}
