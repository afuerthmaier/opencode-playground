import type { ChecklistItem } from '../types/ChecklistItem';

interface Props {
  item: ChecklistItem;
  onDelete: (id: number) => Promise<void>;
}

export default function ItemRow({ item, onDelete }: Props) {
  return (
    <li className="flex items-start justify-between gap-4 rounded-xl border border-gray-200 bg-white px-4 py-3 shadow-sm">
      <div className="flex items-start gap-3 min-w-0">
        <span
          className={`mt-0.5 h-4 w-4 shrink-0 rounded-full border-2 ${
            item.completed ? 'border-indigo-500 bg-indigo-500' : 'border-gray-300 bg-white'
          }`}
        />
        <div className="min-w-0">
          <p className={`text-sm font-medium truncate ${item.completed ? 'text-gray-400 line-through' : 'text-gray-900'}`}>
            {item.title}
          </p>
          {item.description && (
            <p className="text-xs text-gray-500 mt-0.5 truncate">{item.description}</p>
          )}
        </div>
      </div>

      <button
        onClick={() => onDelete(item.id)}
        aria-label={`Delete "${item.title}"`}
        className="shrink-0 rounded-lg p-1.5 text-gray-400 hover:bg-red-50 hover:text-red-500 transition-colors"
      >
        <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
          <path strokeLinecap="round" strokeLinejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6M9 7h6m2 0a1 1 0 00-1-1h-4a1 1 0 00-1 1H5" />
        </svg>
      </button>
    </li>
  );
}
