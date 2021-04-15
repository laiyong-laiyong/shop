FROM java:8
MAINTAINER WCY
#设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' > /etc/timezone

RUN mkdir /sobeyMall
ADD sobeyMallMsg-1.0.jar /sobeyMall/
ENTRYPOINT ["java","-jar","/sobeyMall/sobeyMallMsg-1.0.jar"]
