#!/bin/sh

ENDPOINT="https://oss-cn-beijing-internal.aliyuncs.com"
BUCKET_NAME="bucket_name"
ACCESS_KEY_ID="LTAI********************"
ACCESS_KEY_SECRET="ojSr**************************"

echo "开始挂载OSS..."
rm -f /etc/ossfs2.conf
echo "--oss_endpoint=$ENDPOINT" > /etc/ossfs2.conf
echo "--oss_bucket=$BUCKET_NAME" >> /etc/ossfs2.conf
echo "--oss_access_key_id=$ACCESS_KEY_ID" >> /etc/ossfs2.conf
echo "--oss_access_key_secret=$ACCESS_KEY_SECRET" >> /etc/ossfs2.conf
chmod 640 /etc/ossfs2.conf
rm -rf /tmp/ossfs2-bucket
mkdir /tmp/ossfs2-bucket
ossfs2 mount /tmp/ossfs2-bucket/ -c /etc/ossfs2.conf

# 检查挂载是否成功
if mount | grep /tmp/ossfs2-bucket > /dev/null; then
    echo "OSS挂载成功，准备启动Java应用..."
    java -jar /app/app.jar
else
    echo "OSS挂载失败！"
    exit 1
fi