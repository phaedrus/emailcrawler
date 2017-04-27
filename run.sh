#!/bin/sh

./gradlew run -q -Pmyargs=$1 2>/dev/null
