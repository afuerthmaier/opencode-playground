import { TestBed, ComponentFixture } from '@angular/core/testing';
import { AddItemFormComponent } from './add-item-form.component';

describe('AddItemFormComponent', () => {
  let fixture: ComponentFixture<AddItemFormComponent>;
  let component: AddItemFormComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddItemFormComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(AddItemFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not emit when title is empty', () => {
    const spy = vi.fn();
    component.add.subscribe(spy);
    component.title = '   ';
    component.handleSubmit();
    expect(spy).not.toHaveBeenCalled();
  });

  it('should emit trimmed title and description on valid submit', () => {
    const emitted: { title: string; description?: string }[] = [];
    component.add.subscribe(v => emitted.push(v));

    component.title = '  Buy groceries  ';
    component.description = '  Milk, eggs  ';
    component.handleSubmit();

    expect(emitted.length).toBe(1);
    expect(emitted[0].title).toBe('Buy groceries');
    expect(emitted[0].description).toBe('Milk, eggs');
  });

  it('should emit without description when description is blank', () => {
    const emitted: { title: string; description?: string }[] = [];
    component.add.subscribe(v => emitted.push(v));

    component.title = 'Task';
    component.description = '   ';
    component.handleSubmit();

    expect(emitted[0].description).toBeUndefined();
  });

  it('should reset fields after submit', () => {
    component.add.subscribe(() => {});
    component.title = 'Task';
    component.description = 'Details';
    component.handleSubmit();
    expect(component.title).toBe('');
    expect(component.description).toBe('');
  });
});
