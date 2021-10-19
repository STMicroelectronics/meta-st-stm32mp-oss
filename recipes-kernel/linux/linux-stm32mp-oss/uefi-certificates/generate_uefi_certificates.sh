#!/bin/bash -
#==============================================================================
#  ORGANIZATION: STMicroelectronics
#     COPYRIGHT: Copyright (C) 2021, STMicroelectronics - All Rights Reserved
#       CREATED: 06/05/21 10:06
#===============================================================================

# Package dependency:
# openssl tools
# efitools
# sudo apt-get install openssl efitools

set -o nounset  # Treat unset variables as an error

COMMON_NAME=ST_OSS_STM32MP

#Create PK
openssl req -x509 -sha256 -newkey rsa:2048 -subj /CN=$COMMON_NAME/ -keyout PK.key -out PK.crt -nodes -days 365
cert-to-efi-sig-list -g 11111111-2222-3333-4444-123456789abc PK.crt PK.esl
sign-efi-sig-list -c PK.crt -k PK.key PK PK.esl PK.auth

#Create KEK
openssl req -x509 -sha256 -newkey rsa:2048 -subj /CN=$COMMON_NAME/ -keyout KEK.key -out KEK.crt -nodes -days 365
cert-to-efi-sig-list -g 11111111-2222-3333-4444-123456789abc KEK.crt KEK.esl
sign-efi-sig-list -c PK.crt -k PK.key KEK KEK.esl KEK.auth

#Create DB
openssl req -x509 -sha256 -newkey rsa:2048 -subj /CN=$COMMON_NAME/ -keyout db.key -out db.crt -nodes -days 365
cert-to-efi-sig-list -g 11111111-2222-3333-4444-123456789abc db.crt db.esl
sign-efi-sig-list -c KEK.crt -k KEK.key db db.esl db.auth

#Create DBX
openssl req -x509 -sha256 -newkey rsa:2048 -subj /CN=$COMMON_NAME/ -keyout dbx.key -out dbx.crt -nodes -days 365
cert-to-efi-sig-list -g 11111111-2222-3333-4444-123456789abc dbx.crt dbx.esl
sign-efi-sig-list -c KEK.crt -k KEK.key dbx dbx.esl dbx.auth

# information:
# To sign an image:
# sbsign --key db.key --cert db.crt <image>

# To sign a digest image:
# hash-to-efi-sig-list <image> db_Image.hash
# sign-efi-sig-list -c KEK.crt -k KEK.key db db_Image.hash db_Image.auth
