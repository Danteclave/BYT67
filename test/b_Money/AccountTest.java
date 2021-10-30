package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		testAccount.addTimedPayment("test1", 1,1, new Money(1, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("test1"));
		testAccount.removeTimedPayment("test1");
		assertFalse(testAccount.timedPaymentExists("test1"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		var alice = SweBank.getBalance("Alice");
		var hans = testAccount.getBalance().getAmount();
		testAccount.addTimedPayment("test1", 5, 0, new Money(1, SEK), SweBank, "Alice");
		testAccount.tick();
		assertEquals(hans-1, testAccount.getBalance().getAmount().intValue());
		assertEquals(alice+1, SweBank.getBalance("Alice").intValue());
		for (int i = 0; i < 4; i++)
		{
			testAccount.tick();
		}
		assertEquals(testAccount.tick(), 0);
		assertEquals(hans-1, testAccount.getBalance().getAmount().intValue());
		assertEquals(alice+1, SweBank.getBalance("Alice").intValue());
		assertEquals(testAccount.tick(), 1);
		assertEquals(hans-2, testAccount.getBalance().getAmount().intValue());
		assertEquals(alice+2, SweBank.getBalance("Alice").intValue());
	}

	@Test
	public void testTimedPaymentToBadAccount()
	{
		var hans = testAccount.getBalance().getAmount();
		testAccount.addTimedPayment("test1", 1,0, new Money(1, SEK), SweBank, "AMOGUS");
		assertEquals(1, testAccount.tick());
		assertEquals(hans, testAccount.getBalance().getAmount());
	}

	@Test
	public void testAddWithdraw() throws AccountDoesNotExistException
	{
		// this was the point where I realised fixed point was a bad choice
		var amnt = testAccount.getBalance().getAmount();
		testAccount.withdraw(new Money(100002, SEK));
		assertEquals(amnt-100002, testAccount.getBalance().getAmount().intValue());

	}
	
	@Test
	public void testGetBalance() {
		assertEquals(SEK, testAccount.getBalance().getCurrency());
		assertEquals(10000000, testAccount.getBalance().getAmount().intValue());
	}
}
