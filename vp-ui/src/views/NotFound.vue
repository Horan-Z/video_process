<template>
  <el-result icon="error" title="404" subTitle="页面不存在！" style="width: 100%; height: 80vh">
    <template #extra>
      <el-button type="primary" size="default" @click="onNavigateBack">返回</el-button>
    </template>
  </el-result>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useRouter } from 'vue-router'

export default defineComponent({
  name: '404-Not-Found',

  setup() {
    const router = useRouter()
    const onNavigateBack = () => {
      router.back()
    }

    return {
      onNavigateBack,
    }
  },
})
</script>
