export interface Page<T> {
  content: T[]; // The content of the current page (list of data)
  pageable: Pageable; // Pagination details
  totalPages: number; // Total number of pages
  totalElements: number; // Total number of items across all pages
  last: boolean; // Indicates if this is the last page
  size: number; // The number of items per page
  number: number; // The current page number (zero-based)
  sort: Sort; // Sorting details
  first: boolean; // Indicates if this is the first page
  numberOfElements: number; // Number of items on the current page
  empty: boolean; // Indicates if the current page is empty
}

export interface Pageable {
  sort: Sort; // Sorting details
  pageNumber: number; // Current page number (zero-based)
  pageSize: number; // Number of items per page
  offset: number; // The starting position of the data
  paged: boolean; // Indicates if pagination is enabled
  unpaged: boolean; // Indicates if pagination is disabled
}

export interface Sort {
  sorted: boolean; // Indicates if sorting is applied
  unsorted: boolean; // Indicates if sorting is not applied
  empty: boolean; // Indicates if sorting information is missing
}
