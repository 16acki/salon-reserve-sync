#!/bin/bash

apt-get update

# Google Chrome の仕込み
TIME_ZONE_TOKYO=Asia/Tokyo
ln -snf /usr/share/zoneinfo/$TIME_ZONE_TOKYO /etc/localtime && echo $TIME_ZONE_TOKYO > /etc/timezone

apt-get install -y wget gnupg2
wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -

echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list
wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
apt-get install --reinstall -y -f ./google-chrome-stable_current_amd64.deb

# 掃除
rm -rf ./*.sql ./google-chrome-stable_current_amd64.deb
