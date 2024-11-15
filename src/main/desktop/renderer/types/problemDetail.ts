export interface Violation {
  code: string;
  object: string;
  field: string;
  rejectedValue: unknown;
  message: string;
}

export interface ProblemDetail {
  type: string;
  title: string;
  status: number;
  detail: string;
  instance: string;
  violations?: Violation[];
}
