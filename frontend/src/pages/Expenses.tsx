import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  Box,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  CircularProgress,
  Alert,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
} from '@mui/material';
import { Edit as EditIcon, Delete as DeleteIcon } from '@mui/icons-material';
import { expenseService, categoryService } from '../services/api';
import { authService } from '../services/auth';

interface ExpenseFormData {
  exp_name: string;
  exp_amt: number;
  exp_created: string;
  user_id: number;
  category_id: number;
}

interface Category {
  category_id: number;
  category_name: string;
}

interface Expense {
  exp_id: number;
  exp_name: string;
  exp_amt: number;
  exp_created: string;
  category: Category;
}

const formatDateForBackend = (dateStr: string) => {
  try {
    // If the date is already in YYYY-MM-DD format, return it
    if (/^\d{4}-\d{2}-\d{2}$/.test(dateStr)) {
      return dateStr;
    }
    
    // Handle DD/MM/YYYY format
    if (dateStr.includes('/')) {
      const [day, month, year] = dateStr.split('/');
      // Ensure month and day are two digits
      const formattedMonth = month.padStart(2, '0');
      const formattedDay = day.padStart(2, '0');
      return `${year}-${formattedMonth}-${formattedDay}`;
    }

    // If neither format matches, return today's date
    return new Date().toISOString().split('T')[0];
  } catch (error) {
    console.error('Error formatting date:', error);
    return new Date().toISOString().split('T')[0];
  }
};

