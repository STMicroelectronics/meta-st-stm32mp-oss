## Summary

**meta-st-stm32mp-oss BSP layer** is a layer containing the STMicroelectronics bsp metadata for upstream versions
of stm32mp.

This layer relies on OpenEmbedded/Yocto build system that is providing through
Bitbake and OpenEmbedded-Core layers or Poky layer all parts of the Yocto Project

The Yocto Project has extensive documentation about OE including a reference manual
which can be found at:

 * **http://yoctoproject.org/documentation**

For information about OpenEmbedded, see the OpenEmbedded website:

 * **http://www.openembedded.org/**

## Important NOTE

 This layer is provided to help the communities to use upstream softwares on STM32MP boards.  
 This layer is not targeted to be used to generate final product SW release.  

 This layer is designed to update and test upstream code on STM32MP boards:
 + Linux kernel (recipes-recipes-kernel/linux/linux-stm32mp-oss_x.xx.bb)
 + Arm Trusted Firmware (TF-A) (recipes-bsp/trusted-firmware-a/tf-a-stm32mp-oss_x.x.bb)
 + U-Boot (recipes-bsp/u-boot/u-boot-stm32mp-oss_xxxx.xx.bb)
 + Optee-os (recipes-security/optee/optee-os-stm32mp-oss_x.xx.x.bb)
      - The component associated to optee-os MUST be aligned to optee-os version:
          - Optee-client (recipes-security/optee/optee-client-oss_x.xx.x.bb)
          - Optee-test (recipes-security/optee/optee-test-oss_x.xx.x.bb)  

