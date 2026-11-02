#!/bin/sh
cd "$(dirname "$0")" || exit 1

if ! command -v javac >/dev/null 2>&1 || ! command -v java >/dev/null 2>&1; then
    echo "JDK 17 or newer is required. Install a JDK and add its bin folder to PATH."
    exit 1
fi

if [ "$#" -ne 3 ]; then
    echo "Usage: ./run.sh <file.jpg> <square size> <S or M>"
    echo "Example: ./run.sh image.jpg 20 S"
    exit 1
fi

sources="${TMPDIR:-/tmp}/pla4-sources.txt"
mkdir -p out
find src -name "*.java" > "$sources"
javac --release 17 -d out "@$sources"
status=$?
rm -f "$sources"
if [ "$status" -ne 0 ]; then
    exit "$status"
fi

exec java -cp out Main "$@"
