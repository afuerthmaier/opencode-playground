import { Component, inject, signal, computed, OnInit } from '@angular/core';
import { ChecklistService } from '../checklist/checklist.service';
import { ChecklistItem } from '../checklist/checklist-item.model';
import { AddItemFormComponent } from '../components/add-item-form/add-item-form.component';
import { ItemListComponent } from '../components/item-list/item-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [AddItemFormComponent, ItemListComponent],
  templateUrl: './app-container.component.html'
})
export class AppContainerComponent implements OnInit {
  private service = inject(ChecklistService);

  items = signal<ChecklistItem[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);
  completed = computed(() => this.items().filter(i => i.completed).length);

  ngOnInit(): void {
    this.fetchItems();
  }

  fetchItems(): void {
    this.error.set(null);
    this.loading.set(true);
    this.service.getAll().subscribe({
      next: (data: ChecklistItem[]) => {
        this.items.set(data);
        this.loading.set(false);
      },
      error: (err: Error) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }

  onAdd(event: { title: string; description?: string }): void {
    this.service.create(event.title, event.description ?? null).subscribe({
      next: () => this.fetchItems()
    });
  }

  onDelete(id: number): void {
    this.service.remove(id).subscribe({
      next: () => this.items.update(items => items.filter(i => i.id !== id))
    });
  }
}
