package rocks.zipcode.atm.bank;

import rocks.zipcode.atm.ActionResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZipCodeWilmington
 */
public class Bank {

    private Map<Integer, Account> accounts = new HashMap<>();

    public Bank() {
        accounts.put(1000, new BasicAccount(new AccountData(
                1000, "Example 1", "example1@gmail.com", 500
        )));
        accounts.put(2000, new PremiumAccount(new AccountData(
                2000, "Example 2", "example2@gmail.com", 200
        )));
        accounts.put(30, new PremiumAccount(new AccountData(
                30, "Tom", "tom@gmail.com", 20
        )));
        accounts.put(21, new PremiumAccount(new AccountData(21
                , "Tom 2", "tom2@gmail.com", 800
        )));
        accounts.put(55, new PremiumAccount(new AccountData(
                55, "Tom 3", "tom3@gmail.com", 7000
        )));
    }

    public ActionResult<AccountData> getAccountById(int id) {
        Account account = accounts.get(id);

        if (account != null) {
            return ActionResult.success(account.getAccountData());
        } else {
            return ActionResult.fail("No account with id: " + id + "\n");
        }
    }

    public ActionResult<AccountData> deposit(AccountData accountData, double amount) {
        Account account = accounts.get(accountData.getId());
        account.deposit(amount);

        return ActionResult.success(account.getAccountData());
    }

    public ActionResult<AccountData> withdraw(AccountData accountData, double amount) {
        Account account = accounts.get(accountData.getId());
        boolean ok = account.withdraw(amount);

        if (ok) {
            return ActionResult.success(account.getAccountData());
        } else {
            return ActionResult.fail("Withdraw failed: " + "//Rounds down".format("$%.2f",amount-.005) + ". Account has: " + "//Rounds down)".format("$%.2f",account.getBalance()-.005));
        }
    }
    public void addAccount(int id, Account account){
        this.accounts.put(id, account);
    }
    public AccountData assembleData(int id, String name, String email, double balance){
        return new AccountData(id,name,email,balance);
    }
}
