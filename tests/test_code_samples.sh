#! /bin/sh
set -e

OUTPUT_FILE='SimpleMindeeClient.java'
ACCOUNT=$1
ENDPOINT=$2
API_KEY=$3

if [ -z "${ACCOUNT}" ]; then echo "ACCOUNT is required"; exit 1; fi
if [ -z "${ENDPOINT}" ]; then echo "ENDPOINT is required"; exit 1; fi

# We need the dependencies otherwise we get class not found exceptions
mvn dependency:copy-dependencies

for f in $(find docs/code_samples -maxdepth 1 -name "*.txt" | sort -h)
do
  echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
  echo "${f}"
  echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
  echo

  cat "${f}" > $OUTPUT_FILE

  if echo "${f}" | grep -q "custom_v1.txt"
  then
    sed -i "s/my-account/$ACCOUNT/g" $OUTPUT_FILE
    sed -i "s/my-endpoint/$ENDPOINT/g" $OUTPUT_FILE
  fi

  if echo "${f}" | grep -q "default.txt"
  then
    sed -i "s/my-account/$ACCOUNT/" $OUTPUT_FILE
    sed -i "s/my-endpoint/$ENDPOINT/" $OUTPUT_FILE
    sed -i "s/my-version/1/" $OUTPUT_FILE
  fi

  if echo "${f}" | grep -q "default_async.txt"
  then
    sed -i "s/my-account/mindee/" $OUTPUT_FILE
    sed -i "s/my-endpoint/invoice_splitter/" $OUTPUT_FILE
    sed -i "s/my-version/1/" $OUTPUT_FILE
  fi

  sed -i "s/my-api-key/$API_KEY/" $OUTPUT_FILE
  sed -i "s/\/path\/to\/the\/file.ext/src\/test\/resources\/file_types\/pdf\/blank_1.pdf/" $OUTPUT_FILE

  javac -cp ./target/dependency/*:./target/* SimpleMindeeClient.java
  java -cp  .:./target/dependency/*:./target/* SimpleMindeeClient

done
