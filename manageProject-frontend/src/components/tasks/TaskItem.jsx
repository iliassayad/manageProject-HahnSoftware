import { useState } from 'react';
import { Calendar, Trash2, Edit2, ChevronDown, ChevronUp } from 'lucide-react';
import { taskService } from '../../services/taskService';
import EditTaskModal from './EditTaskModal';

const TaskItem = ({ task, onTaskUpdated, onTaskDeleted }) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [updatingStatus, setUpdatingStatus] = useState(false);

  const statusConfig = {
    TODO: {
      label: 'To Do',
      color: 'bg-gray-100 text-gray-700 border-gray-300',
      dotColor: 'bg-gray-500'
    },
    IN_PROGRESS: {
      label: 'In Progress',
      color: 'bg-blue-100 text-blue-700 border-blue-300',
      dotColor: 'bg-blue-500'
    },
    COMPLETED: {
      label: 'Completed',
      color: 'bg-green-100 text-green-700 border-green-300',
      dotColor: 'bg-green-500'
    },
    CANCELLED: {
      label: 'Cancelled',
      color: 'bg-red-100 text-red-700 border-red-300',
      dotColor: 'bg-red-500'
    }
  };

  const currentStatus = statusConfig[task.status];

  const handleStatusChange = async (newStatus) => {
    if (updatingStatus || newStatus === task.status) return;

    try {
      setUpdatingStatus(true);
      await taskService.updateTaskStatus(task.id, newStatus);
      await onTaskUpdated();
    } catch (err) {
      alert('Error updating task status');
      console.error(err);
    } finally {
      setUpdatingStatus(false);
    }
  };

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      await onTaskDeleted(task.id);
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      day: 'numeric',
      month: 'long',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  return (
    <>
      <div className="p-6 hover:bg-gray-50 transition">
        <div className="flex items-start gap-4">
          {/* Quick complete checkbox */}
          <button
            onClick={() => handleStatusChange(
              task.status === 'COMPLETED' ? 'TODO' : 'COMPLETED'
            )}
            disabled={updatingStatus}
            className="mt-1 flex-shrink-0"
          >
            <div className={`w-5 h-5 rounded border-2 transition ${
              task.status === 'COMPLETED'
                ? 'bg-green-500 border-green-500'
                : 'border-gray-300 hover:border-indigo-500'
            } flex items-center justify-center`}>
              {task.status === 'COMPLETED' && (
                <svg className="w-3 h-3 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M5 13l4 4L19 7" />
                </svg>
              )}
            </div>
          </button>

          {/* Main content */}
          <div className="flex-1 min-w-0">
            <div className="flex items-start justify-between gap-4">
              <div className="flex-1">
                <h3 className={`text-lg font-semibold ${
                  task.status === 'COMPLETED' 
                    ? 'text-gray-400 line-through' 
                    : 'text-gray-900'
                }`}>
                  {task.title}
                </h3>
                
                {task.description && (
                  <p className={`mt-1 text-sm ${
                    task.status === 'COMPLETED'
                      ? 'text-gray-400'
                      : 'text-gray-600'
                  } ${!isExpanded && 'line-clamp-2'}`}>
                    {task.description}
                  </p>
                )}

                {/* Creation date */}
                <div className="flex items-center gap-2 mt-2 text-xs text-gray-500">
                  <Calendar className="w-3.5 h-3.5" />
                  <span>Created on {formatDate(task.createdAt)}</span>
                </div>
              </div>

              {/* Status badge */}
              <div className="flex items-center gap-2">
                <span className={`px-3 py-1 rounded-full text-xs font-medium border ${currentStatus.color} flex items-center gap-1.5`}>
                  <span className={`w-1.5 h-1.5 rounded-full ${currentStatus.dotColor}`} />
                  {currentStatus.label}
                </span>
              </div>
            </div>

            {/* Actions and expand */}
            <div className="flex items-center gap-2 mt-4">
              {/* Change status dropdown */}
              <div className="relative">
                <select
                  value={task.status}
                  onChange={(e) => handleStatusChange(e.target.value)}
                  disabled={updatingStatus}
                  className="text-sm border border-gray-300 rounded-lg px-3 py-1.5 pr-8 appearance-none bg-white hover:border-indigo-500 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 outline-none transition cursor-pointer disabled:opacity-50"
                >
                  <option value="TODO">To Do</option>
                  <option value="IN_PROGRESS">In Progress</option>
                  <option value="COMPLETED">Completed</option>
                  <option value="CANCELLED">Cancelled</option>
                </select>
              </div>

              {/* Edit button */}
              <button
                onClick={() => setIsEditModalOpen(true)}
                className="p-1.5 text-gray-500 hover:text-indigo-600 hover:bg-indigo-50 rounded transition"
                title="Edit"
              >
                <Edit2 className="w-4 h-4" />
              </button>

              {/* Delete button */}
              <button
                onClick={handleDelete}
                className="p-1.5 text-gray-500 hover:text-red-600 hover:bg-red-50 rounded transition"
                title="Delete"
              >
                <Trash2 className="w-4 h-4" />
              </button>

              {/* Expand/collapse button if description exists */}
              {task.description && (
                <button
                  onClick={() => setIsExpanded(!isExpanded)}
                  className="ml-auto p-1.5 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded transition"
                >
                  {isExpanded ? (
                    <ChevronUp className="w-4 h-4" />
                  ) : (
                    <ChevronDown className="w-4 h-4" />
                  )}
                </button>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Edit modal */}
      <EditTaskModal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        onTaskUpdated={onTaskUpdated}
        task={task}
      />
    </>
  );
};

export default TaskItem;
