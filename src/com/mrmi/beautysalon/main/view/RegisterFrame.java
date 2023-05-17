package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class RegisterFrame extends JFrame {
    private final SalonManager salonManager;
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final AuthManager authManager;
    private final boolean canPickUserType;
    private final boolean isClient;
    private final User user;
    private JButton buttonRegister;
    private JButton buttonBack;
    private JTextField textUsername, textPassword, textName, textSurname, textGender, textPhone, textAddress, textSalary, textQualificationLevel, textExperience;

    private JComboBox<String> comboBoxUserType;
    private JLabel labelSalary, labelQualificationLevel, labelExperience;
    public RegisterFrame(SalonManager salonManager, TreatmentManager treatmentManager, UserManager userManager, AuthManager authManager, boolean canPickUserType, User user, boolean isClient) {
        this.salonManager = salonManager;
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.authManager = authManager;
        this.canPickUserType = canPickUserType;
        this.isClient = isClient;
        this.user = user;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2", "[center, grow, align right][center, grow, align left]", "[center, grow]"));
        this.setTitle("Beauty salon - Registration");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());

        this.add(new JLabel("Username"));
        textUsername = new JTextField(20);
        this.add(textUsername);

        this.add(new JLabel("Password"));
        textPassword = new JTextField(20);
        this.add(textPassword);

        this.add(new JLabel("Name"));
        textName = new JTextField(20);
        this.add(textName);

        this.add(new JLabel("Surname"));
        textSurname = new JTextField(20);
        this.add(textSurname);

        this.add(new JLabel("Gender"));
        textGender = new JTextField(20);
        this.add(textGender);

        this.add(new JLabel("Phone number"));

        textPhone = new JTextField(20);
        this.add(textPhone);

        this.add(new JLabel("Address"));
        textAddress = new JTextField(20);
        this.add(textAddress);

        if (canPickUserType) {
            comboBoxUserType = new JComboBox<>();
            comboBoxUserType.addItem("Client");
            comboBoxUserType.addItem("Beautician");
            comboBoxUserType.addItem("Receptionist");
            comboBoxUserType.addItem("Manager");

            this.add(comboBoxUserType, "span, center");

            labelExperience = new JLabel("Years of experience");
            this.add(labelExperience);

            textExperience = new JTextField(20);
            this.add(textExperience);

            labelQualificationLevel = new JLabel("Qualification level");
            this.add(labelQualificationLevel);

            textQualificationLevel = new JTextField(20);
            this.add(textQualificationLevel);

            labelSalary = new JLabel("Salary");
            this.add(labelSalary);

            textSalary = new JTextField(20);
            this.add(textSalary);

            if (user != null) {
                if (user instanceof Client) {
                    comboBoxUserType.setSelectedIndex(0);
                    toggleEmployeeFields(false);
                } else {
                    toggleEmployeeFields(true);

                    if (user instanceof Beautician) {
                        comboBoxUserType.setSelectedIndex(1);
                    } else if (user instanceof Receptionist) {
                        comboBoxUserType.setSelectedIndex(2);
                    } else {
                        comboBoxUserType.setSelectedIndex(3);
                    }
                }
            }
        }

        if (user != null) {
            textUsername.setText(user.getUsername());
            textPassword.setText(user.getPassword());
            textName.setText(user.getName());
            textSurname.setText(user.getSurname());
            textGender.setText(user.getGender());
            textPhone.setText(user.getPhoneNumber());
            textAddress.setText(user.getAddress());

            if (user instanceof Employee) {
                textQualificationLevel.setText(String.valueOf(((Employee) user).getQualificationLevel()));
                textExperience.setText(String.valueOf(((Employee) user).getYearsOfExperience()));
                textSalary.setText(String.valueOf(((Employee) user).getMonthlySalary()));
            }
        }

        buttonRegister = new JButton((user == null) ? "Register" : "Save");
        this.add(buttonRegister, "span, center");

        buttonBack = new JButton("Back");
        this.add(buttonBack, "span, center");
    }

    private void initialiseListeners() {
        if (canPickUserType) {
            comboBoxUserType.addActionListener(e -> toggleEmployeeFields(comboBoxUserType.getSelectedIndex() > 0));
        }

        buttonRegister.addActionListener(e -> {
            if (!canPickUserType) {
                Client client = new Client(textUsername.getText(), textPassword.getText(), textName.getText(), textSurname.getText(), textGender.getText(), textPhone.getText(), textAddress.getText());
                userManager.addUser(client);
                this.dispose();

                if (isClient) {
                    ClientFrame clientFrame = new ClientFrame(salonManager, treatmentManager, userManager, authManager, textUsername.getText(), client);
                    clientFrame.setVisible(true);
                }
                return;
            }
            User newUser = user;
            switch (comboBoxUserType.getSelectedIndex()) {
                case 0:
                    newUser = new Client(textUsername.getText(), textPassword.getText(), textName.getText(), textSurname.getText(), textGender.getText(), textPhone.getText(), textAddress.getText());
                    break;
                case 1:
                    newUser = new Beautician(textUsername.getText(), textPassword.getText(), textName.getText(), textSurname.getText(), textGender.getText(), textPhone.getText(), textAddress.getText(), Byte.parseByte(textQualificationLevel.getText()), Byte.parseByte(textExperience.getText()), Float.parseFloat(textSalary.getText()));
                    break;
                case 2:
                    newUser = new Receptionist(textUsername.getText(), textPassword.getText(), textName.getText(), textSurname.getText(), textGender.getText(), textPhone.getText(), textAddress.getText(), Byte.parseByte(textQualificationLevel.getText()), Byte.parseByte(textExperience.getText()), Float.parseFloat(textSalary.getText()));
                    break;
                case 3:
                    newUser = new Manager(textUsername.getText(), textPassword.getText(), textName.getText(), textSurname.getText(), textGender.getText(), textPhone.getText(), textAddress.getText(), Byte.parseByte(textQualificationLevel.getText()), Byte.parseByte(textExperience.getText()), Float.parseFloat(textSalary.getText()));
                    break;
            }

            if (user == null) {
                userManager.addUser(newUser);
            } else {
                newUser.setId(user.getId());
                userManager.updateUser(newUser);
            }

            this.dispose();
        });

        buttonBack.addActionListener(e -> this.dispose());
    }

    private void toggleEmployeeFields(boolean value) {
        labelExperience.setVisible(value);
        textExperience.setVisible(value);
        labelQualificationLevel.setVisible(value);
        textQualificationLevel.setVisible(value);
        labelSalary.setVisible(value);
        textSalary.setVisible(value);
    }
}
