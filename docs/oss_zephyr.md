## Purpose
The goal is to build a Zephyr image from the community to test it on STM32MP boards.

## Important note

The support of the stm32mp1 platform has been introduced since the Mickledore distribution of
the meta-zephyr layer.

## Layer Dependency

```
[OECORE]
  URI: https://github.com/openembedded/openembedded-core.git
  layers: meta
  branch: same dedicated branch as meta-zephyr
  revision: HEAD
```

```
[BITBAKE]
  URI: https://github.com/openembedded/bitbake.git
  branch: branch associated to oecore branch
  revision: HEAD
```

```
[META-OPENEMBEDDED]
  URI: git://github.com/openembedded/meta-openembedded.git
  layers: meta-python meta-oe
  branch: same dedicated branch as meta-zephyr
  revision: HEAD
```

## Building the Zephyr distribution

### Get all repositories manually:

From your project root path:

```
git clone https://github.com/openembedded/openembedded-core.git layers/openembedded-core
cd layers/openembedded-core
git checkout -b WORKING <origin/<branch associated to the yocto release>
cd -
git clone https://github.com/openembedded/bitbake.git layers/openembedded-core/bitbake
cd layers/openembedded-core/bitbake
git checkout -b WORKING origin/<branch: branch associated to openembbedded-core branch>
cd -
git clone git://github.com/openembedded/meta-openembedded.git layers/meta-openembedded
cd layers/meta-openembedded
git checkout -b WORKING origin/<branch: branch associated to openembbedded-core branch>
cd -
git clone https://git.yoctoproject.org/meta-zephyr.git layers/meta-zephyr
cd layers/meta-zephyr
git checkout -b WORKING < sha1 or origin/branch >
cd -
```
As example for Mickledorer distribution:
```
git clone https://github.com/openembedded/openembedded-core.git layers/openembedded-core
cd layers/openembedded-core
git checkout -b WORKING origin/mickledore
cd -
git clone https://github.com/openembedded/bitbake.git layers/openembedded-core/bitbake
cd layers/openembedded-core/bitbake
git checkout -b WORKING origin/2.4
cd -
git clone git://github.com/openembedded/meta-openembedded.git layers/meta-openembedded
cd layers/meta-openembedded
git checkout -b WORKING origin/mickledore
cd -
git clone https://git.yoctoproject.org/meta-zephyr.git layers/meta-zephyr
cd layers/meta-zephyr
git checkout -b WORKING origin/mickledore
cd -
```
### Initializing the OpenEmbedded build environment

The OpenEmbedded environment setup script must be run once in each new working terminal in which you use the BitBake or devtool tools (see later) from the installation directory:

```
MACHINE="stm32mp157c-dk2" DISTRO="zephyr" source layers/openembedded-core/oe-init-build-env build-zephyr
bitbake-layers add-layer ../layers/meta-openembedded/meta-oe/
bitbake-layers add-layer ../layers/meta-openembedded/meta-python/
bitbake-layers add-layer ../layers/meta-zephyr/
```

### Building the Zephyr image

For instance to build the zephyr-openamp-rsc-table example which answers to [the Linux rpmsg sample client example](https://elixir.bootlin.com/linux/latest/source/samples/rpmsg/rpmsg_client_sample.c):
```
MACHINE="stm32mp157c-dk2" DISTRO="zephyr" bitbake zephyr-openamp-rsc-table
```

Note that:

- to build around 30 GB is needed
- building the distribution can take 1 or 2 hours depending on performance of the PC.

### Install the Zephyr binary on the sdcard

The Zephyr sample binary is available in the sub-folder of build directory ./tmp-newlib/deploy/images/stm32mp157c-dk2/
It needs to be installed on the "rootfs" partition of the sdcard

   ```
   $ sudo cp tmp-newlib/deploy/images/stm32mp157c-dk2/zephyr-openamp-rsc-table.elf <mount point>/rootfs/lib/firmware/
   ```
Properly unoumt the sdcard partitions.

## Zephyr Demos

Connect the board to the PC to enable the board console and Boot the board.
For details see description of [booting board](https://wiki.st.com/stm32mpu/wiki/STM32MP15_Discovery_kits_-_Starter_Package#Booting_the_board)

#### Linux rpmsg sample client driver demo

The [zephyr-openamp-rsc-table](https://github.com/zephyrproject-rtos/zephyr/tree/main/samples/subsys/ipc/openamp_rsc_table) demonstrates the RPMsg interprocessor communication with the Linux by initiating communication with the Linux rpmsg_client_sample sample. To run the sample embedded in the rootfs partition of the sdcard in /lib/firmware/ directory:

   ```
   root@stm32mp15-disco-oss:~# echo zephyr-openamp-rsc-table.elf >/sys/class/remoteproc/remoteproc0/firmware
   root@stm32mp15-disco-oss:~# echo start >/sys/class/remoteproc/remoteproc0/state
   ```
On success the following message will appear on the corresponding Zephyr console:

   ```
   rpmsg_client_sample virtio0.rpmsg-client-sample.-1.1024: new channel: 0x400 -> 0x400!
   rpmsg_client_sample virtio0.rpmsg-client-sample.-1.1024: incoming msg 1 (src: 0x400)
   rpmsg_client_sample virtio0.rpmsg-client-sample.-1.1024: incoming msg 2 (src: 0x400)
   rpmsg_client_sample virtio0.rpmsg-client-sample.-1.1024: incoming msg 3 (src: 0x400)
   ...
   rpmsg_client_sample virtio0.rpmsg-client-sample.-1.1024: incoming msg 100 (src: 0x400)
   rpmsg_client_sample virtio0.rpmsg-client-sample.-1.1024: goodbye!
   virtio_rpmsg_bus virtio0: destroying channel rpmsg-client-sample addr 0x400
   rpmsg_client_sample virtio0.rpmsg-client-sample.-1.1024: rpmsg sample client driver is removed
   ```
