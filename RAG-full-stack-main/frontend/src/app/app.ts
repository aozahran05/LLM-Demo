import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Chat } from './services/chat';

@Component({
  imports: [FormsModule],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {

  protected readonly title = signal('frontend');

  message = '';

  messages: { text: string; sender: string }[] = [
    {
      text: 'Hello! How can I help you with your invoices?',
      sender: 'bot'
    }
  ];

  constructor(private chatService: Chat) {}

  sendMessage() {

    if (this.message.trim() === '') {
      return;
    }

    this.messages.push({
      text: this.message,
      sender: 'user'
    });

    this.chatService.sendMessage(this.message).subscribe({

      next: (response: any) => {
        this.messages.push({
          text: response.response,
          sender: 'bot'
        });
      },

      error: () => {
        this.messages.push({
          text: 'Something went wrong. Please try again.',
          sender: 'bot'
        });
      }

    });

    this.message = '';
  }
}
