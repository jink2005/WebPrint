#! /bin/bash
parentDir=`dirname $0`
nohup java -jar $parentDir/WebPrint.jar > WebPrint.log 2>&1 &