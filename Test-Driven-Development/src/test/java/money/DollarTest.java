package money;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DollarTest {

    @Test
    void multiplication() {
        Money five = Money.dollar(5);
        assertEquals(five.times(2), Money.dollar(10));
        assertEquals(five.times(3), Money.dollar(15));
    }

    @Test
    void equality() {
        assertEquals(Money.dollar(5), Money.dollar(5));
        assertFalse(Money.dollar(5).equals(Money.dollar(6)));
        assertTrue(Money.franc(5).equals(Money.franc(5)));
        assertFalse(Money.franc(5).equals(Money.franc(6)));
        assertFalse(Money.franc(5).equals(Money.dollar(5)));
    }

    @Test
    void francMultiplication() {
        Money five = Money.franc(5);
        assertEquals(five.times(2), Money.franc(10));
        assertEquals(five.times(3), Money.franc(15));
    }

    @Test
    void currency() {
        assertEquals(Money.dollar(1).currency(), "USD");
        assertEquals(Money.franc(1).currency(), "CHF");
    }

    @Test
    void differentClassEquality() {
        assertTrue(new Money(10, "CHF").equals(new Franc(10, "CHF")));
    }

    @Test
    void simpleAddition() {
        Money _5 = Money.dollar(5);
        Expression sum= _5.plus(_5);
        Bank bank = new Bank();
        Money reduced = bank.reduce(sum, "USD");
        assertEquals(Money.dollar(10), reduced);
    }

    @Test
    void plusReturnsSum() {
        Money five = Money.dollar(5);
        Expression result = five.plus(five);
        Sum sum = (Sum)result;
        assertEquals(five, sum.augend);
        assertEquals(five, sum.addend);
    }

    @Test
    void reduceSum() {
        Expression sum = new Sum(Money.dollar(3), Money.dollar(4));
        Bank bank = new Bank();
        Money result = bank.reduce(sum, "USD");
        assertEquals(Money.dollar(7), result);
    }

    @Test
    void reduceMoney() {
        Bank bank = new Bank();
        Money result = bank.reduce(Money.dollar(1), "USD");
        assertEquals(result, Money.dollar(1));
    }

    @Test
    void reduceMoneyDifferentCurrency() {
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result = bank.reduce(Money.franc(2), "USD");
        assertEquals(result, Money.dollar(1));
    }

    @Test
    void arrayEquals() {
        assertEquals(new Object[]{"abc"}, new Object[]{"abc"});
    }
}