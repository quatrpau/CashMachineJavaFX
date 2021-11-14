package rocks.zipcode.atm;

import rocks.zipcode.atm.bank.AccountData;
import rocks.zipcode.atm.bank.Bank;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author ZipCodeWilmington
 */
public class CashMachine {

    private final Bank bank;
    private AccountData accountData = null;
    private Exception except = null;

    public CashMachine(Bank bank) {
        this.bank = bank;
    }

    private Consumer<AccountData> update = data -> {
        accountData = data;
    };

    public void login(int id) {
        tryCall(
                () -> bank.getAccountById(id),
                update
        );
    }

    public void deposit(double amount) {
        if (accountData != null) {
            tryCall(
                    () -> bank.deposit(accountData, amount),
                    update
            );
        }
    }

    public void withdraw(double amount) {
        if (accountData != null) {
            tryCall(
                    () -> bank.withdraw(accountData, amount),
                    update
            );
        }
    }

    public void exit() {
        if (accountData != null) {
            accountData = null;
        }
    }
    public String printException() {
        if(this.except != null){
            String ex = except.getMessage();
            this.except = null;
            return ex + "\n\n";
        }
        return "";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(this.except != null){
            sb.append(except.getMessage()).append("\n\n");
            this.except = null;
        }
        if(accountData != null){
            sb.append(accountData).append("\n");
        }
        else{
            sb.append("Try account 21, 30, 55, 1000 or 2000 and click submit." + "\n");
        }
        return sb.toString();
    }

    private <T> void tryCall(Supplier<ActionResult<T> > action, Consumer<T> postAction) {
        try {
            ActionResult<T> result = action.get();
            if (result.isSuccess()) {
                T data = result.getData();
                postAction.accept(data);
            } else {
                String errorMessage = result.getErrorMessage();
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            this.except = e;
            System.out.println("Error: " + e.getMessage());
        }
    }
}
