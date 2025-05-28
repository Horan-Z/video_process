<script setup lang="ts">
import { UploadFilled } from '@element-plus/icons-vue'
import OSS from 'ali-oss'
import axios from 'axios'
import { ref } from 'vue'

const uploadPercentage = ref(0)
const showProgress = ref(false)
let abortCheckpoint = null

class OSSClientManager {
  private static instance: OSSClientManager
  private client: OSS | null = null
  private region = 'oss-cn-beijing'
  private bucket = 'xiaoming10086'
  private stsEndpoint = 'http://localhost:8080/api/sts'

  // 单例模式，确保全局只有一个client实例
  public static getInstance(): OSSClientManager {
    if (!OSSClientManager.instance) {
      OSSClientManager.instance = new OSSClientManager()
    }
    return OSSClientManager.instance
  }

  // 初始化或获取已有的client
  public async getClient(): Promise<OSS> {
    if (this.client) {
      return this.client
    }

    const token = await this.fetchSTSToken()
    this.client = new OSS({
      region: this.region,
      accessKeyId: token.credentials.accessKeyId,
      accessKeySecret: token.credentials.accessKeySecret,
      stsToken: token.credentials.securityToken,
      // @ts-expect-error idk
      authorizationV4: true,
      bucket: this.bucket,
      // 自动刷新临时访问凭证
      refreshSTSToken: async () => {
        const refreshedToken = await this.fetchSTSToken()
        return {
          accessKeyId: refreshedToken.credentials.accessKeyId,
          accessKeySecret: refreshedToken.credentials.accessKeySecret,
          stsToken: refreshedToken.credentials.securityToken,
        }
      },
    })
    return this.client
  }

  // 上传文件方法
  public async uploadFile(fileName: string, file: File) {
    try {
      const client = await this.getClient()
      // const result = await client.put(fileName, file, { headers })
      const result = await client.multipartUpload(fileName, file, {
        progress,
        headers,
      })
      // const head = await client.head(fileName)
      console.log('上传成功:', result)
      // console.log(head)
    } catch (error) {
      console.error('上传失败:', error)
      throw error
    }
  }

  public async abortUpload(fileName: string, uploadId: string) {
    try {
      const client = await this.getClient()
      // const result = await client.put(fileName, file, { headers })
      // await client.abortMultipartUpload(fileName, uploadId)
      await client.cancel()
    } catch (error) {
      throw error
    }
  }

  // 获取STS令牌
  private async fetchSTSToken() {
    const response = await axios.get(this.stsEndpoint)
    // console.log(response)
    return response.data.data
  }
}

const progress = (p, cpt) => {
  // Object的上传进度。
  console.log(p)
  uploadPercentage.value = parseFloat((p * 100).toFixed(2))
  abortCheckpoint = cpt
  // 分片上传的断点信息。
  // console.log(_checkpoint)
}

// 自定义请求头
const headers = {
  // 指定Object的存储类型。
  'x-oss-storage-class': 'Standard',
  // 指定Object的访问权限。
  'x-oss-object-acl': 'private',
  // 通过文件URL访问文件时，指定以附件形式下载文件，下载后的文件名称定义为example.txt。
  // 'Content-Disposition': 'attachment; filename="example.txt"',
  // 设置Object的标签，可同时设置多个标签。
  // 'x-oss-tagging': 'Tag1=1&Tag2=2',
  // 指定PutObject操作时是否覆盖同名目标Object。此处设置为true，表示禁止覆盖同名Object。
  'x-oss-forbid-overwrite': 'true',
}

async function upload(options: JSON) {
  try {
    // @ts-expect-error it dose
    const file = options.file
    // console.log(file)
    const fileName = '/vp/source/' + file.name
    // console.log(fileName)
    await OSSClientManager.getInstance().uploadFile(fileName, file)
    // console.log(result)
    console.log('上传完毕')
  } catch (e) {
    console.log(e)
  }
}

async function abort() {
  if (abortCheckpoint != null) {
    await OSSClientManager.getInstance().abortUpload(abortCheckpoint.name, abortCheckpoint.uploadId)
  }
}

function beforeUpload() {
  uploadPercentage.value = 0.0
  showProgress.value = true
}

function exceed() {
  console.log('exceed')
}
</script>

<template>
  <el-upload
    class="upload"
    drag
    action="#"
    :http-request="upload"
    :before-upload="beforeUpload"
    :on-remove="abort"
    :limit="1"
    :on-exceed="exceed"
    auto-upload
  >
    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
    <div class="el-upload__text">Drop file here or <em>click to upload</em></div>
    <div class="el-upload__text">最大文件大小2GB</div>
  </el-upload>

  <div class="progress" v-show="showProgress">
    <el-progress :text-inside="true" :stroke-width="26" :percentage="uploadPercentage" />
  </div>
</template>

<!-- <style scoped>
.uplpad {
  max-width: 1280px;
}
</style> -->
