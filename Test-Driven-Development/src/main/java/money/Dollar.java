package money;

public class Dollar extends Money {

    public Dollar(int amount, String currency) {
        super(amount, currency);
    }

    @Override
    public Money times(int i) {
        return new Dollar(amount * i, currency);
    }

    @Override
    public String currency() {
        return "USD";
    }
}
