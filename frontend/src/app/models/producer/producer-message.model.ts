export class ProducerMessage {
    key: string;
    message: string;
    partition: number;
    headers: any;
}
