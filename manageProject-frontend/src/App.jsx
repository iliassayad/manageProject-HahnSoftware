import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/auth/ProtectedRoute';
import LoginPage from './pages/auth/LoginPage';
import ProjectsPage from './pages/projects/ProjectsPage';
import ProjectDetailPage from './pages/projects/ProjectDetailPage';
import Layout from './components/layout/Layout';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Route publique - Login */}
          <Route path="/login" element={<LoginPage />} />

          {/* Routes protégées avec Layout */}
          <Route
            path="/*"
            element={
              <ProtectedRoute>
                <Layout>
                  <Routes>
                    {/* Redirection de la racine vers /projects */}
                    <Route path="/" element={<Navigate to="/projects" replace />} />
                    
                    {/* Liste des projets */}
                    <Route path="/projects" element={<ProjectsPage />} />
                    
                    {/* Détail d'un projet avec ses tâches */}
                    <Route path="/projects/:id" element={<ProjectDetailPage />} />
                  </Routes>
                </Layout>
              </ProtectedRoute>
            }
          />

          {/* Route 404 */}
          <Route path="*" element={<Navigate to="/projects" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;