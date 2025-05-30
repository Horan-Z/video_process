<script setup lang="ts">
import { UploadFilled, Delete } from '@element-plus/icons-vue'
import OSS from 'ali-oss'
import axios from 'axios'
import { reactive, ref } from 'vue'
import { v4 as uuidv4 } from 'uuid';
import PageHeader from '@/components/PageHeader.vue'

interface UploadFile {
  fileUuid: string;     // 唯一标识
  fileName: string;     // 文件名
  percentage: number;   // 上传进度
  checkpoint: object | null;   // 分片上传checkpoint
  inProgress: boolean;  // 是否上传中
  uploadFinish: boolean;// 是否上传完成
  file: File;           // 文件对象
  ossObject: string;
  canceled: boolean;
  client: OSS;
}

const uploadFiles = ref<UploadFile[]>([]);

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
  const token = await fetchSTSToken()
  return new OSS({
    region: 'oss-cn-beijing',
    accessKeyId: token.credentials.accessKeyId,
    accessKeySecret: token.credentials.accessKeySecret,
    stsToken: token.credentials.securityToken,
    // @ts-expect-error idk
    authorizationV4: true,
    bucket: 'xiaoming10086',
    refreshSTSToken: async () => {
      const token = await fetchSTSToken()
      return {
        accessKeyId: token.credentials.accessKeyId,
        accessKeySecret: token.credentials.accessKeySecret,
        stsToken: token.credentials.securityToken,
      }
    },
  })
}

// 获取STS令牌
async function fetchSTSToken() {
  const response = await axios.get('http://localhost:8080/api/sts')
  return response.data.data
}

function getFileExtension(filename: string) {
  const parts = filename.split('.');
  return parts.length > 1 ? parts.pop() : '';
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
      uploadFinish: false,
      // @ts-expect-error it does exist
      file: options.file,
      ossObject: '',
      canceled: false,
      client: await createClient()
    })
    uploadFiles.value.push(newFile)
    newFile.ossObject = `/vp/source/${newFile.fileUuid}.${getFileExtension(newFile.fileName)}`
    const result = await newFile.client.multipartUpload(newFile.ossObject, newFile.file, {
      // parallel: 1,
      partSize: 5 * 1024 * 1024, // 5MByte
      progress: (p: number, checkpoint: object) => {
        newFile.percentage = parseFloat((p * 100).toFixed(2))
        newFile.checkpoint = checkpoint
      },
      headers,
    })
    console.log('上传成功:', result)
    uploadSuccess(newFile)
  } catch (e) {
    console.log(e)
  }
}

function uploadSuccess(newFile: UploadFile) {
  newFile.inProgress = false
  newFile.uploadFinish = true
  newFile.percentage = 100.00
}

async function abortOrRemove(file: UploadFile) {
  try{
    file.canceled = true
    if(file.inProgress) {
      // // @ts-expect-error it does exist
      // await file.client.abortMultipartUpload(file.checkpoint.name, file.checkpoint.uploadId)
      // @ts-expect-error it does exist
      await file.client.cancel()
    } else if(file.uploadFinish) {
      console.log('remove')
      // TODO remove logic
    }
    // 从列表中移除文件
    const index = uploadFiles.value.findIndex(f => f.fileUuid === file.fileUuid);
    if (index !== -1) {
      uploadFiles.value.splice(index, 1);
    }
  } catch(e) {
    console.log(e)
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
      <div  v-if="!file.canceled">
        <div class="name">
          <span style="font-size: 1.8em;">{{ file.fileName }}</span>
          <el-button type="danger" :icon="Delete" circle @click="abortOrRemove(file)" />
        </div>
        <el-progress :text-inside="true" :stroke-width="26" :percentage="file.percentage" :status="file.percentage == 100 ? 'success' : ''" />
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
