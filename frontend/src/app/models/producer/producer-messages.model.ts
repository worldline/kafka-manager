import { ProducerMessage } from './producer-message.model';

export class ProducerMessages {
    messages: ProducerMessage[];

    constructor(messages: ProducerMessage[]) {
        this.messages = messages;
    }
}
