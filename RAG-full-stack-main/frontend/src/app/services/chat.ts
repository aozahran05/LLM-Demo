import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Service()
export class Chat {

  private http = inject(HttpClient);

  sendMessage(message: string) {
    return this.http.post('http://localhost:8080/api/chat', {
      message: message
    });
  }
}
