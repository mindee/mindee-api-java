#!/bin/sh
set -e

TEST_FILE=$1

if [ -z "$TEST_FILE" ]; then
  TEST_FILE='./src/test/resources/file_types/pdf/blank_1.pdf'
fi
echo "TEST_FILE: ${TEST_FILE}"

PRODUCTS="financial-document receipt invoice invoice-splitter"
PRODUCTS_SIZE=4
i=1

for product in $PRODUCTS
do
  echo "--- Test $product with Summary Output ($i/$PRODUCTS_SIZE) ---"
  SUMMARY_OUTPUT=$(./cli.sh v1 "$product" "$TEST_FILE")
  echo "$SUMMARY_OUTPUT"
  echo ""
  echo ""
  sleep 0.5
  i=$((i + 1))
done
