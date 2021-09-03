export class ConsumerOffsets {
  
  current: number;
  end: number;

  constructor(current: number, end: number) {
    this.current = current;
    this.end = end;
  }

}