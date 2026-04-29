import { Component, input, output } from '@angular/core';
import { ChecklistItem } from '../../checklist/checklist-item.model';
import { ItemRowComponent } from '../item-row/item-row.component';

@Component({
  selector: 'app-item-list',
  standalone: true,
  imports: [ItemRowComponent],
  templateUrl: './item-list.component.html'
})
export class ItemListComponent {
  items = input.required<ChecklistItem[]>();
  delete = output<number>();
}
