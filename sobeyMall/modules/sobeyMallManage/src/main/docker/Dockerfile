FROM java:8
MAINTAINER lgc
#设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' > /etc/timezone

RUN mkdir  /sobeyMall
WORKDIR    /sobeyMall
ADD sobeyMallManage-1.0.jar /sobeyMall/
CMD java -jar /sobeyMall/sobeyMallManage-1.0.jar

