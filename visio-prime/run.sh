#!/bin/sh
echo $JAVA_HOME
if [ -n "$JAVA_HOME" ] ;then
    RUNJAVA=$JAVA_HOME/bin/java
else
    echo JAVA_HOME is not set. Trying to use default PATH
    RUNJAVA=java
fi

$RUNJAVA -jar visio.jar