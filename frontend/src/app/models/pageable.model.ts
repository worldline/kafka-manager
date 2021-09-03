import { Page } from '@models/page.model';

export class Pageable {

    currentPage: number;
    itemsPerPage: number;
    totalItems: number;
    hasPagination: boolean;

    constructor(currentPage: number = 1, itemsPerPage: number = 10, totalItems: number = 0, hasPagination: boolean = false) {
        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
        this.totalItems = totalItems;
        this.hasPagination = hasPagination;
    }

    static readPage<T>(page: Page<T>): Pageable {
        return new Pageable(page.number + 1, page.size, page.totalElements, page.totalPages > 1);
    }
}
