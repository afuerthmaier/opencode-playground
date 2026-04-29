import { TestBed, ComponentFixture } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ItemListComponent } from './item-list.component';
import { ChecklistItem } from '../../checklist/checklist-item.model';

const MOCK_ITEMS: ChecklistItem[] = [
  { id: 1, title: 'Task one', description: null, completed: false, createdAt: '2026-01-01T00:00:00' },
  { id: 2, title: 'Task two', description: 'desc', completed: true, createdAt: '2026-01-02T00:00:00' }
];

describe('ItemListComponent', () => {
  let fixture: ComponentFixture<ItemListComponent>;
  let component: ItemListComponent;

  async function createWithItems(items: ChecklistItem[]): Promise<void> {
    await TestBed.configureTestingModule({
      imports: [ItemListComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ItemListComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('items', items);
    fixture.detectChanges();
  }

  it('should create', async () => {
    await createWithItems([]);
    expect(component).toBeTruthy();
  });

  it('should show empty state when no items', async () => {
    await createWithItems([]);
    const text = fixture.nativeElement.textContent;
    expect(text).toContain('No items yet');
  });

  it('should render one row per item', async () => {
    await createWithItems(MOCK_ITEMS);
    const rows = fixture.debugElement.queryAll(By.css('app-item-row'));
    expect(rows.length).toBe(2);
  });

  it('should bubble delete event from item-row', async () => {
    await createWithItems(MOCK_ITEMS);
    const deletedIds: number[] = [];
    component.delete.subscribe((id: number) => deletedIds.push(id));

    const firstRow = fixture.debugElement.query(By.css('app-item-row'));
    firstRow.triggerEventHandler('delete', 1);

    expect(deletedIds).toEqual([1]);
  });
});
