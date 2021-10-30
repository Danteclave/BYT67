package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("SEK", SEK.getName());
		assertNotEquals("SUS", EUR.getName());
	}
	
	@Test
	public void testGetRate() {
		assertEquals(0.15, SEK.getRate(), 0.001);
		assertNotEquals(199, DKK.getRate(), 0.001);
	}
	
	@Test
	public void testSetRate() {
		EUR.setRate(4.1);
		assertEquals(4.1, EUR.getRate(), 0.001);
		assertNotEquals(1.5, EUR.getRate(), 0.001);
	}
	
	@Test
	public void testGlobalValue() {
		assertEquals(30000, EUR.universalValue(200).intValue());
		assertEquals(15000, SEK.universalValue(1000).intValue());
		assertNotEquals(1000, SEK.universalValue(100).intValue());
		assertEquals(15000, DKK.universalValue(750).intValue());
	}
	
	@Test
	public void testValueInThisCurrency() {
		assertEquals(750, DKK.valueInThisCurrency(100, EUR).intValue());
		assertEquals(100, EUR.valueInThisCurrency(1000, SEK).intValue());
		assertEquals(100, EUR.valueInThisCurrency(750, DKK).intValue());
	}

}
