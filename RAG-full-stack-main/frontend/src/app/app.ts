import { Component, signal, ViewChild, ElementRef, inject, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Chat } from './services/chat';

export interface Message {
  text: string;
  sender: 'user' | 'bot';
}

@Component({
  imports: [FormsModule],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {

  private chatService = inject(Chat);
  private cdr = inject(ChangeDetectorRef);

  message = signal('');
  isLoading = signal(false);

  messages = signal<Message[]>([
    {
      text: 'Hello! How can I help you with your invoices?',
      sender: 'bot'
    }
  ]);

  @ViewChild('messagesContainer') private messagesContainer?: ElementRef<HTMLDivElement>;

  sendMessage() {
    const textToSend = this.message().trim();

    if (!textToSend || this.isLoading()) {
      return;
    }

    // 1. Immediately append user message
    this.messages.update((msgs) => [...msgs, { text: textToSend, sender: 'user' }]);
    this.message.set('');
    this.isLoading.set(true);
    this.scrollToBottom();

    // 2. Dispatch request
    this.chatService.sendMessage(textToSend).subscribe({
      next: (response: any) => {
        const botReply = response?.response || response?.answer || 'I could not retrieve an answer.';
        this.messages.update((msgs) => [...msgs, { text: botReply, sender: 'bot' }]);
        this.isLoading.set(false);
        this.cdr.markForCheck();
        this.scrollToBottom();
      },

      error: (err) => {
        console.error('Chat error:', err);
        this.messages.update((msgs) => [
          ...msgs,
          { text: 'Something went wrong. Please try again.', sender: 'bot' }
        ]);
        this.isLoading.set(false);
        this.cdr.markForCheck();
        this.scrollToBottom();
      }
    });
  }

  private scrollToBottom() {
    setTimeout(() => {
      if (this.messagesContainer?.nativeElement) {
        this.messagesContainer.nativeElement.scrollTop = this.messagesContainer.nativeElement.scrollHeight;
      }
    }, 50);
  }
}
