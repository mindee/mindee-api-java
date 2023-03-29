#! /bin/sh
set -e

OUTPUT_FILE='SimpleMindeeClient.java'
ACCOUNT=$1
ENDPOINT=$2
API_KEY=$3

for f in `find docs/code_samples -name "*.txt"`
do
  echo $f
  echo "###############################################"

  if echo "$f" | grep -q "custom_v1.txt" || echo "$f" | grep -q "invoices_v4.txt" || echo "$f" | grep -q "default.txt"
      then
        echo "custom_v1 or invoices_v4 or default"
      else
        continue
  fi

  cat $f > $OUTPUT_FILE

  if echo "$f" | grep -q "custom_v1.txt"
  then
    sed -i "s/my-account/$ACCOUNT/g" $OUTPUT_FILE
    sed -i "s/my-endpoint/$ENDPOINT/g" $OUTPUT_FILE
  fi

  if echo "$f" | grep -q "default.txt"
  then
    sed -i "s/my-endpoint/bank_account_details/" $OUTPUT_FILE
    sed -i "s/my-account/mindee/" $OUTPUT_FILE
    sed -i "s/my-version/1/" $OUTPUT_FILE
  fi

  if echo "$f" | grep -q "invoices_v4.txt"
  then
    sed -i "s/my-api-key/$API_KEY/" $OUTPUT_FILE
    sed -i "s/\/path\/to\/the\/file.ext/src\/test\/resources\/data\/pdf\/blank_1.pdf/" $OUTPUT_FILE
  fi
  javac -cp ./target/dependency/*:./target/* SimpleMindeeClient.java
  java -cp  .:./target/dependency/*:./target/* SimpleMindeeClient

done
