import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import org.junit.Test;

public class SubjectTest {

    @Test
    public void AsyncSubjectTest() {

        TestObserver<String> tester = new TestObserver<>();
        AsyncSubject<String> subject = AsyncSubject.create();
        subject.subscribe(tester);
        subject.subscribe(data -> System.out.println("Subscriber #1 => " + data));
        subject.onNext("1");
        subject.onNext("3");
        subject.subscribe(data -> System.out.println("Subscriber #2 => " + data));
        subject.onNext("5");
        subject.onComplete();

        tester.assertResult("5");
        tester.assertTerminated();
        tester.assertNoErrors();
    }

    @Test
    public void BehaviorSubjectTest() {

        TestObserver<String> tester = new TestObserver<>();

        BehaviorSubject<String> subject = BehaviorSubject.createDefault("6");
        subject.subscribe(tester);
        subject.subscribe(data -> System.out.println("Subscriber #1 => " + data));
        subject.onNext("1");
        subject.onNext("3");
        subject.subscribe(data -> System.out.println("Subscriber #2 => " + data));
        subject.onNext("5");
        subject.onComplete();

        tester.assertResult("6","1","3","5")
                .assertTerminated()
                .assertNoErrors();

    }

    @Test
    public void PublishSubject() {

        TestObserver<String> tester = new TestObserver<>();

        PublishSubject<String> subject = PublishSubject.create();
        subject.subscribe(tester);
        subject.subscribe(data -> System.out.println("Subscriber #1 => " + data));
        subject.onNext("1");
        subject.onNext("3");
        subject.subscribe(data -> System.out.println("Subscriber #2 => " + data));
        subject.onNext("5");
        subject.onComplete();

        tester.assertResult("1", "3", "5")
                .assertTerminated()
                .assertNoErrors();

    }

    @Test
    public void ReplaySubject() {
        TestObserver<String> tester = new TestObserver<>();

        ReplaySubject<String> subject = ReplaySubject.create();
        subject.subscribe(tester);
        subject.subscribe(data -> System.out.println("Subscriber #1 => " + data));
        subject.onNext("1");
        subject.onNext("3");
        subject.subscribe(data -> System.out.println("Subscriber #2 => " + data));
        subject.onNext("5");
        subject.onComplete();

        tester.assertResult("1","3","5");
    }
}
