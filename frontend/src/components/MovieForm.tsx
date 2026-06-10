import { useState, useEffect } from 'react';
import type { Movie, MovieCreate } from '../types/Movie';

interface MovieFormProps {
  movie?: Movie | null;
  onSubmit: (data: MovieCreate) => void;
  onCancel?: () => void;
}

export default function MovieForm({ movie, onSubmit, onCancel }: MovieFormProps) {
  const [title, setTitle] = useState('');
  const [category, setCategory] = useState('');
  const [duration, setDuration] = useState(0);

  useEffect(() => {
    if (movie) {
      setTitle(movie.title);
      setCategory(movie.category);
      setDuration(movie.duration);
    } else {
      setTitle('');
      setCategory('');
      setDuration(0);
    }
  }, [movie]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({ title, category, duration });
    if (!movie) {
      setTitle('');
      setCategory('');
      setDuration(0);
    }
  };

  return (
    <form className="movie-form" onSubmit={handleSubmit}>
      <h2>{movie ? 'Edit Movie' : 'Add New Movie'}</h2>
      <div className="form-group">
        <label>Title</label>
        <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />
      </div>
      <div className="form-group">
        <label>Category</label>
        <input type="text" value={category} onChange={(e) => setCategory(e.target.value)} required />
      </div>
      <div className="form-group">
        <label>Duration (min)</label>
        <input type="number" value={duration} onChange={(e) => setDuration(Number(e.target.value))} required min={1} />
      </div>
      <div className="form-actions">
        <button type="submit" className="btn-primary">{movie ? 'Update' : 'Add Movie'}</button>
        {onCancel && <button type="button" className="btn-cancel" onClick={onCancel}>Cancel</button>}
      </div>
    </form>
  );
}
