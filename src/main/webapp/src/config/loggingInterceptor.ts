import { AxiosError } from 'axios';
import { ProblemDetail } from '@/types/problemDetail';

const DEVELOPMENT = process.env.NODE_ENV === 'development';

const getErrorMessage = (problemDetail: ProblemDetail): string => {
  let { detail } = problemDetail;

  if (problemDetail.violations && problemDetail.violations.length > 0) {
    problemDetail.violations.forEach((violation) => {
      detail += `\nField: ${violation.field}, Object: ${violation.object}, Message: ${violation.message}`;
    });
  }

  return detail;
};

export const loggingInterceptor = (error: AxiosError): Promise<AxiosError> => {
  if (DEVELOPMENT) {

    if (error.response && error.response.data) {
      const problemDetail = error.response.data as ProblemDetail;
      const message = getErrorMessage(problemDetail);
      console.error(`Actual cause: ${message}`);
    }
  }
  return Promise.reject(error);
};
