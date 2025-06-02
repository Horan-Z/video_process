FROM ubuntu:22.04

# 设置端口号
ENV SPRING_PORT=8080

ENV DEBIAN_FRONTEND=noninteractive
ENV JAVA_VERSION=17 \
    JAVA_HOME=/usr/lib/jvm/bellsoft-java17-full-amd64 \
    PATH=$PATH:/usr/lib/jvm/bellsoft-java17-full-amd64/bin

RUN apt-get update -y && \
    apt-get install -y wget gpg ffmpeg && \
    mkdir -m 0755 -p /etc/apt/keyrings/ && \
    wget -q -O - https://download.bell-sw.com/pki/GPG-KEY-bellsoft | gpg --dearmor | tee /etc/apt/keyrings/GPG-KEY-bellsoft.gpg > /dev/null && \
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/GPG-KEY-bellsoft.gpg] https://apt.bell-sw.com/ stable main" | tee /etc/apt/sources.list.d/bellsoft.list && \
    apt-get update && \
    apt-get install -y bellsoft-java17-runtime && \
    apt-get install -y automake autotools-dev g++ git libcurl4-gnutls-dev libfuse-dev libssl-dev libxml2-dev make pkg-config && \
    git clone https://github.com/aliyun/ossfs.git && \
    cd ossfs && \
    ./autogen.sh && \
    ./configure && \
    make && \
    make install

WORKDIR /app

COPY target/vp-backend.jar app.jar

EXPOSE ${SPRING_PORT}

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]