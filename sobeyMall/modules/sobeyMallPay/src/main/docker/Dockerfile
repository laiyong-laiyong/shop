FROM java:8
MAINTAINER WCY
#设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' > /etc/timezone

RUN mkdir  /usr/local/sobeyMallPay/
RUN mkdir  /usr/local/sobeyMallPay/crt
RUN mkdir  /usr/local/sobeyMallPay/crt/zfb
WORKDIR    /usr/local/sobeyMallPay
ADD sobeyMallPay-1.0.jar /usr/local/sobeyMallPay/app.jar
#拷贝支付宝的证书
ADD crt/* /usr/local/sobeyMallPay/crt/zfb/
ENTRYPOINT ["java","-jar","/usr/local/sobeyMallPay/app.jar"]
