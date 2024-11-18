import React from 'react';
import { Pagination } from 'react-bootstrap';
import useMediaQuery from '../hooks/useMediaQuery';

interface PaginationProps {
  currentPage: number; // Current page (0-based index)
  totalPages: number; // Total number of pages
  onPageChange: (page: number) => void; // Callback to handle page change
  siblingCount?: number; // Number of sibling pages to show on each side
}

const PaginationComponent: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  onPageChange,
  siblingCount = 1,
}) => {
  const isMobile = useMediaQuery('(max-width: 768px)');

  const createPaginationRange = (): (number | string)[] => {
    const totalPageNumbers = siblingCount * 2 + 5; // First, Last, Prev, Next, Current + Siblings

    if (totalPages <= totalPageNumbers) {
      // Show all pages if total pages fit within the limit
      return Array.from({ length: totalPages }, (_, i) => i);
    }

    const start = Math.max(currentPage - siblingCount, 0);
    const end = Math.min(currentPage + siblingCount, totalPages - 1);

    const hasLeftEllipsis = start > 1;
    const hasRightEllipsis = end < totalPages - 2;

    const range = [];

    if (hasLeftEllipsis) {
      range.push(0, '...');
    } else {
      for (let i = 0; i < start; i++) {
        range.push(i);
      }
    }

    for (let i = start; i <= end; i++) {
      range.push(i);
    }

    if (hasRightEllipsis) {
      range.push('...', totalPages - 1);
    } else {
      for (let i = end + 1; i < totalPages; i++) {
        range.push(i);
      }
    }

    return range;
  };

  const paginationRange = createPaginationRange();

  const handlePageChange = (page: number) => {
    if (page !== currentPage && page >= 0 && page < totalPages) {
      onPageChange(page);
    }
  };

  return (
    <Pagination size={isMobile ? 'sm' : 'lg'} className="justify-content-center my-3">
      <Pagination.First onClick={() => handlePageChange(0)} disabled={currentPage === 0} />
      <Pagination.Prev onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 0} />
      {paginationRange.map((item, index) =>
        typeof item === 'string' ? (
          <Pagination.Ellipsis key={index} disabled />
        ) : (
          <Pagination.Item key={item} active={item === currentPage} onClick={() => handlePageChange(item)}>
            {item + 1}
          </Pagination.Item>
        ),
      )}
      <Pagination.Next onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages - 1} />
      <Pagination.Last onClick={() => handlePageChange(totalPages - 1)} disabled={currentPage === totalPages - 1} />
    </Pagination>
  );
};

export default PaginationComponent;
