import { TestBed, ComponentFixture } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ItemRowComponent } from './item-row.component';
import { ChecklistItem } from '../../checklist/checklist-item.model';

const INCOMPLETE_ITEM: ChecklistItem = {
  id: 1,
  title: 'Buy groceries',
  description: 'Milk, eggs',
  completed: false,
  createdAt: '2026-01-01T00:00:00'
};

const COMPLETED_ITEM: ChecklistItem = {
  ...INCOMPLETE_ITEM,
  completed: true
};

describe('ItemRowComponent', () => {
  let fixture: ComponentFixture<ItemRowComponent>;
  let component: ItemRowComponent;

  async function createWithItem(item: ChecklistItem): Promise<void> {
    await TestBed.configureTestingModule({
      imports: [ItemRowComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ItemRowComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('item', item);
    fixture.detectChanges();
  }

  it('should create', async () => {
    await createWithItem(INCOMPLETE_ITEM);
    expect(component).toBeTruthy();
  });

  it('should display the item title', async () => {
    await createWithItem(INCOMPLETE_ITEM);
    const text = fixture.nativeElement.textContent;
    expect(text).toContain('Buy groceries');
  });

  it('should display description when present', async () => {
    await createWithItem(INCOMPLETE_ITEM);
    const text = fixture.nativeElement.textContent;
    expect(text).toContain('Milk, eggs');
  });

  it('should apply line-through style when completed', async () => {
    await createWithItem(COMPLETED_ITEM);
    const title = fixture.debugElement.query(By.css('p'));
    expect(title.nativeElement.classList).toContain('line-through');
  });

  it('should not apply line-through when incomplete', async () => {
    await createWithItem(INCOMPLETE_ITEM);
    const title = fixture.debugElement.query(By.css('p'));
    expect(title.nativeElement.classList).not.toContain('line-through');
  });

  it('should emit delete event with item id when delete button clicked', async () => {
    await createWithItem(INCOMPLETE_ITEM);
    const emitted: number[] = [];
    component.delete.subscribe((id: number) => emitted.push(id));

    const button = fixture.debugElement.query(By.css('button'));
    button.triggerEventHandler('click', null);

    expect(emitted).toEqual([1]);
  });
});
