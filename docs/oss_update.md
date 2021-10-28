## Purpose
The goal is to update easily the main components from the community to test it on STM32MP boards.

## List of components that can be updated

  * Linux kernel
  * Arm Trusted Firmware (TF-A)
  * U-Boot
  * Optee

## Update Linux kernel

  URI of Linux Kernel: **https://www.kernel.org**

  On recipe **recipes-kernel/linux/linux-stm32mp-oss_5.15.bb**:
```
# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.15-rc6"
LINUX_VERSION = "5.15"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "318a37a7e0fcbcb33fa19374b481535e6b2e58463a33d2d79827b5a53996e3d2"

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

  On recipe **recipes-bsp/trusted-firmware-a/tf-a-stm32mp-oss_2.5.bb**:
```
require tf-a-stm32mp-oss.inc

# ------------------------------------------------
# to be updated
TFA_VERSION = "2.5"

SRCREV = "09665c83484b3e730814e368df80129598573bd9"
#2.5 SRCREV = "1e13c500a0351ac4b55d09a63f7008e2438550f8"
#2.4 SRCREV = "e2c509a39c6cc4dda8734e6509cdbe6e3603cdfc"
# ------------------------------------------------
```
  You Need to update the version of TFA (**TFA_VERSION**) and **SRCREV** which match to SHA1 of git commit associated to the version.

## Update U-Boot

  URI of U-Boot: **git://git.denx.de/u-boot.git (https://source.denx.de/u-boot/u-boot)**

  On recipe **recipes-bsp/u-boot/u-boot-stm32mp-oss_2021.10.bb**:
```
require u-boot-stm32mp-oss.inc

# ------------------------------------------------
# to be updated
UBOOT_VERSION = "2021.10"
SRCREV = "6a86f1212656d4497b8980048907535f5294fabe"
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

  On recipe **recipes-security/optee/optee-os-stm32mp-oss_3.15.0.bb**:
```
require optee-os-stm32mp-oss.inc
# Need to be aligned with the version of OPTEE-OS
# ------------------------------------------------
# to be updated
OPTEE_VERSION = "3.15.0"

SRCREV = "6be0dbcaa11394a2ad5a46ac77e2f76e31a41722"
# SRCREV = "6be0dbcaa11394a2ad5a46ac77e2f76e31a41722" # 3.15.0
# ------------------------------------------------
```
  You Need to update the version of OPTEE (**OPTEE_VERSION**) and **SRCREV** which match to SHA1 of git commit associated to the version.

  Optee-os provide interfaces and script (SDK) to help to generate TA, on this context you need to update also the component: optee-client and optee-test.

### Optee-client

  URI of Optee-client: **https://github.com/OP-TEE/optee_client.git**

  On recipe **recipes-security/optee/optee-client-oss_3.15.0.bb**:
```
require optee-client-oss.inc
# Need to be aligned with the version of OPTEE-OS
# ------------------------------------------------
# to be updated
OPTEE_VERSION = "3.15.0"

SRCREV = "182874320395787a389e5b0f7df02b32f3c0a1b0"
# SRCREV = "182874320395787a389e5b0f7df02b32f3c0a1b0" # 3.15.0
# ------------------------------------------------
```
  You Need to update the version of OPTEE (**OPTEE_VERSION**) and **SRCREV** which match to SHA1 of git commit associated to the version.

### Optee-test

  URI of Optee-test: **https://github.com/OP-TEE/optee_test.git**

  On recipe **recipes-security/optee/optee-test-oss_3.15.0.bb**:
```
require optee-test-oss.inc
# Need to be aligned with the version of OPTEE-OS
# ------------------------------------------------
# to be updated
OPTEE_VERSION = "3.15.0"

SRCREV = "f88f69eb27beda52998de09cd89a7ee422da00d9"
#SRCREV = "f88f69eb27beda52998de09cd89a7ee422da00d9" # 3.15.0
# ------------------------------------------------
```
  You Need to update the version of OPTEE (**OPTEE_VERSION**) and **SRCREV** associated which match to SHA1 of git commit associated to the version.
