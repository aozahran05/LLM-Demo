import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Service()
export class Chat {

  private http = inject(HttpClient);
  private sessionId = 'session-' + Math.random().toString(36).substring(2, 11) + '-' + Date.now();

  sendMessage(message: string) {
    return this.http.post('http://localhost:8080/api/chat', {
      message: message,
      session_id: this.sessionId
    });
  }
}
