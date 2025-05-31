# 使用Alibaba Cloud Linux 3作为基础镜像
FROM alibaba-cloud-linux-3-registry.cn-hangzhou.cr.aliyuncs.com/alinux3/alinux3:latest

LABEL authors="xiaoming10086"

ENV JAVA_VERSION=17 \
    JAVA_HOME=/usr/lib/jvm/bellsoft-java17-full-amd64 \
    PATH=$PATH:/usr/lib/jvm/bellsoft-java17-full-amd64/bin

RUN yum makecache && \
    yum update -y && \
    yum install -y wget tar gzip && \
    echo '[BELLSOFT]' > /etc/yum.repos.d/bellsoft.repo && \
    echo 'name=BELLSOFT Repository' >> /etc/yum.repos.d/bellsoft.repo && \
    echo 'baseurl=https://yum.bell-sw.com' >> /etc/yum.repos.d/bellsoft.repo && \
    echo 'enabled=1' >> /etc/yum.repos.d/bellsoft.repo && \
    echo 'gpgcheck=1' >> /etc/yum.repos.d/bellsoft.repo && \
    echo 'gpgkey=https://download.bell-sw.com/pki/GPG-KEY-bellsoft' >> /etc/yum.repos.d/bellsoft.repo && \
    echo 'priority=1' >> /etc/yum.repos.d/bellsoft.repo && \
    yum install -y bellsoft-java17-runtime && \
    yum install -y automake gcc-c++ git libcurl-devel libxml2-devel fuse-devel make openssl-devel && \
    yum clean all && \
    rm -rf /var/cache/yum && \
    git clone https://github.com/aliyun/ossfs.git && \
    cd ossfs && \
    ./autogen.sh && \
    ./configure && \
    make && \
    make install && \
    cd && \
    echo xiaoming10086:LTAI5t9T98FuQVWPBfVGDapB:WeudJ9iP5Gsqk82HW3umPtJIvULkSz > /etc/passwd-ossfs && \
    chmod 640 /etc/passwd-ossfs && \
    mkdir /tmp/ossfs

WORKDIR /app

COPY target/vp-backend.jar app.jar

EXPOSE 8080

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]