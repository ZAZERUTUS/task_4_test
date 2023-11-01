FROM ubuntu

RUN apt update && apt-get upgrade -y
RUN apt install curl -y
RUN apt install git -y
RUN apt install bash -y
RUN apt install zip -y
RUN apt install maven -y


RUN curl -s "https://get.sdkman.io" | bash
RUN echo 'PATH=$PATH:$HOME/.jenv/bin:$HOME/.sdkman/bin' >> /etc/environment
RUN echo '. $HOME/.sdkman/bin/sdkman-init.sh' >> /etc/environment


RUN bash -c '. /etc/environment && sdk install java 17.0.7-amzn'

RUN apt-get install language-pack-ru -y
RUN echo 'LANG="ru_RU.UTF-8"\nLANGUAGE="ru:en"' >> /etc/default/locale
ENV LANGUAGE ru_RU.UTF-8
ENV LANG ru_RU.UTF-8
ENV LC_ALL ru_RU.UTF-8
RUN locale-gen ru_RU.UTF-8 && dpkg-reconfigure locales
RUN bash -c '. /etc/environment && sdk use java 17.0.7-amzn && sdk install gradle'


WORKDIR /tests
VOLUME /tests
CMD ping 127.0.0.1

#sudo docker pull ubuntu
#sudo docker build -t test .
#docker run --rm -v cd:/tests -it test bash