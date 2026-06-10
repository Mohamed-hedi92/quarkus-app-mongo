import type { Movie, MovieCreate } from '../types/Movie';
import { getMovieId } from '../types/Movie';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081/movies';

export async function fetchMovies(): Promise<Movie[]> {
  const res = await fetch(API_URL);
  if (!res.ok) throw new Error('Failed to fetch movies');
  return res.json();
}

export async function createMovie(movie: MovieCreate): Promise<Movie> {
  const res = await fetch(API_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(movie),
  });
  if (!res.ok) throw new Error('Failed to create movie');
  return res.json();
}

export async function updateMovie(movie: Movie): Promise<Movie> {
  const id = getMovieId(movie);
  const res = await fetch(API_URL + '/' + id, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ title: movie.title, category: movie.category, duration: movie.duration }),
  });
  if (!res.ok) throw new Error('Failed to update movie');
  return res.json();
}

export async function deleteMovie(id: string): Promise<void> {
  const res = await fetch(API_URL + '/' + id, { method: 'DELETE' });
  if (!res.ok) throw new Error('Failed to delete movie');
}
