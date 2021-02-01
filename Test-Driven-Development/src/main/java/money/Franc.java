package money;

public class Franc extends Money {

    public Franc(int amount, String currency) {
        super(amount, currency);
    }

    @Override
    public Money times(int i) {
        return new Money(amount * i, currency);
    }

    @Override
    public String currency() {
        return "CHF";
    }


}