const Expenses = () => {
  const [open, setOpen] = useState(false);
  const [editingExpense, setEditingExpense] = useState<Expense | null>(null);
  const [formData, setFormData] = useState<ExpenseFormData>({
    exp_name: '',
    exp_amt: 0,
    exp_created: new Date().toISOString().split('T')[0],
    user_id: authService.getCurrentUserId() || 0,
    category_id: 0,
  });
  const [error, setError] = useState('');

  const queryClient = useQueryClient();

  const { data: expenses = [], isLoading: expensesLoading } = useQuery({
    queryKey: ['expenses'],
    queryFn: expenseService.getAllExpenses,
  });

  const { data: categories = [], isLoading: categoriesLoading } = useQuery({
    queryKey: ['categories'],
    queryFn: categoryService.getAllCategories,
  });

  const createMutation = useMutation({
    mutationFn: (data: ExpenseFormData) => {
      console.log('Creating expense with data:', data);
      return expenseService.createExpense(data);
    },
    onSuccess: (response) => {
      console.log('Expense created successfully:', response);
      queryClient.invalidateQueries({ queryKey: ['expenses'] });
      handleClose();
    },
    onError: (error: any) => {
      console.error('Create expense error:', error);
      setError(error.response?.data?.message || 'Failed to create expense');
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: ExpenseFormData }) => {
      console.log('Updating expense with data:', { id, data });
      return expenseService.updateExpense(id, data);
    },
    onSuccess: (response) => {
      console.log('Expense updated successfully:', response);
      queryClient.invalidateQueries({ queryKey: ['expenses'] });
      handleClose();
    },
    onError: (error: any) => {
      console.error('Update expense error:', error);
      setError(error.response?.data?.message || 'Failed to update expense');
    },
  });

  const deleteMutation = useMutation({
    mutationFn: expenseService.deleteExpense,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['expenses'] });
    },
    onError: (error: any) => {
      console.error('Delete expense error:', error);
      setError(error.response?.data?.message || 'Failed to delete expense');
    },
  });

  const handleOpen = (expense?: Expense) => {
    const currentUserId = authService.getCurrentUserId();
    if (!currentUserId) {
      setError('User not logged in');
      return;
    }

    if (expense) {
      setEditingExpense(expense);
      setFormData({
        exp_name: expense.exp_name,
        exp_amt: expense.exp_amt,
        exp_created: formatDateForBackend(expense.exp_created),
        user_id: currentUserId,
        category_id: expense.category.category_id,
      });
    } else {
      setEditingExpense(null);
      setFormData({
        exp_name: '',
        exp_amt: 0,
        exp_created: new Date().toISOString().split('T')[0],
        user_id: currentUserId,
        category_id: 0,
      });
    }
    setError('');
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setEditingExpense(null);
    setFormData({
      exp_name: '',
      exp_amt: 0,
      exp_created: new Date().toISOString().split('T')[0],
      user_id: authService.getCurrentUserId() || 0,
      category_id: 0,
    });
    setError('');
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    const currentUserId = authService.getCurrentUserId();
    if (!currentUserId) {
      setError('User not logged in');
      return;
    }

    // Validate required fields
    if (!formData.exp_name || !formData.exp_amt || !formData.category_id) {
      setError('Please fill in all required fields');
      return;
    }

    // Format the data for submission
    const submissionData = {
      ...formData,
      exp_amt: Number(formData.exp_amt),
      user_id: currentUserId,
      category_id: Number(formData.category_id),
      exp_created: formatDateForBackend(formData.exp_created)
    };

    console.log('Submitting form data:', submissionData);
    
    try {
      if (editingExpense) {
        await updateMutation.mutateAsync({ id: editingExpense.exp_id, data: submissionData });
      } else {
        await createMutation.mutateAsync(submissionData);
      }
    } catch (err: any) {
      console.error('Submit error:', err);
      setError(err.response?.data?.message || 'Failed to submit expense');
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | { name?: string; value: unknown }>) => {
    const { name, value } = e.target;
    console.log('Form field change:', { name, value });
    
    if (name === 'category_id') {
      setFormData((prev) => ({
        ...prev,
        category_id: Number(value),
      }));
    } else if (name === 'exp_amt') {
      setFormData((prev) => ({
        ...prev,
        exp_amt: Number(value),
      }));
    } else if (name === 'exp_created') {
      setFormData((prev) => ({
        ...prev,
        exp_created: value as string,
      }));
    } else {
      setFormData((prev) => ({
        ...prev,
        [name as string]: value,
      }));
    }
  };

  if (expensesLoading || categoriesLoading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4">Expenses</Typography>
        <Button variant="contained" color="primary" onClick={() => handleOpen()}>
          Add Expense
        </Button>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Description</TableCell>
              <TableCell>Amount</TableCell>
              <TableCell>Category</TableCell>
              <TableCell>Date</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {Array.isArray(expenses) && expenses.map((expense: Expense) => (
              <TableRow key={expense.exp_id}>
                <TableCell>{expense.exp_name}</TableCell>
                <TableCell>${expense.exp_amt.toFixed(2)}</TableCell>
                <TableCell>{expense.category.category_name}</TableCell>
                <TableCell>{new Date(expense.exp_created).toLocaleDateString()}</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleOpen(expense)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton onClick={() => deleteMutation.mutate(expense.exp_id)}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>
          {editingExpense ? 'Edit Expense' : 'Add New Expense'}
        </DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <TextField
              margin="dense"
              name="exp_name"
              label="Description"
              fullWidth
              value={formData.exp_name}
              onChange={handleChange}
              required
            />
            <TextField
              margin="dense"
              name="exp_amt"
              label="Amount"
              type="number"
              fullWidth
              value={formData.exp_amt}
              onChange={handleChange}
              required
              inputProps={{ min: 0, step: "0.01" }}
            />
            <TextField
              margin="dense"
              name="exp_created"
              label="Date"
              type="date"
              fullWidth
              value={formData.exp_created}
              onChange={handleChange}
              InputLabelProps={{ shrink: true }}
              required
              inputProps={{
                max: new Date().toISOString().split('T')[0]
              }}
            />
            <FormControl fullWidth margin="dense" required>
              <InputLabel>Category</InputLabel>
              <Select
                name="category_id"
                value={formData.category_id || ''}
                onChange={handleChange}
                required
              >
                {categories.map((category: Category) => (
                  <MenuItem key={category.category_id} value={category.category_id}>
                    {category.category_name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClose}>Cancel</Button>
            <Button 
              type="submit" 
              variant="contained" 
              color="primary"
              disabled={!formData.exp_name || formData.exp_amt <= 0 || !formData.category_id}
            >
              {editingExpense ? 'Update' : 'Add'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default Expenses; 