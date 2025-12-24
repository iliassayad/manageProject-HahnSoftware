import api from './api';

export const userService = {
    createUser: async (userData) => {
        const response = await api.post('/users/create', userData);
        return response.data;
    },
};
