inherit image_types

STBOOTFS_SIZE ??= "65536"

# thie variable could'nt be changed due to link creation
STBOOTFS_NAME = "${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.vfat"

STBOOTFS_FILE_REGEXP ?= "\
    boot.scr* \
    mmc0_extlinux/* \
    uefi-certificates/* \
    zImage \
    zImage.signed \
    ubootefi.var \
    uImage \
    splash* \
    stm32mp*.dtb \
    "

do_image_stbootfs[depends] += " \
        mtools-native:do_populate_sysroot \
        dosfstools-native:do_populate_sysroot \
        "

python __anonymous () {
    import re
    target = d.getVar('TARGET_ARCH')
    if target == "x86_64":
        kernel_efi_image = "bootx64.efi"
    elif re.match('i.86', target):
        kernel_efi_image = "bootia32.efi"
    elif re.match('aarch64', target):
        kernel_efi_image = "bootaa64.efi"
    elif re.match('arm', target):
        kernel_efi_image = "bootarm.efi"
    else:
        raise bb.parse.SkipRecipe("kernel efi is incompatible with target %s" % target)
    d.setVar("KERNEL_EFI_IMAGE", kernel_efi_image)
}

IMAGE_CMD:stbootfs () {
    rm -f ${STBOOTFS_NAME}

    # create bootfs filesystem
    mkfs.vfat -n BOOTFS -S 512 -C ${STBOOTFS_NAME} ${STBOOTFS_SIZE}

    cd ${IMGDEPLOYDIR};
    mmd -i ${STBOOTFS_NAME} ::/mmc0_extlinux
    mmd -i ${STBOOTFS_NAME} ::/uefi-certificates

    # special case of extlinux.conf
    if [ -s ${IMAGE_ROOTFS}/boot/extlinux.conf ]; then
        mmd -i ${STBOOTFS_NAME} ::/extlinux
        mcopy -i ${STBOOTFS_NAME} -s ${IMAGE_ROOTFS}/extlinux/* ::/extlinux/
    fi

    #populate boot image
    for reg in ${STBOOTFS_FILE_REGEXP}; do
        if (echo $reg | grep -q "/") ; then
            dir=$(dirname $reg)
            mcopy -i ${STBOOTFS_NAME} -s ${IMAGE_ROOTFS}/boot/$reg ::/$dir
        else
            mcopy -i ${STBOOTFS_NAME} -s ${IMAGE_ROOTFS}/boot/$reg ::/
        fi
    done


    (cd ${IMGDEPLOYDIR};ln -sf ${IMAGE_NAME}.bootfs.vfat ${IMAGE_LINK_NAME}.bootfs.vfat)
    (cd ${IMGDEPLOYDIR};xz -z -c ${IMAGE_NAME}.bootfs.vfat > ${IMAGE_NAME}.bootfs.vfat.xz )
    (cd ${IMGDEPLOYDIR};cp ${IMAGE_NAME}.bootfs.vfat.xz ${IMAGE_LINK_NAME}.bootfs.vfat.xz)
}

