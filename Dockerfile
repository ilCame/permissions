FROM ubuntu:latest
LABEL authors="root"

EXPOSE 8087
ENTRYPOINT ["top", "-b"]