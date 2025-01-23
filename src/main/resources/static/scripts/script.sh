#!/bin/bash

SFTP_USER=${SFTP_USER}
SFTP_HOST=${SFTP_HOST}
SFTP_PORT=${SFTP_PORT}
SFTP_PASSWORD=${SFTP_PASSWORD}
SFTP_REMOTE_DIR=${SFTP_REMOTE_DIR}
LOCAL_DIR=${LOCAL_DIR}

# Ensure the local directory exists
mkdir -p "$LOCAL_DIR"

sshpass -p "$SFTP_PASSWORD" sftp -P $SFTP_PORT "$SFTP_USER@$SFTP_HOST" <<EOF
lcd $LOCAL_DIR
cd $SFTP_REMOTE_DIR
mget *
bye
EOF

# Check the exit status
if [ $? -eq 0 ]; then
  echo "Files downloaded successfully to $LOCAL_DIR"
else
  echo "Error occurred while downloading files."
  exit 1
fi
