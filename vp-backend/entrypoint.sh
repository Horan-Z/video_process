#!/bin/sh

echo "开始挂载OSS..."
ossfs xiaoming10086 /tmp/ossfs -o url=http://oss-cn-beijing.aliyuncs.com

# 检查挂载是否成功
if mount | grep /tmp/ossfs > /dev/null; then
    echo "OSS挂载成功，准备启动Java应用..."
    java -jar /app/app.jar
else
    echo "OSS挂载失败！"
    exit 1
fi