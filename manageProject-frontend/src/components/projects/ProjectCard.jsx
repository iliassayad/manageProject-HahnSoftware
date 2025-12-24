import {
  Calendar,
  CheckCircle2,
  ListTodo,
  Trash2,
  Edit2
} from "lucide-react";

const ProjectCard = ({ project, onClick, onDelete, onEdit }) => {

  const handleDelete = (e) => {
    e.stopPropagation();
    onDelete();
  };

  const handleEdit = (e) => {
    e.stopPropagation();
    onEdit();
  };

  const formattedDate = new Date(project.createdAt).toLocaleDateString();

  return (
    <div
      onClick={onClick}
      className="bg-white rounded-xl shadow-sm hover:shadow-lg border border-gray-200 p-6 cursor-pointer transition-all duration-200 hover:-translate-y-1"
    >
      {/* Header */}
      <div className="flex justify-between items-start mb-4">
        <div className="flex-1">
          <h3 className="text-lg font-semibold text-gray-900 mb-1 line-clamp-1">
            {project.title}
          </h3>
          {project.description && (
            <p className="text-sm text-gray-600 line-clamp-2">
              {project.description}
            </p>
          )}
        </div>

        {/* Actions */}
        <div className="flex gap-1">
          <button
            onClick={handleEdit}
            className="p-2 text-gray-400 hover:text-indigo-600 hover:bg-indigo-50 rounded-lg transition"
            title="Edit project"
          >
            <Edit2 className="w-4 h-4" />
          </button>

          <button
            onClick={handleDelete}
            className="p-2 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition"
            title="Delete project"
          >
            <Trash2 className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Stats */}
      <div className="space-y-3 mb-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2 text-sm text-gray-600">
            <ListTodo className="w-4 h-4" />
            <span>Total Tasks</span>
          </div>
          <span className="font-semibold text-gray-900">
            {project.totalTasks}
          </span>
        </div>

        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2 text-sm text-gray-600">
            <CheckCircle2 className="w-4 h-4" />
            <span>Completed Tasks</span>
          </div>
          <span className="font-semibold text-green-600">
            {project.completedTasks}
          </span>
        </div>
      </div>

      {/* Progress */}
      <div className="mb-4">
        <div className="flex justify-between items-center mb-2">
          <span className="text-xs font-medium text-gray-600">Progress</span>
          <span className="text-xs font-bold text-indigo-600">
            {Math.round(project.completionPercentage)}%
          </span>
        </div>
        <div className="w-full bg-gray-200 rounded-full h-2 overflow-hidden">
          <div
            className="bg-gradient-to-r from-indigo-500 to-indigo-600 h-full rounded-full transition-all duration-500"
            style={{ width: `${project.completionPercentage}%` }}
          />
        </div>
      </div>

      {/* Created date */}
      <div className="flex items-center gap-2 text-xs text-gray-500 pt-3 border-t border-gray-100">
        <Calendar className="w-3.5 h-3.5" />
        <span>Created on {formattedDate}</span>
      </div>
    </div>
  );
};

export default ProjectCard;
