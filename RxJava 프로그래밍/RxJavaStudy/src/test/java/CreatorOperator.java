import common.CommonUtils;
import common.Log;
import common.OkHttpHelper;
import common.Shape;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 생성연산자?
 * 데이터 흐름을 만드는 것.
 * 간단하게 Observable(Observable, Single, Maybe 객체)
 * ex) Just() / fromArray() / fromIterable() /from() / create()
 */
public class CreatorOperator {

    @Test
    public void testInterval() throws InterruptedException {
        Observable<Long> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(data -> (data + 1) * 100)
                .take(5);
        source.subscribe(Log::it);
        Thread.sleep(1000);
    }

    //일정 시간 간격으로 데이터 흐름 생성?
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    private static Observable<Long> interval(long initalDelay, long period, Scheduler unit) {
        return interval(initalDelay, period, Schedulers.computation());
    }


    @Test
    public void testDefer() {
        Iterator<String> colors = Arrays.asList(Shape.RED, Shape.GREEN, Shape.BLUE, Shape.PUPPLE).iterator();


        Callable<Observable<String>> supplier = () -> getObservable(colors);
        Observable<String> source = Observable.defer(supplier);

        source.subscribe(val -> Log.i("Subscriber #1:" + val));
        source.subscribe(val -> Log.i("Subscriber #2:" + val));
        CommonUtils.exampleComplete();
    }

    //번호가 적인 도형을 발행하는 Observable을 생성합니다.

    private Observable<String> getObservable(Iterator<String> colors) {
        if (colors.hasNext()) {
            String color = colors.next();
            return Observable.just(
                    Shape.getString(color, Shape.BALL),
                    Shape.getString(color, Shape.RECTANGLE),
                    Shape.getString(color, Shape.PENTAGON));
        }
        return Observable.empty();
    }

    @Test
    public void repeatTest() {
        CommonUtils.exampleStart();
        String serverUrl = "https://api.github.com/zen";

        //  인수 안넣으면 무한반복
        // 만약 스레드를 맞추러면 timer와 repeat를 제거하고 range 넣기.
        Observable.timer(2, TimeUnit.SECONDS)
                .map(val -> serverUrl)
                .map(OkHttpHelper::get)
                .repeat()
                .subscribe(res -> Log.it("Ping result : " + res));
        CommonUtils.sleep(10000);
    }
}
