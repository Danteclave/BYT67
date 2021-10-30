package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("DKK", DKK.getName());
		assertNotEquals("DKK", SEK.getName());
	}

	@Test
	public void testGetCurrency() {
		// turns out the currency class didn't override the equals method
		// in all honesty the currencies could be implemented as a singleton
		// added equals method override to Currency to fix this test
		assertEquals(SEK, Nordea.getCurrency());
		assertNotEquals(SEK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		Nordea.openAccount("Ikea");
		assertEquals(0, Nordea.getBalance("Ikea").intValue());
		try {
			Nordea.openAccount("Ikea");
		}
		catch(AccountExistsException e)
		{
			assertEquals(AccountExistsException.class, e.getClass());
		}
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		assertEquals(0, SweBank.getBalance("Bob").intValue());
		SweBank.deposit("Bob", new Money(1337, SEK));
		assertEquals(1337, SweBank.getBalance("Bob").intValue());
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		assertEquals(0, SweBank.getBalance("Bob").intValue());
		SweBank.withdraw("Bob", new Money(1337, SEK));
		assertEquals(-1337, SweBank.getBalance("Bob").intValue());
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		assertEquals(0, SweBank.getBalance("Bob").intValue());
		assertEquals(0, Nordea.getBalance("Bob").intValue());
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {

		/*
			this let me figure out that the overload of transfer
			within one bank had an oopsie and repeated
			fromaccount twice >:(
		* */
		SweBank.transfer("Bob", "Ulrika", new Money(1, SEK));
		assertEquals(-1, SweBank.getBalance("Bob").intValue());
		assertEquals(1, SweBank.getBalance("Ulrika").intValue());

		SweBank.transfer("Ulrika", Nordea, "Bob", new Money(1, SEK));
		assertEquals(1, Nordea.getBalance("Bob").intValue());
		assertEquals(0, SweBank.getBalance("Ulrika").intValue());
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		SweBank.addTimedPayment("Bob", "bob's pjatk tuition", 1, 0, new Money(99999, SEK), Nordea, "Bob");
		SweBank.tick();
		assertEquals(-99999, SweBank.getBalance("Bob").intValue());
		assertEquals(99999, Nordea.getBalance("Bob").intValue());
		SweBank.tick();
		assertEquals(-99999, SweBank.getBalance("Bob").intValue());
		assertEquals(99999, Nordea.getBalance("Bob").intValue());
		SweBank.tick();
		assertEquals(-(2*99999), SweBank.getBalance("Bob").intValue());
		assertEquals(2*99999, Nordea.getBalance("Bob").intValue());
		SweBank.removeTimedPayment("Bob", "bob's pjatk tuition");
		SweBank.tick();
		SweBank.tick();
		assertEquals(-(2*99999), SweBank.getBalance("Bob").intValue());
		assertEquals(2*99999, Nordea.getBalance("Bob").intValue());
	}
}
