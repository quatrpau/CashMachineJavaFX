package rocks.zipcode.atm;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import rocks.zipcode.atm.bank.Bank;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * @author ZipCodeWilmington
 */
public class CashMachineApp extends Application {

    private TextField idField = new TextField();
    private TextField depositField = new TextField();
    private TextField withdrawField = new TextField();
    private CashMachine cashMachine = new CashMachine(new Bank());
    private Button btnDeposit = new Button("Deposit");
    private Button btnWithdraw = new Button("Withdraw");
    private Button btnLogout = new Button("Logout");
    private Button btnLogin = new Button("Login");
    private TextArea areaInfo = new TextArea();
    private Parent createContent() {
        //init states
        areaInfo.setEditable(false);
        setVisibility(false);
        VBox vbox = new VBox(10);
        vbox.setBackground(new Background(new BackgroundFill(Color.BLACK,new CornerRadii(4),new Insets(0))));
        vbox.setPrefSize(600, 600);
        areaInfo.setEditable(false);
        //deposit action
        btnDeposit.setOnAction(e -> {
            double amount = Double.parseDouble(depositField.getText());
            cashMachine.deposit(amount);

            areaInfo.setText(cashMachine.toString());
        });
        //withdraw action
        btnWithdraw.setOnAction(e -> {
            double amount = Double.parseDouble(withdrawField.getText());
            cashMachine.withdraw(amount);

            areaInfo.setText(cashMachine.toString());
        });

        //logout action
        btnLogout.setOnAction(e -> {
            cashMachine.exit();
            setVisibility(false);
            areaInfo.setText(cashMachine.toString());
        });
        //login action
        btnLogin.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            cashMachine.login(id);
            String ex = cashMachine.printException();
            if(ex.equals("")){
                setVisibility(true);
            }
            areaInfo.setText(ex + cashMachine.toString());
        });
        FlowPane flowpane = new FlowPane();
        VBox buttons = new VBox();
        VBox fields = new VBox();
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnDeposit.setMaxWidth(Double.MAX_VALUE);
        btnWithdraw.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        areaInfo.setPrefSize(600,600);
        buttons.getChildren().addAll(btnLogin,btnDeposit,btnWithdraw);
        fields.getChildren().addAll(idField,depositField,withdrawField);
        flowpane.getChildren().addAll(buttons,fields);
        vbox.getChildren().addAll(flowpane, btnLogout, areaInfo);
        return vbox;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Zip Code Cash Machine");
        Scene scene = new Scene(createContent());
        scene.setFill(Color.BLACK);
        MenuBar menuBar = new MenuBar();
        menuBar.setBackground(new Background(new BackgroundFill(Color.DARKTURQUOISE,new CornerRadii(0),new Insets(0))));
        Menu defaultAccountsMenu = new Menu("Default Accounts");
        MenuItem example1,example2,tom,tom2,tom3;
        (example1 = new MenuItem("Example 1")).setOnAction(e -> defaultLogin(1000));
        (example2 = new MenuItem("Example 2")).setOnAction(e -> defaultLogin(2000));
        (tom = new MenuItem("Tom")).setOnAction(e -> defaultLogin(30));
        (tom2 = new MenuItem("Tom 2")).setOnAction(e -> defaultLogin(21));
        (tom3 = new MenuItem("Tom 3")).setOnAction(e -> defaultLogin(55));
        defaultAccountsMenu.getItems().addAll(example1,example2,tom,tom2,tom3);
        menuBar.getMenus().add(defaultAccountsMenu);
        ((VBox) scene.getRoot()).getChildren().add(menuBar);
        stage.setScene(scene);
        stage.show();
    }
    private void defaultLogin(int id){
        cashMachine.login(id);
        setVisibility(true);
        areaInfo.setText(cashMachine.toString());
    }
    private void setVisibility(boolean isLoggedIn){
        btnDeposit.setVisible(isLoggedIn);
        btnDeposit.setDisable(!isLoggedIn);
        btnWithdraw.setVisible(isLoggedIn);
        btnWithdraw.setDisable(!isLoggedIn);
        btnLogout.setVisible(isLoggedIn);
        btnLogout.setDisable(!isLoggedIn);
        depositField.setVisible(isLoggedIn);
        depositField.setDisable(!isLoggedIn);
        withdrawField.setVisible(isLoggedIn);
        withdrawField.setDisable(!isLoggedIn);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
