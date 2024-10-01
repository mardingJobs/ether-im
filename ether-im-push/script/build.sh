#!/bin/zsh

SRC_DIR="./input"
DST_DIR="../src/main/java/"

./protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/*.proto
