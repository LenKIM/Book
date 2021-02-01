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
}