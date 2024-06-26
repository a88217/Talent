# syntax = docker/dockerfile:1.2

FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.4

RUN apt-get update && apt-get install -yq make unzip

RUN --mount=type=secret,id=private_pem,dst=/etc/secrets/private.pem cat /etc/secrets/private.pem

RUN --mount=type=secret,id=public_pem,dst=/etc/secrets/public.pem cat /etc/secrets/public.pem

RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle

RUN mv gradle-${GRADLE_VERSION} ${GRADLE_HOME}

ENV PATH=$PATH:$GRADLE_HOME/bin

WORKDIR /project
RUN --mount=type=secret,id=private_pem,dst=/etc/secrets/private.pem mkdir /project/code

ENV GRADLE_USER_HOME /project/.gradle

COPY . .

RUN --mount=type=secret,id=private_pem,dst=/etc/secrets/private.pem --mount=type=secret,id=public_pem,dst=/etc/secrets/public.pem gradle installDist

CMD cat /etc/secrets/private.pem && build/install/talent/bin/talent

