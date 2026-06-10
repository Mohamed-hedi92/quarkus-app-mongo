import type { Movie } from '../types/Movie';
import { getMovieId } from '../types/Movie';

interface MovieListProps {
  movies: Movie[];
  onEdit: (movie: Movie) => void;
  onDelete: (id: string) => void;
}

export default function MovieList({ movies, onEdit, onDelete }: MovieListProps) {
  return (
    <div className="movie-grid">
      {movies.map((movie) => (
        <div key={getMovieId(movie)} className="movie-card">
          <h3>{movie.title}</h3>
          <p><strong>Category:</strong> {movie.category}</p>
          <p><strong>Duration:</strong> {movie.duration} min</p>
          <div className="card-actions">
            <button className="btn-edit" onClick={() => onEdit(movie)}>Edit</button>
            <button className="btn-delete" onClick={() => onDelete(getMovieId(movie))}>Delete</button>
          </div>
        </div>
      ))}
    </div>
  );
}
