/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galoisdialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GaloisDialog extends JDialog implements ActionListener {
    private Label degreeLabel, aLabel, bLabel, cLabel, dLabel, resultLabel;
    private Choice degreeChoice;
    private TextField aTextField, bTextField, cTextField, dTextField, resultTextField;
    private Button calculateButton, clearButton;
    private Double a, b, c, d;

    public GaloisDialog(JFrame parent) {
        super(parent, "GALOIS", true);
        
        // Create the degree label and combo box
        degreeLabel = new Label("Degree:");
        degreeChoice = new Choice();
        degreeChoice.add("Linear (ax + b = 0)");
        degreeChoice.add("Quadratic (ax^2 + bx + c = 0)");
        degreeChoice.add("Cubic (ax^3 + bx^2 + cx + d = 0)");
        degreeChoice.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
            if (e.getStateChange() == ItemEvent.SELECTED) {
                } }});

        // Create the input labels and fields
        aLabel = new Label("a:");
        bLabel = new Label("b:");
        cLabel = new Label("c:");
        dLabel = new Label("d:");
        aTextField = new TextField(10);
        bTextField = new TextField(10);
        cTextField = new TextField(10);
        dTextField = new TextField(10);
        
        // Create the result label and field
        resultLabel = new Label("Result:");
        resultTextField = new TextField(20);
        resultTextField.setEditable(true);

        // Create the calculate and clear buttons
        calculateButton = new Button("Calculate");
        calculateButton.addActionListener(this);
        clearButton = new Button("Clear");
        clearButton.addActionListener(this);

        // Create the input panel and add the components
        Panel inputPanel = new Panel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(aLabel);
        inputPanel.add(aTextField);
        inputPanel.add(bLabel);
        inputPanel.add(bTextField);
        inputPanel.add(cLabel);
        inputPanel.add(cTextField);
        inputPanel.add(dLabel);
        inputPanel.add(dTextField);

        // Create the result panel and add the components
        Panel resultPanel = new Panel();
        resultPanel.setLayout(new FlowLayout());
        resultPanel.add(resultLabel);
        resultPanel.add(resultTextField);

        // Create the button panel and add the components
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);

        // Add the components to the dialog box
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(degreeLabel, BorderLayout.NORTH);
        contentPane.add(degreeChoice, BorderLayout.NORTH);
        contentPane.add(inputPanel, BorderLayout.CENTER);
        contentPane.add(resultPanel, BorderLayout.EAST);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set the dialog box size and make it visible
        setSize(900, 200);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == calculateButton) {
            // Validate input fields and calculate the result
            String errorMessage = validateInput();
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            calculateResult();
        } else if (source == clearButton) {
            // Clear all input fields and the result field
            aTextField.setText("");
            bTextField.setText("");
            cTextField.setText("");
            dTextField.setText("");
            resultTextField.setText("");
            a = null;
            b = null;
            c = null;
            d = null;
            
        }
    }

    private String validateInput() {
        String selectedDegree = (String) degreeChoice.getSelectedItem();
        String errorMessage = null;
        switch (selectedDegree) {
            case "Linear (ax + b = 0)":
                if (aTextField.getText().isEmpty() || bTextField.getText().isEmpty()) {
                    errorMessage = "Please fill in all fields";
                }   break;
            case "Quadratic (ax^2 + bx + c = 0)":
                if (aTextField.getText().isEmpty() || bTextField.getText().isEmpty() || cTextField.getText().isEmpty()) {
                    errorMessage = "Please fill in all fields";
                }   break;
            case "Cubic (ax^3 + bx^2 + cx + d = 0)":
                if (aTextField.getText().isEmpty() || bTextField.getText().isEmpty() || cTextField.getText().isEmpty() || dTextField.getText().isEmpty()) {
                    errorMessage = "Please fill in all fields";
                }   break;
            default:
                break;
        }
        if (errorMessage == null) {
            try {
                a = Double.parseDouble(aTextField.getText());
                b = Double.parseDouble(bTextField.getText());
                if (selectedDegree.equals("Quadratic (ax^2 + bx + c = 0)")) {
                    c = Double.parseDouble(cTextField.getText());
                }
                if (selectedDegree.equals("Cubic (ax^3 + bx^2 + cx + d = 0)")) {
                    c = Double.parseDouble(cTextField.getText());
                    d = Double.parseDouble(dTextField.getText());
                }
            } catch (NumberFormatException e) {
                errorMessage = "Please enter valid numbers";
            }
        }
        return errorMessage;
    }

    private void calculateResult() {
        String selectedDegree = (String) degreeChoice.getSelectedItem();
        switch (selectedDegree) {
            case "Linear (ax + b = 0)":
                resultTextField.setText(String.format("%.2f", -b/a));
                break;
            case "Quadratic (ax^2 + bx + c = 0)":
                //D is discriminant of the quadratic.
                double D = b*b - 4*a*c;
                if (D < 0) {
                    double Re = -b/(2*a);
                    double Im = Math.sqrt(-D) / (2*a);
                    resultTextField.setText(String.format("%.2f + i%.2f, %.2f - i%.2f", Re, Im, Re, Im));
                } else if (D == 0) {
                    resultTextField.setText(String.format("%.2f", -b/(2*a)));
                } else {
                    double root1 = (-b + Math.sqrt(D)) / (2*a);
                    double root2 = (-b - Math.sqrt(D)) / (2*a);
                    resultTextField.setText(String.format("%.2f, %.2f", root1, root2));
                }   break;
            case "Cubic (ax^3 + bx^2 + cx + d = 0)":
    //Depression of cubic.
    double p = (3*a*c - b*b)/(3*a*a);
    double q = (2*b*b*b - 9*a*b*c + 27*a*a*d)/(27*a*a*a);
    //D1 = Discriminant.
    double D1 = (4*p*p*p + 27*q*q)/108;
    if(D1 > 0){
        //Cardan's solution.
        double u = Math.cbrt(-q/2 + Math.sqrt(D1));
        double v = Math.cbrt(-q/2 - Math.sqrt(D1));
        double t = u+v;
        double x = t-(b/(3*a));
        resultTextField.setText(String.format("%.2f, %.2f + i%.2f, %.2f - i%.2f", x, t*(-0.5) - (b/(3*a)), (u-v)*0.866, t*(-0.5) - (b/(3*a)), (u-v)*0.866 ));
    }
    else if(D1 == 0){
        double u = Math.cbrt(-q/2);
        double t = 2*u - (b/(3*a));
        resultTextField.setText(String.format("%.2f", t));
    }
    else {
        //Irreducible case.
        double P = Math.sqrt(-p/3);
        double Q = 1.5*(q/p)*(1/P);
        double theta = Math.acos(Q);
        double t0 = 2*P*Math.cos(0.333*theta) - b/(3*a);
        double t1 = 2*P*Math.cos(0.333*theta - 2.0944) - b/(3*a);
        double t2 = 2*P*Math.cos(0.333*theta - 4.1888) - b/(3*a);
        resultTextField.setText(String.format("%.2f, %.2f, %.2f", t0, t1, t2));
    }
    break;
default:
    break;

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GaloisDialog dialog = new GaloisDialog(frame);        
    }
}