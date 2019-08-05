#!/bin/bash

# This script uploads the APK resulting from a CI build to Telegram
. buildReleaseApp.sh
version_name=$(cat app/build.gradle | grep "versionCode" | awk '{print $2}' | cut -d '=' -f1)
sha_hash=$(git rev-parse --short HEAD)
cp app/build/outputs/apk/release/WPclassroom-*.apk app/build/outputs/apk/release/WPclassroom-"$version_name"_"$sha_hash".apk
apk=app/build/outputs/apk/release/WPclassroom-"$version_name"_"$sha_hash".apk

function notify() {
  # Notify via Telegram
  if [ "$1" = "success" ]
  then
  
tg_message="[$sha_hash](https://github.com/ishubhamsingh/WPclassroom_client/commit/$sha_hash) passed"
  else
  
tg_message="[$sha_hash](https://github.com/ishubhamsingh/WPclassroom_client/commit/$sha_hash) failed"
  fi
  curl -F chat_id="$TG_GRP_ID" -F parse_mode="markdown" -F text="$tg_message" "https://api.telegram.org/bot$TG_BOT_ID/sendMessage"
}

function sendfile() {
  curl -F chat_id="$TG_GRP_ID" -F document="@$apk" "https://api.telegram.org/bot$TG_BOT_ID/sendDocument"
}

function sendChangelog() {
curl -F chat_id="$TG_GRP_ID" -F parse_mode="HTML" -F text="$(. changelog.sh)" "https://api.telegram.org/bot$TG_BOT_ID/sendMessage"
}


if [ -f "$apk" ];
then
notify success
sendChangelog
sendfile
exit 0
else
notify failure
exit 1
fi
