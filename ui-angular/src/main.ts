import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { AppContainerComponent } from './app/containers/app-container.component';

bootstrapApplication(AppContainerComponent, {
  providers: [provideHttpClient()]
}).catch((err) => console.error(err));
