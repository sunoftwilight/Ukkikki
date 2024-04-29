export interface MyResponseData {
  data: string;
  status: number;
  statusText: string;
  headers: Record<string, string>;
  config: string;
}