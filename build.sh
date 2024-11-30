#!/bin/bash
mvn clean install -Dgpg.skip -DskipTests && \
  find . -type d -name 'target' -exec rm -rf {} \; &&
  find . -type d -name 'javadoc' -exec rm -rf {} \;
