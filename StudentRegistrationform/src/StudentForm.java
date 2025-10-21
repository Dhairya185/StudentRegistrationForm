import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentForm extends JFrame implements ActionListener {

    private JTextField nameField, emailField, contactField;
    private JComboBox<String> courseBox;
    private JRadioButton maleBtn, femaleBtn;
    private JButton submitBtn, clearBtn, exitBtn;
    private ButtonGroup genderGroup;

    public StudentForm() {
        setTitle("Student Registration Form");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 2, 10, 10));

        // Labels and Fields
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel genderLabel = new JLabel("Gender:");
        maleBtn = new JRadioButton("Male");
        femaleBtn = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleBtn);
        genderGroup.add(femaleBtn);

        JPanel genderPanel = new JPanel();
        genderPanel.add(maleBtn);
        genderPanel.add(femaleBtn);

        JLabel courseLabel = new JLabel("Course:");
        String[] courses = {"B.Tech", "BCA", "B.Sc", "MCA", "MBA"};
        courseBox = new JComboBox<>(courses);

        JLabel contactLabel = new JLabel("Contact:");
        contactField = new JTextField();

        submitBtn = new JButton("Submit");
        clearBtn = new JButton("Clear");
        exitBtn = new JButton("Exit");

        submitBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        exitBtn.addActionListener(this);

        // Adding components to frame
        add(nameLabel); add(nameField);
        add(emailLabel); add(emailField);
        add(genderLabel); add(genderPanel);
        add(courseLabel); add(courseBox);
        add(contactLabel); add(contactField);
        add(submitBtn); add(clearBtn);
        add(new JLabel()); add(exitBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitBtn) {
            handleSubmit();
        } else if (e.getSource() == clearBtn) {
            clearForm();
        } else if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }

    private void handleSubmit() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String gender = maleBtn.isSelected() ? "Male" : (femaleBtn.isSelected() ? "Female" : "");
        String course = (String) courseBox.getSelectedItem();
        String contact = contactField.getText().trim();

        // Input Validation
        if (name.isEmpty() || email.isEmpty() || gender.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!contact.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Contact number must be 10 digits!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save to Database
        String query = "INSERT INTO students (name, email, gender, course, contact) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, gender);
            ps.setString(4, course);
            ps.setString(5, contact);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        contactField.setText("");
        genderGroup.clearSelection();
        courseBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentForm::new);
    }
}
