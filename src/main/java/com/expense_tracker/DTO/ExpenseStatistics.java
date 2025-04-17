package com.expense_tracker.DTO;

import lombok.Data;
import java.util.Map;

@Data
public class ExpenseStatistics {
    private double totalExpenses;
    private double averageExpense;
    private Map<String, Double> expensesByCategory;
    private double highestExpense;
    private double lowestExpense;
} 