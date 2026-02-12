#! /bin/sh
set -e

OUTPUT_FILE='SimpleMindeeClientV2.java'

# We need the dependencies otherwise we get class not found exceptions
mvn dependency:copy-dependencies

for f in $(
  find docs/code_samples -maxdepth 1 -name "v2_*.txt" | sort -h
)
do
  echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
  echo "${f}"
  echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
  echo

  sed "s/MY_API_KEY/${MINDEE_V2_API_KEY}/" "${f}" > $OUTPUT_FILE
  sed -i "s/\/path\/to\/the\/file.ext/src\/test\/resources\/file_types\/pdf\/blank_1.pdf/" $OUTPUT_FILE

  cat "${f}" > $OUTPUT_FILE

  if echo "${f}" | grep -q "v2_classification.txt"
  then
    sed -i "s/MY_MODEL_ID/${MINDEE_V2_SE_TESTS_CLASSIFICATION_MODEL_ID}/" $OUTPUT_FILE
  fi

  if echo "${f}" | grep -q "v2_crop.txt"
  then
    sed -i "s/MY_MODEL_ID/${MINDEE_V2_SE_TESTS_CROP_MODEL_ID}/" $OUTPUT_FILE
  fi

  if echo "${f}" | grep -q "v2_extraction.txt"
  then
    sed -i "s/MY_MODEL_ID/${MINDEE_V2_SE_TESTS_FINDOC_MODEL_ID}/" $OUTPUT_FILE
  fi

  if echo "${f}" | grep -q "v2_ocr.txt"
  then
    sed -i "s/MY_MODEL_ID/${MINDEE_V2_SE_TESTS_OCR_MODEL_ID}/" $OUTPUT_FILE
  fi

  if echo "${f}" | grep -q "v2_split.txt"
  then
    sed -i "s/MY_MODEL_ID/${MINDEE_V2_SE_TESTS_SPLIT_MODEL_ID}/" $OUTPUT_FILE
  fi

  sleep 0.5  # avoid too many request errors
    javac -cp ./target/dependency/*:./target/* "${OUTPUT_FILE}"
  java -cp  .:./target/dependency/*:./target/* SimpleMindeeClient

done