[See section  How to update opensource component](#update-component)

For helping the test of EFI boot, some recipes coming from https://github.com/jiazhang0/meta-secure-core
are incorporated on this layer (efitools and sbsigntools).

**No support.** STMicroelectronics is under no obligation to support the layer and or to provide you with updates or error corrections.

Using the **meta-st-stm32mp-oss BSP** layer, you acknowledge that the Software may have defects or deficiencies which cannot or will not be corrected by STMicroelectronics .


## Layer Dependency

This layer depends on:

```
[OECORE]
URI: https://github.com/openembedded/openembedded-core.git
layers: meta
branch: same dedicated branch as meta-st-stm32mp
revision: HEAD
[BITBAKE]
URI: https://github.com/openembedded/bitbake.git
branch: branch associated to oecore branch
revision: HEAD
```
or
```
[OECORE]
URI: git://git.yoctoproject.org/poky
layers: meta
branch: same dedicated branch as meta-st-stm32mp-oss
revision: HEAD
```

```
[META-OPENEMBEDDED]
URI: git://github.com/openembedded/meta-openembedded.git
layers: meta-python meta-oe
branch: same dedicated branch as meta-st-stm32mp-oss
revision: HEAD
```

The dependency (meta-python) are due to the usage of OPTEE which require to use some python packages.

## EULA

Some SoC depends on firmware and/or packages that are covered by
 STMicroelectronics EULA. To have the right to use those binaries in your images you need to read and accept the EULA available as:

conf/eula/$MACHINE, e.g. conf/eula/stm32mp1

In order to accept it, you should add, in your local.conf file:

ACCEPT_EULA_$MACHINE = "1", e.g.: ACCEPT_EULA_stm32mp1 = "1"

If you do not accept the EULA the generated image will be missing some
components and features.


## OSS: How to

### Get all repositories with ST manifest:
```
 repo init -u https://github.com/STMicroelectronics/oe-manifest.git
 repo sync
 cd layers/meta-st/
 git clone https://github.com/STMicroelectronics/meta-st-stm32mp-oss
 cd meta-st-stm32mp-oss
 git checkout -b OSS origin/<branch associated to openembedded-core>
 cd -
```
 For stm32mp15-disco-oss
```
 MACHINE=stm32mp15-disco-oss DISTRO=openstlinux-weston source layers/meta-st/scripts/envsetup.sh

 bitbake st-image-weston

 cd tmp-glibc/deploy/images/stm32mp15-disco-oss/
 # flash wic image on your board:
 dd if=st-image-weston-openstlinux-weston-stm32mp15-disco-oss.wic of=/dev/mmcblk0 bs=8M conv=fdatasync
```
 For stm32mp15-eval-oss
```
 MACHINE=stm32mp15-eval-oss DISTRO=openstlinux-weston source layers/meta-st/scripts/envsetup.sh

 bitbake st-image-weston

 cd tmp-glibc/deploy/images/stm32mp15-eval-oss/
 # flash wic image on your board:
 dd if=st-image-weston-openstlinux-weston-stm32mp15-eval-oss.wic of=/dev/mmcblk0 bs=8M conv=fdatasync
```
### Get all repositories manually:
```
mkdir layers; cd layers
git clone https://github.com/openembedded/openembedded-core.git
cd openembedded-core
git checkout -b WORKING <origin/<branch associated to meta-st-stm32mp-oss>

git clone https://github.com/openembedded/bitbake.git
cd bitbake
git checkout -b WORKING origin/<branch: branch associated to openembbedded-core branch>
cd ..
cd ..
git clone git://github.com/openembedded/meta-openembedded.git
cd meta-openembedded
git checkout -b WORKING origin/<branch: branch associated to openembbedded-core branch>
cd ..
mkdir meta-st/; cd meta-st
git clone https://github.com/STMicroelectronics/meta-st-stm32mp-oss
cd meta-st-stm32mp-oss
git checkout -b OSS origin/<branch associated to openembedded-core>
cd ../..
```
 For stm32mp15-disco-oss
```

 source ./layers/openembedded-core/oe-init-build-env build-stm32mp15-disco-oss

 bitbake-layers add-layer ../layers/meta-openembedded/meta-oe
 bitbake-layers add-layer ../layers/meta-openembedded/meta-perl
 bitbake-layers add-layer ../layers/meta-openembedded/meta-python
 bitbake-layers add-layer ../layers/meta-st/meta-st-stm32mp-oss

 echo "MACHINE = \"stm32mp15-disco-oss\"" >> conf/local.conf
 echo "DISTRO = \"nodistro\"" >> conf/local.conf
 echo "PACKAGE_CLASSES = \"package_deb\" " >> conf/local.conf

 bitbake core-image-base

 cd tmp-glibc/deploy/images/stm32mp15-disco-oss/
 # flash wic image on your board:
 dd if=core-image-base-stm32mp15-disco-oss.wic of=/dev/mmcblk0 bs=8M conv=fdatasync
```
 For stm32mp15-eval-oss
```
 MACHINE=stm32mp15-eval-oss DISTRO=openstlinux-weston source layers/meta-st/scripts/envsetup.sh
 bitbake-layers add-layer ../layers/meta-openembedded/meta-oe
 bitbake-layers add-layer ../layers/meta-openembedded/meta-perl
 bitbake-layers add-layer ../layers/meta-openembedded/meta-python
 bitbake-layers add-layer ../layers/meta-st/meta-st-stm32mp-oss

 echo "MACHINE = \"stm32mp15-eval-oss\"" >> conf/local.conf
 echo "DISTRO = \"nodistro\"" >> conf/local.conf
 echo "PACKAGE_CLASSES = \"package_deb\" " >> conf/local.conf

 bitbake core-image-base

 cd tmp-glibc/deploy/images/stm32mp15-eval-oss/
 # flash wic image on your board:
 dd if=core-image-base-stm32mp15-eval-oss.wic of=/dev/mmcblk0 bs=8M conv=fdatasync
```






## OSS: How to update opensource component <a name="update-component"></a>

[See description of component update](docs/oss_update.md)

## OSS: Boot strategy

[See description of boot strategy](docs/oss_boot_strategy.md)

## Supported board
 Board supported:
 + STM32MP157C-DK2 via stm32mp15-disco-oss machine
 + STM32MP157F-DK2 via stm32mp15-disco-oss machine
 + STM32mp157C-EV1 via stm32mp15-eval-oss machine
 + STM32mp157F-EV1 via stm32mp15-eval-oss machine

**NOTE**:
 The software generated for a **C** package boards are compilatible for board which contains **F** package:  
 + Software for STM32MP157C-DK2 can be used on STM32MP157F-DK2
 + Software for STM32MP157C-EV1 can be used on STM32MP157F-EV1

## Limitation

The features and boards supported depend on upstream status for each component used.  

## Contributing

If you want to contribute changes, you can send Github pull requests at
**https://github.com/stmicroelectronics/meta-st-stm32mp-oss/pulls**.


## Maintainers

 - Christophe Priouzeau <christophe.priouzeau@foss.st.com>
