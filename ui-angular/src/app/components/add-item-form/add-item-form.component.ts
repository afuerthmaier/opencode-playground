import { Component, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-item-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-item-form.component.html'
})
export class AddItemFormComponent {
  // Plain properties for two-way ngModel binding
  title = '';
  description = '';

  loading = signal(false);
  error = signal<string | null>(null);

  add = output<{ title: string; description?: string }>();

  handleSubmit(): void {
    const trimmedTitle = this.title.trim();
    if (!trimmedTitle) return;

    this.loading.set(true);
    this.error.set(null);

    this.add.emit({
      title: trimmedTitle,
      description: this.description.trim() || undefined
    });

    this.title = '';
    this.description = '';
    this.loading.set(false);
  }
}
