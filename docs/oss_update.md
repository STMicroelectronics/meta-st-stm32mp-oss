## Purpose
The goal is to update easily the main components from the community to test it on STM32MP boards.

## List of components that can be updated

  * Linux kernel
  * Arm Trusted Firmware (TF-A)
  * U-Boot
  * Optee

## Update Linux kernel

  URI of Linux Kernel: **https://www.kernel.org**

  On recipe **recipes-kernel/linux/linux-stm32mp-oss_6.3.bb**:
```
# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "6.3.4"
LINUX_VERSION = "6.3"

SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v6.x/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "5cc25097ab31f02e507be17acfd032aacace93262596f6d011fe9fb119fb0efd"

# ------------------------------------------------
require linux-stm32mp-oss.inc
```
  You Need to update the version of kernel desired on variable **ST_KERNEL_VERSION** and
  add the SHA256 of the tarball associated to version to add.

  **TIPS**: to get the SHA256 of the tarball, remove the line with **SRC_URI[kernel.sha256sum]** on the recipe and ask to bitbake to indicate the correct new SHA256:
```
bitbake linux-stm32mp-oss -c fetch
```

## Update Arm Trusted Firmware

  URI of Arm Trusted Firmware: **https://github.com/ARM-software/arm-trusted-firmware.git**

  On recipe **recipes-bsp/trusted-firmware-a/tf-a-stm32mp-oss_2.9.bb**:
```
require tf-a-stm32mp-oss.inc

# ------------------------------------------------
# to be updated
TFA_VERSION = "2.9"

SRCREV = "d3e71ead6ea5bc3555ac90a446efec84ef6c6122"
#2.9 SRCREV = "d3e71ead6ea5bc3555ac90a446efec84ef6c6122"
#2.8 SRCREV = "9881bb93a3bc0a3ea37e9f093e09ab4b360a9e48"
#2.7 SRCREV = "35f4c7295bafeb32c8bcbdfb6a3f2e74a57e732b"
#2.6 SRCREV = "a1f02f4f3daae7e21ee58b4c93ec3e46b8f28d15"
#2.5 SRCREV = "1e13c500a0351ac4b55d09a63f7008e2438550f8"
#2.4 SRCREV = "e2c509a39c6cc4dda8734e6509cdbe6e3603cdfc"
# ------------------------------------------------
```
  You Need to update the version of TFA (**TFA_VERSION**) and **SRCREV** which match to SHA1 of git commit associated to the version.

## Update U-Boot

  URI of U-Boot: **git://git.denx.de/u-boot.git (https://source.denx.de/u-boot/u-boot)**

  On recipe **recipes-bsp/u-boot/u-boot-stm32mp-oss_2023.04.bb**:
```
require u-boot-stm32mp-oss.inc

# ------------------------------------------------
# to be updated
UBOOT_VERSION = "2023.04"
SRCREV = "fd4ed6b7e83ec3aea9a2ce21baea8ca9676f40dd"
#SRCREV = "fd4ed6b7e83ec3aea9a2ce21baea8ca9676f40dd" # 2023.04
#SRCREV = "62e2ad1ceafbfdf2c44d3dc1b6efc81e768a96b9" # 2023.01
#SRCREV = "4debc57a3da6c3f4d3f89a637e99206f4cea0a96" # 2022.10
#SRCREV = "e092e3250270a1016c877da7bdd9384f14b1321e" # 2022.07
#SRCREV = "e4b6ebd3de982ae7185dbf689a030e73fd06e0d2" # 2022.04
#SRCREV = "d637294e264adfeb29f390dfc393106fd4d41b17" # 2022.01
#SRCREV = "d80bb749fab53da72c4a0e09b8c2d2aaa3103c91" # 2021.10
#SRCREV = "840658b093976390e9537724f802281c9c8439f5" # 2021.07
#SRCREV = "b46dd116ce03e235f2a7d4843c6278e1da44b5e1" # 2021.04
#SRCREV = "c4fddedc48f336eabc4ce3f74940e6aa372de18c" # 2021.01
#SRCREV = "050acee119b3757fee3bd128f55d720fdd9bb890" # 2020.10
#SRCREV = "2f5fbb5b39f7b67044dda5c35e4a4b31685a3109" # 2020.07
# ------------------------------------------------
```
  You Need to update the version of U-BOOT (**UBOOT_VERSION**) and **SRCREV** which match to SHA1 of git commit associated to the version.

