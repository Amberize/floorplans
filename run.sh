#! /bin/bash

./gradlew clean build

java -jar ./page-extractor/build/libs/page-extractor-1.0-SNAPSHOT.jar
java -jar ./image-processor/build/libs/image-processor-1.0-SNAPSHOT.jar
java -jar ./csv-merger/build/libs/csv-merger-1.0-SNAPSHOT.jar