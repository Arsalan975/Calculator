package com.example.calcu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    public TextView display;
    private String currentDisplay = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        display = findViewById(R.id.textDis);

        Button btn0 = findViewById(R.id.button_0);
        Button btn00 = findViewById(R.id.button_00);
        Button btn1 = findViewById(R.id.button_1);
        Button btn2 = findViewById(R.id.button_2);
        Button btn3 = findViewById(R.id.button_3);
        Button btn4 = findViewById(R.id.button_4);
        Button btn5 = findViewById(R.id.button_5);
        Button btn6 = findViewById(R.id.button_6);
        Button btn7 = findViewById(R.id.button_7);
        Button btn8 = findViewById(R.id.button_8);
        Button btn9 = findViewById(R.id.button_9);
        Button btnDot = findViewById(R.id.button_dot);
        Button btnEqual = findViewById(R.id.button_equal);
        Button btnPlus = findViewById(R.id.button_plus);
        Button btnMinus = findViewById(R.id.button_minus);
        Button btnMultiply = findViewById(R.id.button_multiply);
        Button btnDivide = findViewById(R.id.button_divide);
        Button btnAC = findViewById(R.id.button_ac);
        Button btnC = findViewById(R.id.button_c);

        View.OnClickListener numberClickListener = v -> {
            Button button = (Button) v;
            currentDisplay += button.getText().toString();
            display.setText(currentDisplay);
        };

        btn0.setOnClickListener(numberClickListener);
        btn00.setOnClickListener(numberClickListener);
        btn1.setOnClickListener(numberClickListener);
        btn2.setOnClickListener(numberClickListener);
        btn3.setOnClickListener(numberClickListener);
        btn4.setOnClickListener(numberClickListener);
        btn5.setOnClickListener(numberClickListener);
        btn6.setOnClickListener(numberClickListener);
        btn7.setOnClickListener(numberClickListener);
        btn8.setOnClickListener(numberClickListener);
        btn9.setOnClickListener(numberClickListener);
        btnDot.setOnClickListener(numberClickListener);

        btnPlus.setOnClickListener(v -> appendOperator("+"));
        btnMinus.setOnClickListener(v -> appendOperator("-"));
        btnMultiply.setOnClickListener(v -> appendOperator("*"));
        btnDivide.setOnClickListener(v -> appendOperator("/"));

        btnEqual.setOnClickListener(v -> calculateResult());
        btnAC.setOnClickListener(v -> clearDisplay());

        btnC.setOnClickListener(v -> {
            if (currentDisplay.length() > 0) {
                currentDisplay = currentDisplay.substring(0, currentDisplay.length() - 1);
                display.setText(currentDisplay.isEmpty() ? "0" : currentDisplay);
            }
        });
    }

    private void appendOperator(String op) {
        if (!currentDisplay.equals("")) {
            currentDisplay += " " + op + " ";
            display.setText(currentDisplay);
        }
    }

    private void calculateResult() {
        if (!currentDisplay.equals("")) {
            double result = evaluate(currentDisplay);
            display.setText(currentDisplay + " = " + result);
            currentDisplay = String.valueOf(result);
        }
    }

    private void clearDisplay() {
        currentDisplay = "";
        display.setText("0");
    }

    private double evaluate(String expression) {
        String[] tokens = expression.split(" ");
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (isNumeric(token)) {
                values.push(Double.parseDouble(token));
            } else if (token.equals("(")) {
                ops.push('(');
            } else if (token.equals(")")) {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            } else if (isOperator(token.charAt(0))) {
                while (!ops.isEmpty() && hasPrecedence(token.charAt(0), ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(token.charAt(0));
            }
        }

        while (!ops.isEmpty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(char op) {
        return op == '+' || op == '-' || op == '*' || op == '/';
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }
}
