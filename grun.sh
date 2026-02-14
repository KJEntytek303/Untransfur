#!/bin/bash
./gradlew build --info --stacktrace && \
./gradlew runClient > /dev/null 2> /dev/null &
