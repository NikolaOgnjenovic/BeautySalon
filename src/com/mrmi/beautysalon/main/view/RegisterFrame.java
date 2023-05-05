package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.AuthController;
import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class RegisterFrame extends JFrame {
    private final TreatmentController treatmentController;
    private final UserController userController;
    private final BeautySalon beautySalon;
    private final AuthController authController;
    private JButton registerButton;
    private JButton backButton;
    private JTextField usernameField, passwordField, nameField, surnameField, genderField, phoneField, addressField;
    private final boolean canPickUserType;
    private JComboBox<String> userTypeComboBox;
    private JLabel bonusLabel, salaryLabel, qualificationLabel, experienceLabel;
    private JTextField bonusField, salaryField, qualificationField, experienceField;
    public RegisterFrame(TreatmentController treatmentController, UserController userController, BeautySalon beautySalon, AuthController authController, boolean canPickUserType) {
        this.treatmentController = treatmentController;
        this.userController = userController;
        this.beautySalon = beautySalon;
        this.authController = authController;
        this.canPickUserType = canPickUserType;

        initialiseViews();
        initialiseListeners();
    }
    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2, debug", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Registration");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        JLabel usernameLabel = new JLabel("Username");
        Utility.setFont(usernameLabel, 24);
        this.add(usernameLabel, "align right");

        usernameField = new JTextField(20);
        Utility.setFont(usernameField, 24);
        this.add(usernameField, "align left");

        JLabel passwordLabel = new JLabel("Password");
        Utility.setFont(passwordLabel, 24);
        this.add(passwordLabel, "align right");

        passwordField = new JTextField(20);
        Utility.setFont(passwordField, 24);
        this.add(passwordField, "align left");

        JLabel nameLabel = new JLabel("Name");
        Utility.setFont(nameLabel, 24);
        this.add(nameLabel, "align right");

        nameField = new JTextField(20);
        Utility.setFont(nameField, 24);
        this.add(nameField, "align left");

        JLabel surnameLabel = new JLabel("Surname");
        Utility.setFont(surnameLabel, 24);
        this.add(surnameLabel, "align right");

        surnameField = new JTextField(20);
        Utility.setFont(surnameField, 24);
        this.add(surnameField, "align left");

        JLabel genderLabel = new JLabel("Gender");
        Utility.setFont(genderLabel, 24);
        this.add(genderLabel, "align right");

        genderField = new JTextField(20);
        Utility.setFont(genderField, 24);
        this.add(genderField, "align left");

        JLabel phoneLabel = new JLabel("Phone number");
        Utility.setFont(phoneLabel, 24);
        this.add(phoneLabel, "align right");

        phoneField = new JTextField(20);
        Utility.setFont(phoneField, 24);
        this.add(phoneField, "align left");

        JLabel addressLabel = new JLabel("Address");
        Utility.setFont(addressLabel, 24);
        this.add(addressLabel, "align right");

        addressField = new JTextField(20);
        Utility.setFont(addressField, 24);
        this.add(addressField, "align left");

        if (canPickUserType) {
            userTypeComboBox = new JComboBox<>();
            userTypeComboBox.addItem("Client");
            userTypeComboBox.addItem("Beautician");
            userTypeComboBox.addItem("Receptionist");
            userTypeComboBox.addItem("Manager");
            Utility.setFont(userTypeComboBox, 24);
            this.add(userTypeComboBox, "span");

            experienceLabel = new JLabel("Years of experience");
            Utility.setFont(experienceLabel, 24);
            this.add(experienceLabel, "align right");
            experienceLabel.setVisible(false);

            experienceField = new JTextField(20);
            Utility.setFont(experienceField, 24);
            this.add(experienceField, "align left");
            experienceField.setVisible(false);

            qualificationLabel = new JLabel("Qualification level");
            Utility.setFont(qualificationLabel, 24);
            this.add(qualificationLabel, "align right");
            qualificationLabel.setVisible(false);

            qualificationField = new JTextField(20);
            Utility.setFont(qualificationField, 24);
            this.add(qualificationField, "align left");
            qualificationField.setVisible(false);

            salaryLabel = new JLabel("Salary");
            Utility.setFont(salaryLabel, 24);
            this.add(salaryLabel, "align right");
            salaryLabel.setVisible(false);

            salaryField = new JTextField(20);
            Utility.setFont(salaryField, 24);
            this.add(salaryField, "align left");
            salaryField.setVisible(false);

            bonusLabel = new JLabel("Salary bonus");
            Utility.setFont(bonusLabel, 24);
            this.add(bonusLabel, "align right");
            bonusLabel.setVisible(false);

            bonusField = new JTextField(20);
            Utility.setFont(bonusField, 24);
            this.add(bonusField, "align left");
            bonusField.setVisible(false);
        }

        registerButton = new JButton("Register");
        Utility.setFont(registerButton, 24);
        this.add(registerButton, "span");

        backButton = new JButton("Back");
        Utility.setFont(backButton, 24);
        this.add(backButton, "span");
    }

    // TODO
    private void initialiseListeners() {
        if (canPickUserType) {
            userTypeComboBox.addActionListener(e -> {
                if (userTypeComboBox.getSelectedIndex() > 0) {
                    experienceLabel.setVisible(true);
                    experienceField.setVisible(true);
                    qualificationLabel.setVisible(true);
                    qualificationField.setVisible(true);
                    salaryLabel.setVisible(true);
                    salaryField.setVisible(true);
                    bonusLabel.setVisible(true);
                    bonusField.setVisible(true);
                } else {
                    experienceLabel.setVisible(false);
                    experienceField.setVisible(false);
                    qualificationLabel.setVisible(false);
                    qualificationField.setVisible(false);
                    salaryLabel.setVisible(false);
                    salaryField.setVisible(false);
                    bonusLabel.setVisible(false);
                    bonusField.setVisible(false);
                }
            });
        }
        registerButton.addActionListener(e -> {
            switch (userTypeComboBox.getSelectedIndex()) {
                case 0:
                    Client client = new Client(passwordField.getText(), nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText());
                    ClientFrame clientFrame = new ClientFrame(client, usernameField.getText(), treatmentController, userController, beautySalon, authController);
                    break;
                case 1:
                    Beautician beautician = new Beautician(passwordField.getText(), nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), Byte.parseByte(qualificationField.getText()), Byte.parseByte(experienceField.getText()), Float.parseFloat(bonusField.getText()), Float.parseFloat(salaryField.getText()));
                    userController.addUser(usernameField.getText(), beautician);
                    this.dispose();
                    break;
                case 2:
                    Receptionist receptionist = new Receptionist(passwordField.getText(), nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), Byte.parseByte(qualificationField.getText()), Byte.parseByte(experienceField.getText()), Float.parseFloat(bonusField.getText()), Float.parseFloat(salaryField.getText()));
                    userController.addUser(usernameField.getText(), receptionist);
                    this.dispose();
                    break;
                case 3:
                    Manager manager = new Manager(passwordField.getText(), nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), Byte.parseByte(qualificationField.getText()), Byte.parseByte(experienceField.getText()), Float.parseFloat(bonusField.getText()), Float.parseFloat(salaryField.getText()));
                    userController.addUser(usernameField.getText(), manager);
                    this.dispose();
                    break;
            }
        });
        backButton.addActionListener(e -> this.dispose());
    }
}
