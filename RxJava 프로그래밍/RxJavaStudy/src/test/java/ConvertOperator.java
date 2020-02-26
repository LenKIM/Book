import common.CommonUtils;
import common.Log;
import common.Shape;
import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;


public class ConvertOperator {

    //concatMap 은 flatmap 과 조금 다른 연산자. 들어온 순서대로 변환 시킴

    @Test
    public void testConcatMap() {
        CommonUtils.exampleStart();
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .concatMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "<>")
                        .take(2)
                );

        source.subscribe(Log::it);
        CommonUtils.sleep(2000);
    }

    //switchMap() 함수?
    //concatMap() 함수가 인터리빙이 발생할 수 있는 상황에서 동작의 순서를 보장해준다면
    // switchMap()함수는 순서를 보장하기 위해 기존에 진행 중이던 작업을 바로 중단한다.
    // 그리고 여러 개의 값이 발행되었을 때 마지막에 들어온 값만 처리하고 싶을 때 사용 중간에 끊기더라도 마지막 데이터의 처리는 보장하기 때문에.
    @Test
    public void testSwitchMap() {
        CommonUtils.exampleStart();
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .doOnNext(Log::it)
                .switchMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "<>")
                        .take(2)
                );

        source.subscribe(Log::it);
        CommonUtils.sleep(2000);
    }

    @Test
    public void testGroupBy() {
        String[] objs = {"6", "4", "2-T", "2", "6-T", "4-T"};
        Observable<GroupedObservable<String, String>> source =
                Observable.fromArray(objs).groupBy(CommonUtils::getShape);

        source.subscribe(obj -> obj
                .filter(val -> obj.getKey().equals(Shape.BALL))
                .subscribe(val -> System.out.println("GROUP:" + obj.getKey() + "\t Value:" + val)));
    }

    @Test
    public void testScan(){
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(balls)
                .scan((ball1, ball2) -> ball2 + "(" + ball1 + ")");
        source.subscribe(Log::i);
    }
}

