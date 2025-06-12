<script setup lang="ts">
import PageHeader from '@/components/PageHeader.vue'
import { ref } from 'vue'

const step = ref(1)
const search = ref('')
const tableData = [
  {
    date: '2016-05-03',
    name: 'Tom',
    state: 'California',
    city: 'San Francisco',
    address: '3650 21st St, San Francisco',
    zip: 'CA 94114',
    family: [
      {
        name: 'Jerry',
        state: 'California',
        city: 'San Francisco',
        address: '3650 21st St, San Francisco',
        zip: 'CA 94114',
      },
      {
        name: 'Spike',
        state: 'California',
        city: 'San Francisco',
        address: '3650 21st St, San Francisco',
        zip: 'CA 94114',
      },
      {
        name: 'Tyke',
        state: 'California',
        city: 'San Francisco',
        address: '3650 21st St, San Francisco',
        zip: 'CA 94114',
      },
    ],
  },
]
</script>

<template>
  <page-header />
  <template v-if="step == 1">
    <h2 class="step">第一步: 选择视频</h2>
    <el-table :data="tableData" :border="false" :preserve-expanded-content="false" class="table">
      <el-table-column type="expand">
        <template #default="props">
          <div>
            <p>State: {{ props.row.state }}</p>
            <p>City: {{ props.row.city }}</p>
            <p>Address: {{ props.row.address }}</p>
            <p>Zip: {{ props.row.zip }}</p>
            <h3>Family</h3>
            <el-table :data="props.row.family" :border="false">
              <el-table-column label="Name" prop="name" />
              <el-table-column label="State" prop="state" />
              <el-table-column label="City" prop="city" />
              <el-table-column label="Address" prop="address" />
              <el-table-column label="Zip" prop="zip" />
            </el-table>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Date" prop="date" />
      <el-table-column label="Name" prop="name" />
      <el-table-column align="right">
        <template #header>
          <el-input
            v-model="search"
            size="small"
            placeholder="文件名模糊查找(还没做,准备用es实现)"
          />
        </template>
        <template #default="scope">
          <el-button size="small" @click="handleSelect(scope.$index, scope.row)"> 选择</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
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
</style>