## Update Optee

### Optee-os

  URI of Optee-os: **https://github.com/OP-TEE/optee_os.git**

  On recipe **recipes-security/optee/optee-os-stm32mp-oss_3.21.0.bb**:
```
require optee-os-stm32mp-oss.inc
# Need to be aligned with the version of OPTEE-OS
# ------------------------------------------------
# to be updated
OPTEE_VERSION = "3.21.0"

SRCREV = "e8abbcfbdf63437a640d5fd87b7e191caab6445e"
# SRCREV = "e8abbcfbdf63437a640d5fd87b7e191caab6445e" # 3.21.0
# SRCREV = "8e74d47616a20eaa23ca692f4bbbf917a236ed94" # 3.20.0
# SRCREV = "afacf356f9593a7f83cae9f96026824ec242ff52" # 3.19.0
# SRCREV = "1ee647035939e073a2e8dddb727c0f019cc035f1" # 3.18.0
# SRCREV = "f9e550142dd4b33ee1112f5dd64ffa94ba79cefa" # 3.17.0
# SRCREV = "d0b742d1564834dac903f906168d7357063d5459" # 3.16.0
# SRCREV = "6be0dbcaa11394a2ad5a46ac77e2f76e31a41722" # 3.15.0
# ------------------------------------------------
```
  You Need to update the version of OPTEE (**OPTEE_VERSION**) and **SRCREV** which match to SHA1 of git commit associated to the version.

  Optee-os provide interfaces and script (SDK) to help to generate TA, on this context you need to update also the component: optee-client and optee-test.

### Optee-client

  URI of Optee-client: **https://github.com/OP-TEE/optee_client.git**

  On recipe **recipes-security/optee/optee-client-oss_3.21.0.bb**:
```
require optee-client-oss.inc
# Need to be aligned with the version of OPTEE-OS
# ------------------------------------------------
# to be updated
OPTEE_VERSION = "3.21.0"

SRCREV = "8533e0e6329840ee96cf81b6453f257204227e6c"
# SRCREV = "8533e0e6329840ee96cf81b6453f257204227e6c" # 3.21.0
# SRCREV = "dd2d39b49975d2ada7870fe2b7f5a84d0d3860dc" # 3.20.0
# SRCREV = "140bf463046071d3ca5ebbde3fb21ee0854e1951" # 3.19.0
# SRCREV = "e7cba71cc6e2ecd02f412c7e9ee104f0a5dffc6f" # 3.18.0
# SRCREV = "9a337049c52495e5e16b4a94decaa3e58fce793e" # 3.17.0
# SRCREV = "06db73b3f3fdb8d23eceaedbc46c49c0b45fd1e2" # 3.16.0
# SRCREV = "182874320395787a389e5b0f7df02b32f3c0a1b0" # 3.15.0
# ------------------------------------------------
```
  You Need to update the version of OPTEE (**OPTEE_VERSION**) and **SRCREV** which match to SHA1 of git commit associated to the version.

### Optee-test

  URI of Optee-test: **https://github.com/OP-TEE/optee_test.git**

  On recipe **recipes-security/optee/optee-test-oss_3.21.0.bb**:
```
require optee-test-oss.inc
# Need to be aligned with the version of OPTEE-OS
# ------------------------------------------------
# to be updated
OPTEE_VERSION = "3.21.0"

SRCREV = "9c872638bc38324d8c65b9296ebec3d124e19466"
#SRCREV = "9c872638bc38324d8c65b9296ebec3d124e19466" # 3.21.0
#SRCREV = "5db8ab4c733d5b2f4afac3e9aef0a26634c4b444" # 3.20.0
#SRCREV = "ab9863cc187724e54c032b738c28bd6e9460a4db" # 3.19.0
#SRCREV = "da5282a011b40621a2cf7a296c11a35c833ed91b" # 3.18.0
#SRCREV = "44a31d02379bd8e50762caa5e1592ad81e3339af" # 3.17.0
#SRCREV = "1cf0e6d2bdd1145370033d4e182634458528579d" # 3.16.0
#SRCREV = "f88f69eb27beda52998de09cd89a7ee422da00d9" # 3.15.0
# ------------------------------------------------
```
  You Need to update the version of OPTEE (**OPTEE_VERSION**) and **SRCREV** associated which match to SHA1 of git commit associated to the version.
