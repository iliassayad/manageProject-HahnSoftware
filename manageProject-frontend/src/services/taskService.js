import api from './api';

export const taskService = {

  getTasksByProject: async (projectId) => {
    const response = await api.get(`/tasks/${projectId}`);
    return response.data;
  },

  getTaskById: async (taskId) => {
    const response = await api.get(`/tasks/task/${taskId}`);
    return response.data;
  },


  createTask: async (taskData) => {
    const response = await api.post('/tasks', taskData);
    return response.data;
  },

  updateTask: async (taskId, taskData) => {
    const response = await api.put(`/tasks/${taskId}`, taskData);
    return response.data;
  },

  updateTaskStatus: async (taskId, status) => {
    const response = await api.patch(`/tasks/${taskId}`, { status });
    return response.data;
  },

  deleteTask: async (taskId) => {
    await api.delete(`/tasks/${taskId}`);
  }
};