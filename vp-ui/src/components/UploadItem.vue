<script setup lang="ts">
import { UploadFilled, Delete } from '@element-plus/icons-vue'
import OSS from 'ali-oss'
import axios from 'axios'
import { ref } from 'vue'

const uploadPercentage = ref(0)
const inProgress = ref(false)
const uploadFinish = ref(false)
const uploadRef = ref(null)
const fileName = ref('null')

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
      const result = await client.multipartUpload(fileName, file, {
        parallel: 4,
        partSize: 5 * 1024 * 1024, // 5MByte
        progress,
        headers,
      })
      console.log('上传成功:', result)
    } catch (error) {
      console.error('上传失败:', error)
      throw error
    }
  }

  public async abortUpload() {
    try {
      const client = await this.getClient()
      // @ts-expect-error it does exist
      await client.cancel()
    } catch (error) {
      throw error
    }
  }

  // 获取STS令牌
  private async fetchSTSToken() {
    const response = await axios.get(this.stsEndpoint)
    return response.data.data
  }
}

const progress = (p: number) => {
  console.log(p)
  uploadPercentage.value = parseFloat((p * 100).toFixed(2))
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
  'x-oss-forbid-overwrite': 'false',
}

async function upload(options: JSON) {
  try {
    // @ts-expect-error it does exist
    const file = options.file
    fileName.value = file.name
    await OSSClientManager.getInstance().uploadFile('/vp/source/' + fileName.value, file)
    console.log('上传完毕')
    uploadSuccess()
  } catch (e) {
    console.log(e)
  }
}

function uploadSuccess() {
  inProgress.value = false
  uploadFinish.value = true
}

async function abortOrRemove() {
  try{
    if(inProgress.value) {
      await OSSClientManager.getInstance().abortUpload()
    } else if(uploadFinish.value) {
      console.log('remove')
      // TODO remove logic
    }
    // @ts-expect-error it does exist
    uploadRef.value!.clearFiles()
    uploadPercentage.value = 0.0
    inProgress.value = false
    uploadFinish.value = false
  } catch(e) {
    console.log(e)
  }

}

function beforeUpload() {
  uploadPercentage.value = 0.0
  inProgress.value = true
}

function exceed() {
  console.log('exceed')
}
</script>

<template>
  <el-upload
    class="upload"
    ref="uploadRef"
    drag
    action="#"
    :http-request="upload"
    :before-upload="beforeUpload"
    :on-remove="abortOrRemove"
    :limit="1"
    :on-exceed="exceed"
    :show-file-list="false"
    auto-upload
    v-show="!(inProgress || uploadFinish)"
  >
    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
    <div class="el-upload__text">Drop file here or <em>click to upload</em></div>
    <div class="el-upload__text">最大文件大小2GB</div>
  </el-upload>

  <div class="progress" v-show="inProgress || uploadFinish">
    <div class="name">
      <span style="font-size: 1.8em;">{{ fileName }}</span>
      <el-button type="danger" :icon="Delete" circle @click="abortOrRemove" />
    </div>
    <el-progress :text-inside="true" :stroke-width="26" :percentage="uploadPercentage" />
  </div>

  <div class="settings" v-show="inProgress || uploadFinish">

  </div>
</template>

<style scoped>
.progress .name {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}
</style>
