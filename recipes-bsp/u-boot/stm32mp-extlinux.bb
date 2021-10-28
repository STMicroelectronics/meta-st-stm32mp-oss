SUMMARY = "Provide 'extlinux.conf' file for U-Boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += "u-boot-mkimage-native"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://boot.scr.cmd.template"

PV = "4.1"

inherit kernel-arch

UBOOT_EXTLINUX_BOOTSCR = "${WORKDIR}/boot.scr.cmd"
UBOOT_EXTLINUX_BOOTSCR_IMG = "${WORKDIR}/boot.scr.uimg"

UBOOT_EXTLINUX_INSTALL_DIR ?= "/boot"

do_compile() {
    if [ -e ${UBOOT_EXTLINUX_BOOTSCR}.template ]; then
        sed "s/%KERNEL_CMDLINE%/${KERNEL_CMDLINE}/" ${UBOOT_EXTLINUX_BOOTSCR}.template > ${UBOOT_EXTLINUX_BOOTSCR}
    fi
    mkimage -C none -A ${UBOOT_ARCH} -T script -d ${UBOOT_EXTLINUX_BOOTSCR} ${UBOOT_EXTLINUX_BOOTSCR_IMG}
}

do_install() {
    install -d ${D}/boot/extlinux/

    if [ -e ${UBOOT_EXTLINUX_BOOTSCR_IMG} ]; then
        install -m 755 ${UBOOT_EXTLINUX_BOOTSCR_IMG} ${D}/${UBOOT_EXTLINUX_INSTALL_DIR}
    fi
    # create ubootefi.var
    touch ${D}/boot/ubootefi.var

    # stm32mp157c-dk2_extlinux.conf
    install -d ${D}/boot/mmc0_extlinux/
    echo "menu title Select the boot mode" > ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    echo "MENU BACKGROUND /splash.bmp" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    echo "TIMEOUT 20" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    if [ "${RT_KERNEL}" = "1" ];
    then
        echo "DEFAULT rt" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    else
        echo "DEFAULT board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    fi
    echo "LABEL board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    echo "	KERNEL /uImage" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    echo "	APPEND root=/dev/mmcblk0p5 rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf

    if [ "${RT_KERNEL}" = "1" ];
    then
        echo "LABEL rt" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
        echo "	KERNEL /uImage-rt" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
        echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
        echo "	APPEND root=/dev/mmcblk0p5 rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    fi
    echo "LABEL efi" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    echo "	localboot 1" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf

    # stm32mp157c-ev1_extlinux.conf
    install -d ${D}/boot/mmc0_extlinux/
    echo "menu title Select the boot mode" > ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "MENU BACKGROUND /splash.bmp" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "TIMEOUT 20" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    if [ "${RT_KERNEL}" = "1" ];
    then
        echo "DEFAULT rt" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    else
        echo "DEFAULT board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    fi
    echo "DEFAULT board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "LABEL board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "	KERNEL /uImage" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "	APPEND root=/dev/mmcblk0p5 rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf

    if [ "${RT_KERNEL}" = "1" ];
    then
        echo "LABEL rt" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
        echo "	KERNEL /uImage-rt" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
        echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
        echo "	APPEND root=/dev/mmcblk0p5 rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    fi
    echo "LABEL efi" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "	localboot 1" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
}
FILES:${PN} = "/boot"
