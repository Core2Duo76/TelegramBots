#!/bin/bash
mvn clean install -Dgpg.skip -DskipTests -Dmaven.javadoc.skip=true && \
echo 1 && \
find . -type d -name 'target' && \
find . -type d -name 'target' -exec rm -rv {} +
