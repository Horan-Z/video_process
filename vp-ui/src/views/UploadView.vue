<script setup lang="ts">
import { UploadFilled, Delete, Check } from '@element-plus/icons-vue'
import apiClient from '@/api/httpClient.ts'
import type { HttpResponse } from '@/types/Response'
import { reactive, ref } from 'vue'
import { v4 as uuidv4 } from 'uuid'
import PageHeader from '@/components/PageHeader.vue'
import OSS from 'ali-oss'

interface UploadFile {
  fileUuid: string // 唯一标识
  fileName: string // 文件名
  percentage: number // 上传进度
  checkpoint: object | null // 分片上传checkpoint
  inProgress: boolean // 是否上传中
  file: File // 文件对象
  ossObject: string
  canceled: boolean
  client: OSS
}

// interface UploadCallback {
//   fileName: string;
//   fileUuid: string;
//   fileExtension: string | undefined;
//   filePath: string;
// }

interface Token {
  credentials: {
    accessKeyId: string;
    accessKeySecret: string;
    securityToken: string
  }
}

const uploadFiles = ref<UploadFile[]>([])

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

async function createClient() {
  const token: Token = await fetchSTSToken()
  return new OSS({
    region: import.meta.env.VITE_OSS_REGION as string,
    accessKeyId: token.credentials.accessKeyId,
    accessKeySecret: token.credentials.accessKeySecret,
    stsToken: token.credentials.securityToken,
    // @ts-expect-error idk
    authorizationV4: true,
    bucket: import.meta.env.VITE_OSS_BUCKET as string,
    refreshSTSToken: async () => {
      const token: Token = await fetchSTSToken()
      return {
        accessKeyId: token.credentials.accessKeyId,
        accessKeySecret: token.credentials.accessKeySecret,
        stsToken: token.credentials.securityToken,
      }
    },
  })
}

// 获取STS令牌
async function fetchSTSToken(): Promise<Token> {
  const response: HttpResponse<Token> = await apiClient.get<HttpResponse<Token>>('/api/oss/sts')
  return response.data
}

function getFileExtension(filename: string) {
  const parts = filename.split('.')
  return parts.length > 1 ? parts.pop() : ''
}

async function upload(options: object) {
  try {
    const newFile = reactive<UploadFile>({
      fileUuid: uuidv4(),
      // @ts-expect-error it does exist
      fileName: options.file.name,
      percentage: 0,
      checkpoint: null,
      inProgress: true,
      // @ts-expect-error it does exist
      file: options.file,
      ossObject: '',
      canceled: false,
      client: await createClient(),
    })
    uploadFiles.value.push(newFile)
    newFile.ossObject = `/vp/source/${newFile.fileUuid}.${getFileExtension(newFile.fileName)}`
    const tokenName = localStorage.getItem('tokenName');
    const tokenValue = localStorage.getItem('tokenValue');

    const result = await newFile.client.multipartUpload(newFile.ossObject, newFile.file, {
      parallel: 1,
      partSize: 5 * 1024 * 1024, // 5MByte
      progress: (p: number, checkpoint: object) => {
        newFile.percentage = parseFloat((p * 100).toFixed(2))
        newFile.checkpoint = checkpoint
      },
      headers,
      callback: {
        // 设置回调请求的服务器地址，例如http://oss-demo.aliyuncs.com:23450。
        url: import.meta.env.VITE_HTTP_BASEURL as string + "/api/oss/upload-callback",
        //（可选）设置回调请求消息头中Host的值，即您的服务器配置Host的值。
        //host: 'yourCallbackHost',
        // 设置发起回调时请求body的值。
        body: "fileName=${x:fileName}&fileUuid=${x:fileUuid}&fileExtension=${x:fileExtension}&filePath=${x:filePath}&userUuid=${x:userUuid}",
        // 设置发起回调请求的Content-Type。
        contentType: "application/x-www-form-urlencoded",
        customValue: {
          fileName: newFile.fileName,
          fileUuid: newFile.fileUuid,
          fileExtension: getFileExtension(newFile.fileName),
          filePath: '/vp/source/',
          userUuid: localStorage.getItem('userUuid')
        },
        headers: {
          [tokenName!]: tokenValue
        }
      }
    })
    console.log('上传成功:', result)
    uploadSuccess(newFile)
  } catch (e) {
    console.log(e)
  }
}

function uploadSuccess(file: UploadFile) {
  file.inProgress = false
  file.percentage = 100.0
  // apiClient.post<HttpResponse<object>, UploadCallback>('/api/oss/upload-callback', {
  //   fileName: file.fileName,
  //   fileUuid: file.fileUuid,
  //   fileExtension: getFileExtension(file.fileName),
  //   filePath: '/vp/source/'
  // })
  setTimeout(() => {
    removeFromList(file)
  }, 5000)
}

async function abortOrRemove(file: UploadFile) {
  try {
    file.canceled = true
    if (file.inProgress) {
      // @ts-expect-error it does exist
      await file.client.cancel()
    }
    removeFromList(file)
  } catch (e) {
    console.log(e)
  }
}

function removeFromList(file: UploadFile) {
  // 从列表中移除文件
  const index = uploadFiles.value.findIndex((f) => f.fileUuid === file.fileUuid)
  if (index !== -1) {
    uploadFiles.value.splice(index, 1)
  }
}
</script>

<template>
  <PageHeader />
  <el-main class="main">
    <el-upload
      class="upload"
      ref="uploadRef"
      drag
      multiple
      action="#"
      :http-request="upload"
      :show-file-list="false"
      auto-upload
      v-show="true"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">Drop file here or <em>click to upload</em></div>
      <div class="el-upload__text">最大文件大小2GB</div>
    </el-upload>

    <div class="progress" v-for="file in uploadFiles" :key="file.fileUuid">
      <div v-if="!file.canceled">
        <div class="name">
          <span style="font-size: 1em">{{ file.fileName }}</span>
          <el-button
            v-show="file.inProgress"
            type="danger"
            :icon="Delete"
            circle
            @click="abortOrRemove(file)"
          />
          <el-button
            v-show="file.percentage == 100"
            type="success"
            :icon="Check"
            circle
            style="pointer-events: none"
          />
        </div>
        <el-progress
          :text-inside="true"
          :stroke-width="26"
          :percentage="file.percentage"
          :status="file.percentage == 100 ? 'success' : ''"
        />
      </div>
    </div>
  </el-main>
</template>

<style scoped>
.progress .name {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.main {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}
</style>
