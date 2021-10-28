## Purpose
This section describe the different boot available with OSS build.

## Boot Stage
With STM32MP platform, we use different boot stage:
```
   ---------------------
   |   BL2: TF-A       |
   ---------------------
   | FIP firmware      |
   | ----------------- |
   | |  BL32: OPTEE  | |
   | ----------------- |
   | |  BL33: U-BOOT | |
   | ----------------- |
   ---------------------
```   

## List of boot available
The customization of boot with OSS software is available only via U-Boot.  
List of boot available via U-Boot:
  * Basic Linux kernel Boot
  * EFI boot
      * (default) EFI Linux kernel Boot
      * EFI Signed Linux Kernel Boot
      * EFI Grub Boot

Declaration made via boot script of the different boot:
```
efidebug boot add -b 0000 'kernel' mmc 0:4 zImage -s 'console=ttySTM0,115200 rootwait root=PARTLABEL=rootfs bootmgr0'
efidebug boot add -b 0001 'signkernel' mmc 0:4 zImage.signed -s 'console=ttySTM0,115200 rootwait root=PARTLABEL=rootfs bootmgr3-signed'
efidebug boot add -b 0002 'grub' mmc 0:4 grub-arm.efi

efidebug boot order 0000
```
### Basic Linux Kernel Boot
On U-boot extlinux menu, just select the name of board (disco or eval):
```
Select the boot mode
1:      disco
2:      efi
Enter choice: 1
```
This action permit to boot the linux kernel with the device tree specified by U-BOOT: **fdtfile**, and the command line specified on the extlinux.conf file.

**NOTE**:
the boot.scr.uimg script select the extlinux conf file on function of the board on which you are booting:
* Disco board: mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
* Eval board: mmc0_extlinux/stm32mp157c-ev1_extlinux.conf

### EFI Linux Kernel boot
On U-boot extlinux menu, just select **efi**:
```
Select the boot mode
1:      disco
2:      efi
Enter choice: 2
```
This action permit to call the localcmd command of U-BOOT.  
The default localcmd command make:
* Change the splash screen color
* load of devicetree (**fdtfile** on u-boot env)
* ask via **bootefi** command to boot an efi firmware : **bootefi bootmgr ${fdt_addr_r}**.


The EFI firmware to boot are specified by the boot order on U-BOOT shell:
```
efidebug boot order 0000
```
To force to use (or re-use ***Linux Kernel*** as EFI Firmware), you need to change the efi boot order to 0000 on U-BOOT shell:
```
STM32MP> efidebug boot order 0000
STM32MP> run localcmd
```

**NOTE**:  
If you need to restore the default LOCAL command, there is two ways:
* restore to production configuration
```
STM32MP> eraseenv
STM32MP> reset
```
* to restore only the LOCAL command:
```
STM32MP> env set localcmd 'run localcmd_efi_bootmgr;'
STM32MP> saveenv
```

### EFI Signed Linux Kernel Boot
On U-Boot shell, you must select to use the signed kernel for EFI boot and precise the new LOCAL command to be sure to load the key to verify signed kernel.

On U-boot shell:
```
STM32MP> efidebug boot next 0001
STM32MP> env set localcmd 'run localcmd_efi_bootmgr_signed'
STM32MP> saveenv
STM32MP> run localcmd_efi_bootmgr_signed
```
or
```
STM32MP> run localcmd_signed
```

This action permit to call the localcmd command of U-BOOT.
The localcmd command make:
* load of devicetree (**fdtfile** on u-boot env)
* Load KEY to verify the signed ST_KERNEL_VERSION
* ask to **bootefi** command to boot an efi firmware via **bootefi bootmgr ${fdt_addr_r}**.

