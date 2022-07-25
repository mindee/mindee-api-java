#! /usr/bin/env sh
set -e

if ! ls ./target/libs/picocli-* 2>&1 >/dev/null; then
    mvn dependency:copy-dependencies -DoutputDirectory=target/libs -Dhttps.protocols=TLSv1.2
fi

java -cp "target/mindee-api-java-1.0-SNAPSHOT.jar:target/libs/*" com.mindee.CLI CLI "$@"
