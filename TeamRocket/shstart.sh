#!/bin/bash

sudo echo "RUNNING Startup SCRIPT" && date
sudo date >> startlog.txt
sudo node-red-start
