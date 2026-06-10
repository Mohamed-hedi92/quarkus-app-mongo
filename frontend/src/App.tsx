import { useState, useEffect } from 'react';
import type { Movie, MovieCreate } from './types/Movie';
import { getMovieId } from './types/Movie';
import { fetchMovies, createMovie, updateMovie, deleteMovie } from './services/movieApi';
import MovieList from './components/MovieList';
import MovieForm from './components/MovieForm';
import SearchBar from './components/SearchBar';
import './App.css';

function App() {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [editingMovie, setEditingMovie] = useState<Movie | null>(null);
  const [search, setSearch] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    loadMovies();
  }, []);

  const loadMovies = async () => {
    try {
      const data = await fetchMovies();
      setMovies(data);
      setError('');
    } catch (err) {
      setError('Failed to load movies. Is the backend running?');
    }
  };

  const handleCreate = async (data: MovieCreate) => {
    await createMovie(data);
    loadMovies();
  };

  const handleUpdate = async (data: MovieCreate) => {
    if (!editingMovie) return;
    const id = getMovieId(editingMovie);
    await updateMovie({ id, ...data } as Movie);
    setEditingMovie(null);
    loadMovies();
  };

  const handleDelete = async (id: string) => {
    await deleteMovie(id);
    loadMovies();
  };

  const filteredMovies = movies.filter((m) =>
    m.title.toLowerCase().includes(search.toLowerCase()) ||
    m.category.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="app">
      <header className="app-header">
        <h1>Movie Collection</h1>
        <SearchBar value={search} onChange={setSearch} />
      </header>
      {error && <div className="error-banner">{error}</div>}
      <MovieForm
        movie={editingMovie}
        onSubmit={editingMovie ? handleUpdate : handleCreate}
        onCancel={editingMovie ? () => setEditingMovie(null) : undefined}
      />
      <MovieList
        movies={filteredMovies}
        onEdit={setEditingMovie}
        onDelete={handleDelete}
      />
    </div>
  );
}

export default App;
