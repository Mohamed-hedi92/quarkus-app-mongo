interface SearchBarProps {
  value: string;
  onChange: (value: string) => void;
}

export default function SearchBar({ value, onChange }: SearchBarProps) {
  return (
    <div className="search-bar">
      <input
        type="text"
        placeholder="Search by title or category..."
        value={value}
        onChange={(e) => onChange(e.target.value)}
      />
    </div>
  );
}
