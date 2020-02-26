import io.reactivex.Flowable;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class Basic {
    @Test
    public void testHelloWorld() {
        String result = "";
        Flowable.just("Hello world").subscribe(System.out::println);
        assertEquals("Hello world",result);
    }

    @Test
    public void testHelloWorld2() {
        ArrayList<Integer> list= new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        String result = "";
        Flowable.just("Hello world").subscribe(System.out::println);
        assertEquals("Hello world",result);
    }
}