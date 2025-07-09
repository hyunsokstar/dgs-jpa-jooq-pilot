FROM ubuntu:latest
LABEL authors="terec"

ENTRYPOINT ["top", "-b"]