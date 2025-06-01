export interface HttpResponse<T = never> {
  code: number;
  msg: string;
  data: T; // 使用泛型支持不同的数据类型
}
