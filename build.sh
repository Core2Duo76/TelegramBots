#!/bin/bash
mvn clean install -Dgpg.skip -DskipTests -Dmaven.javadoc.skip=true && \
find . -type d -name 'target' && \
find . -type d -name 'target' -exec rm -rv {} +
