<script setup lang="ts">
import PageHeader from '@/components/PageHeader.vue'
import { onMounted, ref } from 'vue'
import apiClient from '@/api/httpClient.ts'
import type { HttpResponse } from '@/types/Response'
import { format } from 'date-fns'
import axios from 'axios'
import _ from 'lodash'
import { useDebounceFn } from '@vueuse/core'

const step = ref(1)
const search = ref('')
const tableData = ref([])
const pageNumber = ref(1)
const pageSize = ref(10)
// TODO 搜索排序
const sortType = ref('NAME_ASC')
const totalCount = ref(1)
const selected = ref(0)
const refreshPage = ref(false)

interface listData {
  count: number
  page: {
    records: []
  }
}

interface rowObject {
  id: number
}

function snakeToCamelLodash(obj: object): object {
  if (Array.isArray(obj)) {
    return obj.map((item) => snakeToCamelLodash(item))
  } else if (obj !== null && typeof obj === 'object') {
    return Object.keys(obj).reduce((result, key) => {
      // @ts-expect-error idk
      result[_.camelCase(key)] = snakeToCamelLodash(obj[key])
      return result
    }, {})
  }
  return obj
}

async function fetchData() {
  if (refreshPage.value) {
    pageNumber.value = 1
    refreshPage.value = false
  }
  if (search.value == '') {
    const res = await apiClient.get<HttpResponse<listData>>(
      `/video/list?pageNum=${pageNumber.value}&pageSize=${pageSize.value}&sortType=${sortType.value}`,
    )
    totalCount.value = res.data.count
    tableData.value = res.data.page.records
  } else {
    console.log(`searching: ${search.value}`)
    const res = await axios.post(
      `${import.meta.env.VITE_ES_BASEURL as string}/file_index/_search`,
      {
        query: {
          match_phrase: {
            file_name: `${search.value}`,
          },
        },
        from: 10 * (pageNumber.value - 1),
        size: 10,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      },
    )
    console.log(res)
    totalCount.value = res.data.hits.total.value
    // @ts-expect-error it does
    const sourceData = res.data.hits.hits.map((hit: object) => hit._source)
    // @ts-expect-error idk
    tableData.value = snakeToCamelLodash(sourceData)
  }
}

onMounted(() => {
  fetchData()
})

function formatDate(row: number, column: number, cellValue: string): string {
  return format(new Date(cellValue), 'yyyy-MM-dd:HH:mm:ss')
}

function videoDuration(millis: number): string {
  const minutes = Math.floor(millis / 1000 / 60)
  const seconds = Math.floor((millis / 1000) % 60)
  if (minutes <= 0) {
    return `${seconds}秒`
  } else {
    return `${minutes}分${seconds}秒`
  }
}

function changePage(page: number) {
  pageNumber.value = page
  fetchData()
}

function handleSelect(row: rowObject) {
  selected.value = row.id
}

const debouncedSearch = useDebounceFn(fetchData, 500)

function handleInput() {
  refreshPage.value = true
  debouncedSearch()
}

async function handleDelete(row: rowObject) {
  await apiClient.delete(`/video/${row.id}`)
  await fetchData()
}
</script>

<template>
  <page-header />
  <template v-if="step == 1">
    <h2 class="step">第一步: 选择视频</h2>
    <el-table :data="tableData" :border="false" :preserve-expanded-content="false" class="table">
      <el-table-column type="expand">
        <template #default="props">
          <div style="margin-left: 60px">
            <p>文件大小: {{ (props.row.fileSizeBytes / 1024).toFixed(2) }} MB</p>
            <p>视频编码格式: {{ props.row.videoCodec }}</p>
            <p>视频封装格式: {{ props.row.videoFormat }}</p>
            <p>视频时长: {{ videoDuration(props.row.videoDurationMillis) }}</p>
            <p>视频比特率: {{ props.row.videoBitrate / 1000 }} Mbps</p>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="添加日期" prop="createTime" :formatter="formatDate" />
      <el-table-column label="文件名" prop="fileName" />
      <el-table-column align="right">
        <template #header>
          <el-input
            v-model="search"
            size="small"
            placeholder="文件名模糊查找"
            clearable
            @input="handleInput"
          />
        </template>
        <template #default="scope">
          <el-button size="small" @click="handleSelect(scope.row)">选择</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)"> 删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-container">
      <el-pagination
        background
        layout="prev, pager, next"
        :hide-on-single-page="true"
        :page-size="pageSize"
        :pager-count="7"
        :total="totalCount"
        :current-page="pageNumber"
        @current-change="changePage"
      />
    </div>
  </template>
</template>

<style scoped>
.step {
  width: 100%;
  max-width: 1000px;
  margin: 10px auto;
}

.table {
  width: 100%;
  max-width: 1000px;
  margin: 10px auto;
}

.page-container {
  width: 100%;
  max-width: 1000px;
  margin: 10px auto;
  display: flex;
  justify-content: center;
}
</style>
