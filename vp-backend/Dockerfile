FROM alibaba-cloud-linux-3-registry.cn-hangzhou.cr.aliyuncs.com/alinux3/alinux3

# 设置端口号
ENV SPRING_PORT=8080

ENV DEBIAN_FRONTEND=noninteractive
ENV JAVA_VERSION=17 \
    JAVA_HOME=/usr/lib/jvm/bellsoft-java17-full-amd64 \
    PATH=$PATH:/usr/lib/jvm/bellsoft-java17-full-amd64/bin

RUN echo "[BELLSOFT]" > /etc/yum.repos.d/bellsoft.repo && \
    echo "name=BELLSOFT Repository" >> /etc/yum.repos.d/bellsoft.repo && \
    echo "baseurl=https://yum.bell-sw.com" >> /etc/yum.repos.d/bellsoft.repo && \
    echo "enabled=1" >> /etc/yum.repos.d/bellsoft.repo && \
    echo "gpgcheck=1" >> /etc/yum.repos.d/bellsoft.repo && \
    echo "gpgkey=https://download.bell-sw.com/pki/GPG-KEY-bellsoft" >> /etc/yum.repos.d/bellsoft.repo && \
    echo "priority=1" >> /etc/yum.repos.d/bellsoft.repo && \
    dnf makecache && \
    dnf install -y bellsoft-java17-runtime

RUN dnf install -y epel-release && \
    dnf install -y https://download1.rpmfusion.org/free/el/rpmfusion-free-release-8.noarch.rpm && \
    dnf install -y ffmpeg

RUN dnf install -y wget && \
    wget https://gosspublic.alicdn.com/ossfs/ossfs2_2.0.1beta_linux_x86_64.rpm && \
    dnf install -y ossfs2_2.0.1beta_linux_x86_64.rpm

WORKDIR /app

COPY target/vp-backend.jar app.jar

EXPOSE ${SPRING_PORT}

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]