#!/bin/bash
OLD_PID=$(ps -ef  | grep pkr | grep jar | awk '{ print $2}')

if [[ -z "${OLD_PID}" ]]; then
  echo "server is not running."
else
  echo "killing pid: ${OLD_PID}."
  ./stop-pkr.sh
fi 

echo "starting pkr ..."
./start-pkr.sh
