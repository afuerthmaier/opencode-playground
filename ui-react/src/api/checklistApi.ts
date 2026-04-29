import type { ChecklistItem } from '../types/ChecklistItem';

const BASE = '/api/checklist-items';

async function handleResponse<T>(res: Response): Promise<T> {
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`HTTP ${res.status}: ${text}`);
  }
  if (res.status === 204) return undefined as T;
  return res.json() as Promise<T>;
}

export async function getAll(): Promise<ChecklistItem[]> {
  const res = await fetch(BASE);
  return handleResponse<ChecklistItem[]>(res);
}

export async function create(title: string, description?: string): Promise<ChecklistItem> {
  const res = await fetch(BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ title, description: description || null }),
  });
  return handleResponse<ChecklistItem>(res);
}

export async function remove(id: number): Promise<void> {
  const res = await fetch(`${BASE}/${id}`, { method: 'DELETE' });
  return handleResponse<void>(res);
}
