package application;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import structs.Account;

public class t1 {

	@Test
	public void test() {
		Account account = new Account();
		account.setFirstname("fname");
		account.setId(1);
		account.setImageFile("jpg");
		account.setLastname("lname");
		account.setDate(LocalDate.now());
		Assert.assertFalse(account.contains("1432456"));
		Assert.assertFalse(account.contains("xyz"));
		Assert.assertFalse(account.contains("634sadf"));
		Assert.assertTrue(account.contains("1"));
		Assert.assertTrue(account.contains("fname"));
		Assert.assertTrue(account.contains("jpg"));
		Assert.assertTrue(account.contains("lname"));
	}
}
