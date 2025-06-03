import axios, { type AxiosResponse, type InternalAxiosRequestConfig } from 'axios'
import router from '@/router'

class HttpClient {
  private instance

  constructor(baseURL: string) {
    this.instance = axios.create({
      baseURL,
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    })

    this.setupInterceptors()
  }

  private setupInterceptors() {
    // 请求拦截器
    this.instance.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        const tokenName = localStorage.getItem('tokenName')
        const tokenValue = localStorage.getItem('tokenValue')
        if (tokenName && tokenValue) {
          config.headers[tokenName] = tokenValue
        }
        return config
      },
      (error) => {
        return Promise.reject(error)
      },
    )

    // 响应拦截器
    this.instance.interceptors.response.use(
      (response: AxiosResponse) => {
        return response.data
      },
      (error) => {
        const status = error.response?.status

        switch (status) {
          case 401:
            console.log('未授权，跳转到登录页')
            localStorage.clear()
            router.push('/login')
            break
          case 403:
            console.log('禁止访问')
            break
          case 500:
            console.log('服务器内部错误')
            break
        }

        return Promise.reject(error)
      },
    )
  }

  // GET 请求
  public get<T = never>(url: string, config?: InternalAxiosRequestConfig): Promise<T> {
    return this.instance.get(url, config)
  }

  // POST 请求
  public post<T = never, D = never>(
    url: string,
    data?: D,
    config?: InternalAxiosRequestConfig,
  ): Promise<T> {
    return this.instance.post(url, data, config)
  }

  // PUT 请求
  public put<T = never, D = never>(
    url: string,
    data?: D,
    config?: InternalAxiosRequestConfig,
  ): Promise<T> {
    return this.instance.put(url, data, config)
  }

  // DELETE 请求
  public delete<T = never>(url: string, config?: InternalAxiosRequestConfig): Promise<T> {
    return this.instance.delete(url, config)
  }
}

// 创建 API 实例
const apiClient = new HttpClient(import.meta.env.VITE_HTTP_BASEURL as string)

export default apiClient
