FROM ubuntu:latest

#install netcat
RUN apt-get install -y netcat

EXPOSE 6900
CMD    ["nc", "-l", "6900"]