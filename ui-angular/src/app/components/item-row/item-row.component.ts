import { Component, input, output } from '@angular/core';
import { NgClass } from '@angular/common';
import { ChecklistItem } from '../../checklist/checklist-item.model';

@Component({
  selector: 'app-item-row',
  standalone: true,
  imports: [NgClass],
  templateUrl: './item-row.component.html'
})
export class ItemRowComponent {
  item = input.required<ChecklistItem>();
  delete = output<number>();

  deleteItem(): void {
    this.delete.emit(this.item().id);
  }
}
