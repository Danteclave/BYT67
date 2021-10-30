package b_Money;

import java.util.Hashtable;

public class Account
{
	private Money content;
	private final Hashtable<String, TimedPayment> timedpayments = new Hashtable<String, TimedPayment>(); // no issues with making this final
	private String name;
	Account(String name, Currency currency) // why didn't this constructor take a String before
	{
		this.content = new Money(0, currency);
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public String setName(String name)
	{
		return this.name = name;
	}

	/**
	 * Add a timed payment
	 * @param id Id of timed payment
	 * @param interval Number of ticks between payments
	 * @param next Number of ticks till first payment (1 tick means it will fire on the next tick, not this one)
	 * @param amount Amount of Money to transfer each payment
	 * @param tobank Bank where receiving account resides
	 * @param toaccount Id of receiving account
	 */
	public void addTimedPayment(String id, Integer interval, Integer next, Money amount, Bank tobank, String toaccount)
	{
		TimedPayment tp = new TimedPayment(
				interval,
				next,
				amount,
				this,
				tobank,
				toaccount
		);
		timedpayments.put(id, tp);
	}
	
	/**
	 * Remove a timed payment
	 * @param id Id of timed payment to remove
	 */
	public void removeTimedPayment(String id)
	{
		timedpayments.remove(id);
	}
	
	/**
	 * Check if a timed payment exists
	 * @param id Id of timed payment to check for
	 */
	public boolean timedPaymentExists(String id)
	{
		return timedpayments.containsKey(id);
	}

	/**
	 * A time unit passes in the system
	 * @return how many payments were initiated (not necessarily succeeded)
	 */
	public int tick() // this now returns an int
	{
		var ct = 0;
		for (TimedPayment tp : timedpayments.values())
		{
			ct += tp.tick()?1:0;
		}
		return ct;
	}
	
	/**
	 * Deposit money to the account
	 * @param money Money to deposit.
	 */
	public void deposit(Money money)
	{
		content = content.add(money);
	}
	
	/**
	 * Withdraw money from the account
	 * @param money Money to withdraw.
	 */
	public void withdraw(Money money)
	{
		content = content.sub(money);
	}

	/**
	 * Get balance of account
	 * @return Amount of Money currently on account
	 */
	public Money getBalance()
	{
		return content;
	}

	/* Everything below belongs to the private inner class, TimedPayment */
	private static class TimedPayment
	{
		private final int interval;
		private int next;
		private final Account fromaccount;
		private final Money amount;
		private final Bank tobank;
		private final String toaccount;
		
		TimedPayment(Integer interval, Integer next, Money amount, Account fromaccount, Bank tobank, String toaccount)
		{
			this.interval = interval;
			this.next = next;
			this.amount = amount;
			this.fromaccount = fromaccount;
			this.tobank = tobank;
			this.toaccount = toaccount;
		}

		/* Return value indicates whether or not a transfer was initiated */
		public Boolean tick()
		{
			if (next == 0) {
				next = interval;

				fromaccount.withdraw(amount);
				try {
					tobank.deposit(toaccount, amount);
				}
				catch (AccountDoesNotExistException e) {
					/* Revert transfer.
					 * In reality, this should probably cause a notification somewhere. */
					fromaccount.deposit(amount);
				}
				return true;
			}
			else {
				next--;
				return false;
			}
		}
	}

}
