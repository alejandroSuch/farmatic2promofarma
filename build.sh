#!/bin/bash

rm -rf src/main/resources/static/
npm --prefix ./src/main/angular install
npm --prefix ./src/main/angular run deploy
mvn clean install spring-boot:repackage -DskipTests
