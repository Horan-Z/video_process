<script setup lang="ts">
import { UploadFilled } from '@element-plus/icons-vue'
import OSS from 'ali-oss'
// import path from 'path'

const client = new OSS({
  // Bucket所在地域
  region: 'oss-cn-beijing',
  // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
  accessKeyId: 'LTAI5tB4d28uuvgGdurjUdrt',
  accessKeySecret: 'R4yg60Am1iQ8lxqoWYc8kCqZ7RDVa2',
  // @ts-expect-error idk
  authorizationV4: true,
  // 填写Bucket名称。
  secure: true,
  bucket: 'xiaoming10086',
})

// 自定义请求头
const headers = {
  // 指定Object的存储类型。
  'x-oss-storage-class': 'Standard',
  // 指定Object的访问权限。
  'x-oss-object-acl': 'private',
  // 通过文件URL访问文件时，指定以附件形式下载文件，下载后的文件名称定义为example.txt。
  'Content-Disposition': 'attachment; filename="example.txt"',
  // 设置Object的标签，可同时设置多个标签。
  'x-oss-tagging': 'Tag1=1&Tag2=2',
  // 指定PutObject操作时是否覆盖同名目标Object。此处设置为true，表示禁止覆盖同名Object。
  'x-oss-forbid-overwrite': 'true',
}

async function put(options: JSON) {
  try {
    // 填写OSS文件完整路径和本地文件的完整路径。OSS文件完整路径中不能包含Bucket名称。
    // 如果本地文件的完整路径中未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
    // @ts-expect-error it dose
    const file = options.file
    // console.log(file)
    const fileName = '/vp/' + file.name
    // console.log(fileName)
    const result = await client.put(
      fileName,
      file,
      // 自定义headers
      { headers },
    )
    console.log(result)
  } catch (e) {
    console.log(e)
  }
}
</script>

<template>
  <el-upload class="upload" drag action="#" :http-request="put" auto-upload>
    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
    <div class="el-upload__text">Drop file here or <em>click to upload</em></div>
    <div class="el-upload__text">最大文件大小2GB</div>
    <template #tip>
      <div class="el-upload__tip">最大文件大小2GB</div>
    </template>
  </el-upload>
</template>
