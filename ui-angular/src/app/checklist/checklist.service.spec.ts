import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting
} from '@angular/common/http/testing';
import { ChecklistService } from './checklist.service';
import { ChecklistItem } from './checklist-item.model';

const MOCK_ITEM: ChecklistItem = {
  id: 1,
  title: 'Buy groceries',
  description: 'Milk, eggs',
  completed: false,
  createdAt: '2026-01-01T00:00:00'
};

describe('ChecklistService', () => {
  let service: ChecklistService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(ChecklistService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getAll()', () => {
    it('should GET /api/checklist-items and return items', () => {
      const items = [MOCK_ITEM];
      service.getAll().subscribe(result => {
        expect(result).toEqual(items);
      });
      const req = httpMock.expectOne('/api/checklist-items');
      expect(req.request.method).toBe('GET');
      req.flush(items);
    });

    it('should propagate HTTP errors as Error instances', () => {
      service.getAll().subscribe({
        error: (err: Error) => {
          expect(err.message).toContain('404');
        }
      });
      httpMock.expectOne('/api/checklist-items').flush('Not found', {
        status: 404,
        statusText: 'Not Found'
      });
    });
  });

  describe('create()', () => {
    it('should POST to /api/checklist-items with title and description', () => {
      service.create('Buy groceries', 'Milk, eggs').subscribe(result => {
        expect(result).toEqual(MOCK_ITEM);
      });
      const req = httpMock.expectOne('/api/checklist-items');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({ title: 'Buy groceries', description: 'Milk, eggs' });
      req.flush(MOCK_ITEM);
    });

    it('should POST with null description when omitted', () => {
      service.create('Title only').subscribe();
      const req = httpMock.expectOne('/api/checklist-items');
      expect(req.request.body).toEqual({ title: 'Title only', description: undefined });
      req.flush(MOCK_ITEM);
    });
  });

  describe('remove()', () => {
    it('should DELETE /api/checklist-items/:id', () => {
      let completed = false;
      service.remove(1).subscribe({ complete: () => (completed = true) });
      const req = httpMock.expectOne('/api/checklist-items/1');
      expect(req.request.method).toBe('DELETE');
      req.flush(null, { status: 204, statusText: 'No Content' });
      expect(completed).toBe(true);
    });

    it('should propagate HTTP errors as Error instances', () => {
      service.remove(999).subscribe({
        error: (err: Error) => {
          expect(err.message).toContain('404');
        }
      });
      httpMock.expectOne('/api/checklist-items/999').flush('Not found', {
        status: 404,
        statusText: 'Not Found'
      });
    });
  });
});
