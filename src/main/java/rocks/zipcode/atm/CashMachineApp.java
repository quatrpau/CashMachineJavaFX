package rocks.zipcode.atm;

import rocks.zipcode.atm.bank.Bank;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;

/**
 * @author ZipCodeWilmington
 */
public class CashMachineApp extends Application {

    private TextField idField = new TextField();
    private TextField depositField = new TextField();
    private TextField withdrawField = new TextField();
    private CashMachine cashMachine = new CashMachine(new Bank());
    private Parent createContent() {
        VBox vbox = new VBox(10);
        vbox.setPrefSize(600, 600);
        //split the string on "\n"
        //put each thing on a separate line
        TextArea areaInfo = new TextArea();
        Button btnSubmit = new Button("Login");
        btnSubmit.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            cashMachine.login(id);

            areaInfo.setText(cashMachine.toString());
        });
        Button btnDeposit = new Button("Deposit");
        btnDeposit.setOnAction(e -> {
            int amount = Integer.parseInt(depositField.getText());
            cashMachine.deposit(amount);

            areaInfo.setText(cashMachine.toString());
        });

        Button btnWithdraw = new Button("Withdraw");
        btnWithdraw.setOnAction(e -> {
            int amount = Integer.parseInt(withdrawField.getText());
            cashMachine.withdraw(amount);

            areaInfo.setText(cashMachine.toString());
        });

        Button btnExit = new Button("Logout");
        btnExit.setOnAction(e -> {
            cashMachine.exit();

            areaInfo.setText(cashMachine.toString());
        });

        FlowPane flowpane = new FlowPane();
        VBox buttons = new VBox();
        VBox fields = new VBox();
        btnSubmit.setMaxWidth(Double.MAX_VALUE);
        btnDeposit.setMaxWidth(Double.MAX_VALUE);
        btnWithdraw.setMaxWidth(Double.MAX_VALUE);
        btnExit.setMaxWidth(Double.MAX_VALUE);
        areaInfo.setPrefSize(600,600);
        //areaInfo.setPrefSize(Double.MAX_VALUE,Double.MAX_VALUE);
        buttons.getChildren().addAll(btnSubmit,btnDeposit,btnWithdraw);
        fields.getChildren().addAll(idField,depositField,withdrawField);
        flowpane.getChildren().addAll(buttons,fields);
        vbox.getChildren().addAll(flowpane, btnExit, areaInfo);
        return vbox;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
