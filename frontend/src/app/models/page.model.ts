export class Page<T> {

  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;

  constructor(content: T[], totalElements: number, totalPages: number, number: number, size: number) {
    this.content = content;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.number = number;
    this.size = size;
  }

}