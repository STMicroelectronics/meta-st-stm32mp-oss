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

INITRD_IMAGE_PRESENT ??= "0"
EFI_AVAILABLE ??= "${@bb.utils.contains('MACHINE_FEATURES', 'efi', '1', '0', d)}"

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
    #if [ "${RT_KERNEL}" = "1" ];
    #then
    #    echo "DEFAULT rt" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    #else
        echo "DEFAULT board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    #fi
    echo "LABEL board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    echo "	KERNEL /zImage" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
        echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    fi
    echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf

    if [ "${RT_KERNEL}" = "1" ];
    then
        echo "LABEL rt" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
        echo "	KERNEL /zImage-${RT_KERNEL_VERSION}" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
        echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
        if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
            echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
        fi
        echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    fi
    if [ "${EFI_AVAILABLE}" = "1" ]; then
        echo "LABEL efi" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
        echo "	localboot 1" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2_extlinux.conf
    fi

    # stm32mp157c-ev1_extlinux.conf
    install -d ${D}/boot/mmc0_extlinux/
    echo "menu title Select the boot mode" > ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "MENU BACKGROUND /splash.bmp" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "TIMEOUT 20" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "DEFAULT board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "LABEL board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "	KERNEL /zImage" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
        echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    fi
    echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf

    if [ "${RT_KERNEL}" = "1" ];
    then
        echo "LABEL rt" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
        echo "	KERNEL /zImage-${RT_KERNEL_VERSION}" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
        echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
        if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
            echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
        fi
        echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    fi
    if [ "${EFI_AVAILABLE}" = "1" ]; then
        echo "LABEL efi" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
        echo "	localboot 1" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1_extlinux.conf
    fi

    # stm32mp135f-dk_extlinux.conf
    install -d ${D}/boot/mmc0_extlinux/
    echo "menu title Select the boot mode" > ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
    echo "MENU BACKGROUND /splash.bmp" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
    echo "TIMEOUT 20" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
    #if [ "${RT_KERNEL}" = "1" ];
    #then
    #    echo "DEFAULT rt" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
    #else
        echo "DEFAULT board" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
    #fi
    echo "LABEL board" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
    echo "	KERNEL /zImage" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
    echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
    if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
        echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
    fi
    echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf

    if [ "${RT_KERNEL}" = "1" ];
    then
        echo "LABEL rt" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
        echo "	KERNEL /zImage-${RT_KERNEL_VERSION}" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
        echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
        if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
            echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
        fi
        echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
    fi
    if [ "${EFI_AVAILABLE}" = "1" ]; then
        echo "LABEL efi" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
        echo "	localboot 1" >> ${D}/boot/mmc0_extlinux/stm32mp135f-dk_extlinux.conf
    fi


    # stm32mp157c-dk2-scmi_extlinux.conf
    install -d ${D}/boot/mmc0_extlinux/
    echo "menu title Select the boot mode" > ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
    echo "MENU BACKGROUND /splash.bmp" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
    echo "TIMEOUT 20" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
    echo "DEFAULT board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
    echo "LABEL board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
    echo "	KERNEL /zImage" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
    echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
    if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
        echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
    fi
    echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf

    if [ "${RT_KERNEL}" = "1" ];
    then
        echo "LABEL rt" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
        echo "	KERNEL /zImage-${RT_KERNEL_VERSION}" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
        echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
        if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
            echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
        fi
        echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
    fi
    if [ "${EFI_AVAILABLE}" = "1" ]; then
        echo "LABEL efi" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
        echo "	localboot 1" >> ${D}/boot/mmc0_extlinux/stm32mp157c-dk2-scmi_extlinux.conf
    fi

    # stm32mp157c-ev1-scmi_extlinux.conf
    install -d ${D}/boot/mmc0_extlinux/
    echo "menu title Select the boot mode" > ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
    echo "MENU BACKGROUND /splash.bmp" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
    echo "TIMEOUT 20" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
    echo "DEFAULT board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
    echo "LABEL board" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
    echo "	KERNEL /zImage" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
    echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
    if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
        echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
    fi
    echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf

    if [ "${RT_KERNEL}" = "1" ];
    then
        echo "LABEL rt" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
        echo "	KERNEL /zImage-${RT_KERNEL_VERSION}" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
        echo "	FDTDIR /" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
        if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
        echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
        fi
        echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
    fi
    if [ "${EFI_AVAILABLE}" = "1" ]; then
        echo "LABEL efi" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
        echo "	localboot 1" >> ${D}/boot/mmc0_extlinux/stm32mp157c-ev1-scmi_extlinux.conf
    fi


    install -d ${D}/boot/mmc1_extlinux/
    # stm32mp157c-ev1_extlinux.conf
    echo "menu title Select the boot mode" > ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "MENU BACKGROUND /splash.bmp" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "TIMEOUT 20" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "DEFAULT board" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "LABEL board" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "	KERNEL /zImage" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "	FDTDIR /" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
    if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
        echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
    fi
    echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf

    if [ "${RT_KERNEL}" = "1" ];
    then
        echo "LABEL rt" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
        echo "	KERNEL /zImage-${RT_KERNEL_VERSION}" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
        echo "	FDTDIR /" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
        if [ "${INITRD_IMAGE_PRESENT}" = "1" ]; then
            echo "	INITRD /st-image-resize-initrd" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
        fi
        echo "	APPEND root=PARTLABEL=rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
    fi
#    if [ "${EFI_AVAILABLE}" = "1" ]; then
        #echo "LABEL efi" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
        #echo "	localboot 1" >> ${D}/boot/mmc1_extlinux/stm32mp157c-ev1_extlinux.conf
#    fi

    install -d ${D}/boot/nand0_extlinux/
    # stm32mp157c-ev1_extlinux.conf
    echo "menu title Select the boot mode" > ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "MENU BACKGROUND /splash.bmp" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "TIMEOUT 20" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "DEFAULT board" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "LABEL board" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "	KERNEL /zImage" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "	FDTDIR /" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
    echo "	APPEND ubi.mtd=UBI rootfstype=ubifs root=ubi0:rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf

    if [ "${RT_KERNEL}" = "1" ];
    then
        echo "LABEL rt" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
        echo "	KERNEL /zImage-${RT_KERNEL_VERSION}" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
        echo "	FDTDIR /" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
        echo "	APPEND ubi.mtd=UBI rootfstype=ubifs root=ubi0:rootfs rootwait rw console=ttySTM0,115200" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
    fi
#    if [ "${EFI_AVAILABLE}" = "1" ]; then
        #echo "LABEL efi" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
        #echo "	localboot 1" >> ${D}/boot/nand0_extlinux/stm32mp157c-ev1_extlinux.conf
#    fi


}
FILES:${PN} = "/boot"
