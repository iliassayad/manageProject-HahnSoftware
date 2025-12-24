import { useState } from 'react';
import { ListTodo } from 'lucide-react';
import TaskItem from './TaskItem';

const TaskList = ({ tasks, onTaskUpdated, onTaskDeleted }) => {
  const [filter, setFilter] = useState('ALL');

  // Filter tasks by status
  const filteredTasks = tasks.filter(task => {
    if (filter === 'ALL') return true;
    return task.status === filter;
  });

  // Statistics
  const stats = {
    all: tasks.length,
    todo: tasks.filter(t => t.status === 'TODO').length,
    inProgress: tasks.filter(t => t.status === 'IN_PROGRESS').length,
    completed: tasks.filter(t => t.status === 'COMPLETED').length
  };

  const filterButtons = [
    { label: 'All', value: 'ALL', count: stats.all },
    { label: 'To Do', value: 'TODO', count: stats.todo },
    { label: 'In Progress', value: 'IN_PROGRESS', count: stats.inProgress },
    { label: 'Completed', value: 'COMPLETED', count: stats.completed }
  ];

  if (tasks.length === 0) {
    return (
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-12 text-center">
        <div className="bg-gray-100 w-20 h-20 rounded-full flex items-center justify-center mx-auto mb-4">
          <ListTodo className="w-10 h-10 text-gray-400" />
        </div>
        <h3 className="text-xl font-semibold text-gray-700 mb-2">
          No tasks yet
        </h3>
        <p className="text-gray-500">
          Start by creating your first task for this project
        </p>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
      {/* Header with filters */}
      <div className="border-b border-gray-200 p-6">
        <h2 className="text-xl font-bold text-gray-900 mb-4">
          Project Tasks
        </h2>
        <div className="flex flex-wrap gap-2">
          {filterButtons.map(btn => (
            <button
              key={btn.value}
              onClick={() => setFilter(btn.value)}
              className={`px-4 py-2 rounded-lg font-medium transition ${
                filter === btn.value
                  ? 'bg-indigo-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {btn.label}
              <span className={`ml-2 text-sm ${
                filter === btn.value ? 'text-indigo-200' : 'text-gray-500'
              }`}>
                ({btn.count})
              </span>
            </button>
          ))}
        </div>
      </div>

      {/* Task list */}
      <div className="divide-y divide-gray-200">
        {filteredTasks.length === 0 ? (
          <div className="p-8 text-center">
            <p className="text-gray-500">
              No tasks in this category
            </p>
          </div>
        ) : (
          filteredTasks.map(task => (
            <TaskItem
              key={task.id}
              task={task}
              onTaskUpdated={onTaskUpdated}
              onTaskDeleted={onTaskDeleted}
            />
          ))
        )}
      </div>
    </div>
  );
};

export default TaskList;
