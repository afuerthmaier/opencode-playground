export interface ChecklistItem {
  id: number;
  title: string;
  description: string | null;
  completed: boolean;
  createdAt: string;
}
