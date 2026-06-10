export interface Movie {
  id: string | { $oid: string };
  title: string;
  category: string;
  duration: number;
}

export interface MovieCreate {
  title: string;
  category: string;
  duration: number;
}

export function getMovieId(movie: Movie): string {
  if (typeof movie.id === 'string') return movie.id;
  return movie.id.$oid;
}
