
package com.example.expensetracker;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView totalSpending;
    private LinearLayout expenseList;
    private TextView emptyMessage;
    private double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalSpending = findViewById(R.id.totalSpending);
        expenseList = findViewById(R.id.expenseList);
        emptyMessage = findViewById(R.id.emptyMessage);
        Button addExpenseButton =
                findViewById(R.id.addExpenseButton);

        addExpenseButton.setOnClickListener(v -> showAddExpenseDialog());
    }

    private void showAddExpenseDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 10);

        EditText nameInput = new EditText(this);
        nameInput.setHint("Expense name");
        layout.addView(nameInput);

        EditText amountInput = new EditText(this);
        amountInput.setHint("Amount ($)");
        amountInput.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
        );
        layout.addView(amountInput);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Expense")
                .setView(layout)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {
                        String name = nameInput.getText().toString().trim();
                        String amountText =
                                amountInput.getText().toString().trim();

                        if (name.isEmpty() || amountText.isEmpty()) {
                            Toast.makeText(this,
                                    "Please fill in both fields",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            double amount = Double.parseDouble(amountText);

                            if (!Double.isFinite(amount) || amount <= 0
                                    || amount > 1000000000) {
                                Toast.makeText(this,
                                        "Enter a valid amount",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            addExpense(name, amount);
                            dialog.dismiss();

                        } catch (NumberFormatException e) {
                            Toast.makeText(this,
                                    "Enter a valid number",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        dialog.show();
    }

    private void addExpense(String name, double amount) {
        total += amount;

        totalSpending.setText(
                String.format(Locale.US, "$%.2f", total)
        );

        emptyMessage.setVisibility(View.GONE);

        TextView expense = new TextView(this);
        expense.setText(
                name + " - " +
                        String.format(Locale.US, "$%.2f", amount)
        );
        expense.setTextSize(18);
        expense.setPadding(0, 16, 0, 16);

        expenseList.addView(expense);
    }
}
