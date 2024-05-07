# syntax = docker/dockerfile:1.2

FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.4

RUN apt-get update && apt-get install -yq make unzip

RUN --mount=type=secret,id=private.pem,dst=/etc/secrets/private.pem

RUN --mount=type=secret,id=public.pem,dst=/etc/secrets/public.pem

RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle

RUN mv gradle-${GRADLE_VERSION} ${GRADLE_HOME}

ENV PATH=$PATH:$GRADLE_HOME/bin

WORKDIR /project
RUN mkdir /project/code

ENV GRADLE_USER_HOME /project/.gradle

COPY . .

RUN gradle installDist

CMD build/install/talent/bin/talent

