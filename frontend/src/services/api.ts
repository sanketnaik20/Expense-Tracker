import axios from 'axios';

const API_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if it exists
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  console.log('Token from localStorage:', token);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
    console.log('Request headers:', config.headers);
  }
  return config;
});

// Add response interceptor for debugging
api.interceptors.response.use(
  (response) => {
    console.log('Response:', response);
    return response;
  },
  (error) => {
    console.error('API Error:', error.response || error);
    return Promise.reject(error);
  }
);

const formatDate = (dateStr: string) => {
  try {
    // If the date is already in YYYY-MM-DD format, return it
    if (/^\d{4}-\d{2}-\d{2}$/.test(dateStr)) {
      return dateStr;
    }
    
    // Convert any date string to YYYY-MM-DD
    const date = new Date(dateStr);
    return date.toISOString().split('T')[0];
  } catch (error) {
    console.error('Error formatting date:', error);
    return new Date().toISOString().split('T')[0];
  }
};

export const authService = {
  login: async (username: string, password: string) => {
    try {
      const response = await api.post('/auth/login', { name: username, password });
      console.log('Login response:', response.data);
      if (response.data.token) {
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('username', response.data.username);
        
        // Get user details after successful login
        const userResponse = await api.get(`/users/by-name/${response.data.username}`);
        console.log('User details:', userResponse.data);
        if (userResponse.data && userResponse.data.id) {
          localStorage.setItem('userId', userResponse.data.id.toString());
        }
      }
      return response.data;
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  },
  register: async (userData: { name: string; email: string; password: string }) => {
    const response = await api.post('/auth/register', userData);
    return response.data;
  },
  getCurrentUserId: () => {
    const userId = localStorage.getItem('userId');
    console.log('Current user ID:', userId);
    return userId ? Number(userId) : null;
  }
};

export const expenseService = {
  getAllExpenses: async () => {
    try {
      const response = await api.get('/api/expenses');
      console.log('Expenses response:', response.data);
      // Format dates in the response
      const formattedExpenses = response.data.map((expense: any) => ({
        ...expense,
        exp_created: formatDate(expense.exp_created)
      }));
      return formattedExpenses;
    } catch (error) {
      console.error('Error fetching expenses:', error);
      console.error('Response:', error.response);
      return [];
    }
  },
  createExpense: async (expenseData: any) => {
    console.log('Creating expense with data:', expenseData);
    try {
      // Ensure all required fields are present and properly formatted
      if (!expenseData.exp_name || !expenseData.exp_amt || !expenseData.category_id || !expenseData.user_id) {
        throw new Error('Missing required fields');
      }

      // Ensure date is in correct format
      const formattedData = {
        ...expenseData,
        exp_created: formatDate(expenseData.exp_created)
      };

      console.log('Formatted expense data:', formattedData);
      const response = await api.post('/api/add', formattedData);
      console.log('Create expense response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error creating expense:', error);
      console.error('Response:', error.response);
      throw error;
    }
  },
  updateExpense: async (id: number, expenseData: any) => {
    try {
      // Ensure all required fields are present and properly formatted
      if (!expenseData.exp_name || !expenseData.exp_amt || !expenseData.category_id || !expenseData.user_id) {
        throw new Error('Missing required fields');
      }

      // Ensure date is in correct format
      const formattedData = {
        ...expenseData,
        exp_created: formatDate(expenseData.exp_created)
      };

      console.log('Updating expense with formatted data:', { id, data: formattedData });
      const response = await api.put(`/api/update?id=${id}`, formattedData);
      console.log('Update expense response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error updating expense:', error);
      console.error('Response:', error.response);
      throw error;
    }
  },
  deleteExpense: async (id: number) => {
    try {
      const response = await api.delete(`/api/delete?id=${id}`);
      console.log('Delete expense response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error deleting expense:', error);
      console.error('Response:', error.response);
      throw error;
    }
  },
  getExpenseStatistics: async () => {
    try {
      const response = await api.get('/api/statistics');
      console.log('Statistics response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error fetching statistics:', error);
      console.error('Response:', error.response);
      return null;
    }
  },
};

export const categoryService = {
  getAllCategories: async () => {
    try {
      const response = await api.get('/categories/');
      console.log('Categories response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error fetching categories:', error);
      console.error('Response:', error.response);
      return [];
    }
  },
  createCategory: async (categoryData: any) => {
    console.log('Creating category with data:', categoryData);
    try {
      const response = await api.post('/categories/create', categoryData);
      console.log('Create category response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error creating category:', error);
      throw error;
    }
  },
  updateCategory: async (id: number, categoryData: any) => {
    try {
      const response = await api.put(`/categories/${id}`, categoryData);
      console.log('Update category response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error updating category:', error);
      throw error;
    }
  },
  deleteCategory: async (id: number) => {
    try {
      const response = await api.delete(`/categories/${id}`);
      console.log('Delete category response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error deleting category:', error);
      throw error;
    }
  },
};

export default api; 