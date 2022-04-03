FROM tomcat:9.0

ADD target/globalsynctool.war /usr/local/tomcat/webapps/ROOT.war

# ssh
ENV SSH_PASSWD "root:Docker!"
RUN apt-get update \
        && apt-get install -y --no-install-recommends dialog \
        && apt-get update \
 && apt-get install -y --no-install-recommends openssh-server \
 && echo "$SSH_PASSWD" | chpasswd 

RUN cd /usr/local/tomcat/webapps/ && rm -rf ROOT && mkdir ROOT && cd ROOT && unzip /usr/local/tomcat/webapps/ROOT.war 

COPY sshd_config /etc/ssh/

EXPOSE 8080 2222

CMD ["catalina.sh", "run"]