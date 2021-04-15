FROM java:8
MAINTAINER lgc
RUN mkdir  /sobeyMall
#这里设置运行时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
#
WORKDIR    /sobeyMall
ADD sobeyMallWorkOrder-1.0.jar /sobeyMall/
CMD java -jar /sobeyMall/sobeyMallWorkOrder-1.0.jar

