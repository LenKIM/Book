import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.junit.Test;

public class ReduceTest {

    @Test
    public void Testreduce() {

        String[] balls = {"1", "3", "5"};
        Maybe<String> source = Observable.fromArray(balls)
                .reduce((ball1, ball2) -> ball2 + "(" + ball1 + ")");

        source.test().assertResult("5(3(1))");
    }
}
