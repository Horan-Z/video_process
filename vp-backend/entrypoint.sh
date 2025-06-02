#!/bin/sh

REGION="oss-cn-beijing"
BUCKET_NAME="bucket_name"
ACCESS_KEY_ID="LTAI********************"
ACCESS_KEY_SECRET="ojSr**************************"

echo "开始挂载OSS..."
echo $BUCKET_NAME:$ACCESS_KEY_ID:$ACCESS_KEY_SECRET > /etc/passwd-ossfs
chmod 640 /etc/passwd-ossfs
mkdir /tmp/ossfs
ossfs $BUCKET_NAME /tmp/ossfs -o url=http://$REGION.aliyuncs.com

# 检查挂载是否成功
if mount | grep /tmp/ossfs > /dev/null; then
    echo "OSS挂载成功，准备启动Java应用..."
    java -jar /app/app.jar
else
    echo "OSS挂载失败！"
    exit 1
fi