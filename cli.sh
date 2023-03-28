#! /usr/bin/env sh
set -e

if ! ls ./target/libs/picocli-* 2>&1 >/dev/null; then
    mvn dependency:copy-dependencies -DoutputDirectory=target/libs -Dhttps.protocols=TLSv1.2
fi

VERSION=$(grep -m1 -o -P '(?<=\<version\>)[0-9.]*(?!/<\/version>)' pom.xml)

java -cp "target/mindee-api-java-${VERSION}.jar:target/libs/*" com.mindee.CommandLineInterface "$@"
