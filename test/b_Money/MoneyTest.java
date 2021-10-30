package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100, DKK7d5, DKK10;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);

		DKK7d5 = new Money(750, DKK);
		DKK10 = new Money(300, DKK);
	}

	@Test
	public void testGetAmount() {
		assertEquals(10000, SEK100.getAmount().intValue());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(EUR, EUR10.getCurrency());
		assertNotEquals(DKK, SEK100.getCurrency());
	}

	@Test
	public void testToString() {
		assertNotEquals("100 SEK", SEK100.toString());
		assertEquals("20.00 EUR", EUR20.toString());
	}

	@Test
	public void testGlobalValue() {
		assertEquals(300000, EUR20.universalValue().intValue());
	}

	@Test
	public void testEqualsMoney() {
		assertTrue(SEK100.equals(EUR10));
		assertTrue(SEK200.equals(EUR20));
		assertFalse(SEK100.equals(EUR20));
	}

	@Test
	public void testAdd() {

		assertEquals( 600, DKK10.add(DKK10).getAmount().intValue());
		assertEquals( 2000, EUR10.add(SEK100).getAmount().intValue());
		assertEquals( 1100, EUR10.add(DKK7d5).getAmount().intValue());
	}

	@Test
	public void testSub() {
		assertEquals(0, EUR10.sub(EUR10).getAmount().intValue());
		assertEquals(0, EUR10.sub(SEK100).getAmount().intValue());
		assertEquals(900, EUR10.sub(DKK7d5).getAmount().intValue());
		Money small = new Money(1, EUR);
		System.out.println(EUR.valueInThisCurrency(small.getAmount(), small.getCurrency()));
		assertEquals(999, EUR10.sub(small).getAmount().intValue());
	}

	@Test
	public void testIsZero() {
		assertTrue(SEK0.isZero());
		assertFalse(SEKn100.isZero());
	}

	@Test
	public void testNegate() {
		assertEquals(SEK100.getAmount().intValue(), SEKn100.negate().getAmount().intValue());
		assertEquals(-750, DKK7d5.negate().getAmount().intValue());
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, SEK100.compareTo(EUR10));
		assertTrue(DKK7d5.compareTo(EUR20) < 0);
		assertTrue(EUR20.compareTo(EUR10) > 0);
	}
}
