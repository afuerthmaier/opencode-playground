import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ChecklistItem } from './checklist-item.model';

@Injectable({ providedIn: 'root' })
export class ChecklistService {
  private readonly BASE = '/api/checklist-items';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ChecklistItem[]> {
    return this.http.get<ChecklistItem[]>(this.BASE).pipe(
      catchError(this.handleError)
    );
  }

  create(title: string, description?: string | null): Observable<ChecklistItem> {
    return this.http.post<ChecklistItem>(this.BASE, { title, description }).pipe(
      catchError(this.handleError)
    );
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const msg = error.error instanceof Error ? error.error.message : `HTTP ${error.status}`;
    return throwError(() => new Error(msg));
  }
}
