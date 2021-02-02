package money;

class Money implements Expression {
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
    }

    public String currency() {
        return currency;
    }

    public Expression plus(Money addend) {
        return new Sum(this, addend);
    }

    public Money reduce(Bank bank, String to){
        int rate = bank.rate(this.currency, to);
        return new Money(amount / rate, to);
    }
}
