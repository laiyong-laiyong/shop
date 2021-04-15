FROM java:8
MAINTAINER WCY
#下边两行命令设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' > /etc/timezone
ADD sobeyMallOrder-1.0.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
