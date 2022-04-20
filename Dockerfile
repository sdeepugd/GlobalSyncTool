FROM tomcat:9.0

ADD target/globalsynctool.war /usr/local/tomcat/webapps/ROOT.war

# ssh
ENV SSH_PASSWD "root:Docker!"
RUN apt-get update \
        && apt-get install -y --no-install-recommends dialog \
        && apt-get update \
 && apt-get install -y --no-install-recommends openssh-server \
 && echo "$SSH_PASSWD" | chpasswd 

RUN cd /usr/local/tomcat/webapps/ \
        && rm -rf ROOT \
        && mkdir ROOT \
        && cd ROOT \
        && unzip /usr/local/tomcat/webapps/ROOT.war 

RUN echo "Build Updated On `date`" > /usr/local/tomcat/webapps/ROOT/index.jsp

RUN mv /usr/local/tomcat/conf/logging.properties /usr/local/tomcat/conf/logging.properties.bak  \
        && cp /usr/local/tomcat/webapps/ROOT/WEB-INF/logging.properties /usr/local/tomcat/conf/logging.properties

COPY sshd_config /etc/ssh/
COPY init.sh /usr/local/bin/

RUN chmod u+x /usr/local/bin/init.sh
EXPOSE 8080 2222

ENTRYPOINT ["init.sh"]