import api from './api';

export const authService = {
    login: async (email, password) => {
        const response = await api.post('/auth/login', { email, password });
        if (response.data.token) {  
            localStorage.setItem('token', response.data.token);
             return { ...response.data, token: response.data.token };
        }
        return response.data;
    },
    logout: () => {
        localStorage.removeItem('token');
    },
    
     isAuthenticated: () => {
    return !!localStorage.getItem('token');
    },
    
    getToken: () => {
    return localStorage.getItem('token');
    }
}