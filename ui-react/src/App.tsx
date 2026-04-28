import { useEffect, useState } from 'react';
import type { ChecklistItem } from './types/ChecklistItem';
import { getAll, create, remove } from './api/checklistApi';
import AddItemForm from './components/AddItemForm';
import ItemList from './components/ItemList';

export default function App() {
  const [items, setItems] = useState<ChecklistItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  async function fetchItems() {
    try {
      setError(null);
      const data = await getAll();
      setItems(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load items');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchItems();
  }, []);

  async function handleAdd(title: string, description?: string) {
    await create(title, description);
    await fetchItems();
  }

  async function handleDelete(id: number) {
    await remove(id);
    setItems(prev => prev.filter(item => item.id !== id));
  }

  const completed = items.filter(i => i.completed).length;

  return (
    <div className="min-h-screen bg-gray-50 px-4 py-10">
      <div className="mx-auto max-w-lg space-y-6">

        {/* Header */}
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Checklist</h1>
          {!loading && (
            <p className="text-sm text-gray-500 mt-1">
              {completed} of {items.length} completed
            </p>
          )}
        </div>

        {/* Add form */}
        <AddItemForm onAdd={handleAdd} />

        {/* List */}
        {loading ? (
          <p className="text-center text-sm text-gray-400 py-10">Loading…</p>
        ) : error ? (
          <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-600">
            {error}
            <button onClick={fetchItems} className="ml-2 underline hover:no-underline">Retry</button>
          </div>
        ) : (
          <ItemList items={items} onDelete={handleDelete} />
        )}

      </div>
    </div>
  );
}
