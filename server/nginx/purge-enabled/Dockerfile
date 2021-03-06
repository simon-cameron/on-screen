FROM debian:jessie

ENV NGINX_VERSION=1.9.9
ENV NGX_CACHE_PURGE_VERSION=2.3

# Install basic packages and build tools
RUN apt-get update && apt-get install -y \
    wget \
    build-essential \
    libssl-dev \
    libpcre3 \
    libpcre3-dev \
    && apt-get clean

# Get sources

RUN cd /tmp && wget http://nginx.org/download/nginx-$NGINX_VERSION.tar.gz http://labs.frickle.com/files/ngx_cache_purge-$NGX_CACHE_PURGE_VERSION.tar.gz && \
    tar -zxvf nginx-$NGINX_VERSION.tar.gz && mv nginx-$NGINX_VERSION nginx && rm nginx-$NGINX_VERSION.tar.gz && \
    tar -zxvf ngx_cache_purge-$NGX_CACHE_PURGE_VERSION.tar.gz && mv ngx_cache_purge-$NGX_CACHE_PURGE_VERSION ngx_cache_purge && rm ngx_cache_purge-$NGX_CACHE_PURGE_VERSION.tar.gz

# Configure and build software
RUN cd /tmp/nginx && ./configure \
    --sbin-path=/usr/local/sbin \
    --conf-path=/etc/nginx/nginx.conf \
    --error-log-path=/var/log/nginx/error.log \
    --http-log-path=/var/log/nginx/access.log \
    --user=www-data \
    --group=www-data \
    --with-http_gzip_static_module \
    --with-http_ssl_module \
    --without-mail_pop3_module \
    --without-mail_imap_module \
    --without-mail_smtp_module \
    --add-module=/tmp/ngx_cache_purge && \
    make && make install

ADD ./config/nginx.conf /etc/nginx/nginx.conf

# Forward request and error logs to docker log collector
RUN ln -sf /dev/stdout /var/log/nginx/access.log && \
    ln -sf /dev/stderr /var/log/nginx/error.log

VOLUME ["/var/cache/nginx"]

CMD ["nginx", "-g", "daemon off;"]