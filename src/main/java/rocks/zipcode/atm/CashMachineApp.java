package rocks.zipcode.atm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import rocks.zipcode.atm.bank.Bank;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



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
    private Button btnCreate = new Button("Create Account");
    private TextArea areaInfo = new TextArea();
    private Parent createContent() {
        //init states
        btnLogin.setDefaultButton(true);
        areaInfo.setEditable(false);
        setVisibility(false);
        VBox mainBox = new VBox(10);
        mainBox.setBackground(new Background(new BackgroundFill(Color.BLACK,new CornerRadii(4),new Insets(0))));
        mainBox.setPrefSize(600, 600);
        FlowPane topPane = new FlowPane();
        VBox buttons = new VBox();
        VBox fields = new VBox();
        VBox create = new VBox();
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnDeposit.setMaxWidth(Double.MAX_VALUE);
        btnWithdraw.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        areaInfo.setPrefSize(600,600);
        buttons.getChildren().addAll(btnLogin,btnDeposit,btnWithdraw);
        fields.getChildren().addAll(idField,depositField,withdrawField);
        create.getChildren().add(btnCreate);
        topPane.getChildren().addAll(buttons,fields,create);
        mainBox.getChildren().addAll(topPane, btnLogout, areaInfo);
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
        //create action
        btnCreate.setOnAction(e -> {
            VBox subMainBox = new VBox();
            subMainBox.setPrefSize(300,300);
            subMainBox.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE,new CornerRadii(0),new Insets(0))));
            Scene subScene = new Scene(subMainBox);
            Stage subStage = new Stage();
            subStage.setTitle("Account Creation");
            subStage.setScene(subScene);
            VBox subLabels = new VBox(10.25);
            subLabels.setAlignment(Pos.CENTER_RIGHT);
            VBox subFields = new VBox();
            FlowPane subPane = new FlowPane(5,0);
            Label lid = new Label("ID");
            Label lname = new Label("Name");
            Label lemail = new Label("Email");
            Label lbalance = new Label("Initial Deposit");
            Label laccount = new Label("Account Type: ");
            TextField fid = new TextField();
            TextField fname = new TextField();
            TextField femail = new TextField();
            TextField fbalance = new TextField();
            Button btnSubmit = new Button("Submit");
            Label lerror = new Label("");
            lerror.setTextFill(Color.WHITE);
            //Radio Button Group
            ToggleGroup accountType = new ToggleGroup();
            RadioButton basic = new RadioButton("Basic");
            basic.setSelected(true);
            RadioButton premium = new RadioButton("Premium");
            basic.setToggleGroup(accountType);
            premium.setToggleGroup(accountType);
            btnSubmit.setOnAction(f -> {
                try {
                    if (fid.getText().equals("") || fbalance.getText().equals("")){
                        throw new NullPointerException();
                    }
                    int newId = Integer.parseInt(fid.getText());
                    String newName = fname.getText();
                    String newEmail = femail.getText();
                    double newBalance = Double.parseDouble(fbalance.getText());
                    //checks if newName or newEmail are null
                    if (newName.equals("") || newEmail.equals("") ) {
                        throw new NullPointerException();
                    }
                    //checks if ID fits constraints
                    if (0 >= newId || newId > 2000) {
                        lerror.setText("ID should be > or = to 0 and < 2000.");
                        throw new Exception();
                    }
                    //checks if ID is already in use
                    cashMachine.login(newId);
                    if (cashMachine.printException().equals("")) {
                        lerror.setText("ID is already in use.");
                        throw new Exception();
                    }
                    //how does this work for basic accounts?
                    if (newBalance < -100) {
                        lerror.setText("Invalid Balance");
                        throw new Exception();
                    }
                    RadioButton selection = (RadioButton) accountType.getSelectedToggle();
                    if (selection.getText().equals("Basic")) {
                        cashMachine.addBasicAccount(newId, newName, newEmail, newBalance);
                    } else if (selection.getText().equals("Premium")) {
                        cashMachine.addPremiumAccount(newId, newName, newEmail, newBalance);
                    } else {
                        lerror.setText("Unknown Account Type");
                        throw new Exception();
                    }
                    defaultLogin(newId);
                    subStage.close();
                } catch (NullPointerException npe) {
                    lerror.setText("Be sure to fill in all fields.");
                    npe.printStackTrace();
                } catch(NumberFormatException nfe) {
                    lerror.setText("Sorry, I only speak decimal; Please try again!");
                    nfe.printStackTrace();
                }
                 catch(Exception exception) {
                    exception.printStackTrace();
                }
            });
            basic.setToggleGroup(accountType);
            premium.setToggleGroup(accountType);
            FlowPane radioPane = new FlowPane();
            radioPane.getChildren().addAll(basic,premium);
            subLabels.getChildren().addAll(lid,lname,lemail,lbalance,laccount);
            subFields.getChildren().addAll(fid,fname,femail,fbalance);
            subPane.getChildren().addAll(subLabels,subFields);
            subMainBox.getChildren().addAll(subPane,radioPane,btnSubmit,lerror);
            subStage.show();
        });
        return mainBox;
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
        btnCreate.setVisible(!isLoggedIn);
        btnCreate.setDisable(isLoggedIn);
        depositField.setVisible(isLoggedIn);
        depositField.setDisable(!isLoggedIn);
        withdrawField.setVisible(isLoggedIn);
        withdrawField.setDisable(!isLoggedIn);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
