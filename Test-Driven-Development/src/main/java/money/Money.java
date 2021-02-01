package money;

class Money {
    protected int amount;
    protected String currency;

    public Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public static Money dollar(int i) {
        return new Money(i, "USD");
    }

    public static Money franc(int i) {
        return new Money(i, "CHF");
    }

    @Override
    public boolean equals(Object obj) {
        Money dollar = (Money) obj;
        return amount == dollar.amount && currency == dollar.currency;
    }

    Money times(int i){
        return new Money(amount * i, currency);
    };

    public String currency() {
        return currency;
    }
}
