import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting
} from '@angular/common/http/testing';
import { AppContainerComponent } from './app-container.component';
import { ChecklistItem } from '../checklist/checklist-item.model';

const MOCK_ITEMS: ChecklistItem[] = [
  { id: 1, title: 'Task one', description: null, completed: false, createdAt: '2026-01-01T00:00:00' },
  { id: 2, title: 'Task two', description: 'desc', completed: true, createdAt: '2026-01-02T00:00:00' }
];

describe('AppContainerComponent', () => {
  let fixture: ComponentFixture<AppContainerComponent>;
  let component: AppContainerComponent;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppContainerComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()]
    }).compileComponents();

    fixture = TestBed.createComponent(AppContainerComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  function flushGetAll(items = MOCK_ITEMS): void {
    httpMock.expectOne('/api/checklist-items').flush(items);
  }

  it('should create', () => {
    flushGetAll([]);
    expect(component).toBeTruthy();
  });

  it('should start in loading state before data arrives', () => {
    // detectChanges triggers ngOnInit and the HTTP request
    fixture.detectChanges();
    expect(component.loading()).toBe(true);
    flushGetAll([]); // satisfy the pending request so afterEach verify() passes
  });

  it('should load items on init', () => {
    fixture.detectChanges();
    flushGetAll();
    expect(component.items()).toEqual(MOCK_ITEMS);
    expect(component.loading()).toBe(false);
  });

  it('should compute completed count correctly', () => {
    fixture.detectChanges();
    flushGetAll();
    expect(component.completed()).toBe(1);
  });

  it('should set error signal on fetch failure', () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/checklist-items').flush('Server error', {
      status: 500,
      statusText: 'Internal Server Error'
    });
    expect(component.error()).toContain('500');
    expect(component.loading()).toBe(false);
  });

  it('should refetch after onAdd', () => {
    fixture.detectChanges();
    flushGetAll([]);

    const newItem: ChecklistItem = { id: 3, title: 'New task', description: null, completed: false, createdAt: '2026-01-03T00:00:00' };
    component.onAdd({ title: 'New task' });
    httpMock.expectOne('/api/checklist-items').flush(newItem);
    flushGetAll([...MOCK_ITEMS, newItem]);

    expect(component.items().length).toBe(3);
  });

  it('should remove item from list on onDelete', () => {
    fixture.detectChanges();
    flushGetAll();

    component.onDelete(1);
    httpMock.expectOne('/api/checklist-items/1').flush(null, { status: 204, statusText: 'No Content' });

    expect(component.items().find(i => i.id === 1)).toBeUndefined();
    expect(component.items().length).toBe(1);
  });
});
