import type { ChecklistItem } from '../types/ChecklistItem';
import ItemRow from './ItemRow';

interface Props {
  items: ChecklistItem[];
  onDelete: (id: number) => Promise<void>;
}

export default function ItemList({ items, onDelete }: Props) {
  if (items.length === 0) {
    return (
      <p className="text-center text-sm text-gray-400 py-10">
        No items yet. Add one above.
      </p>
    );
  }

  return (
    <ul className="space-y-2">
      {items.map(item => (
        <ItemRow key={item.id} item={item} onDelete={onDelete} />
      ))}
    </ul>
  );
}
