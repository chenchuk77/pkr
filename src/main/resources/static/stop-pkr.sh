#!/bin/bash
OLD_PID=$(ps -ef  | grep pkr | grep jar | awk '{ print $2}') 
kill -9 ${OLD_PID}
