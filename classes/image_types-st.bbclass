inherit image_types

STBOOTFS_SIZE ??= "63488"

# thie variable could'nt be changed due to link creation
STBOOTFS_VFAT_NAME = "${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.vfat"
STBOOTFS_EXT4_NAME = "${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.ext4"

STBOOTFS_FILE_REGEXP ?= "\
    boot.scr* \
    mmc0_extlinux/* \
    uefi-certificates/* \
    zImage \
    zImage-* \
    zImage.signed \
    ubootefi.var \
    uImage \
    uImage-* \
    splash* \
    stm32mp*.dtb \
    "

do_image_stbootfs[depends] += " \
        mtools-native:do_populate_sysroot \
        dosfstools-native:do_populate_sysroot \
        e2fsprogs-native:do_populate_sysroot \
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
    mkdir -p ${IMAGE_ROOTFS}/../bootfs
    # populate bootfs directory
    #   mmc0_extlinux
    cp -ar  ${IMAGE_ROOTFS}/boot/mmc0_extlinux ${IMAGE_ROOTFS}/../bootfs/
    #   uefi-certificates
    cp -ar  ${IMAGE_ROOTFS}/boot/uefi-certificates ${IMAGE_ROOTFS}/../bootfs/
    # special case of extlinux.conf
    if [ -s ${IMAGE_ROOTFS}/boot/extlinux.conf ]; then
        mkdir -p ${IMAGE_ROOTFS}/../bootfs/extlinux
        cp -ar ${IMAGE_ROOTFS}/boot/extlinux.conf  ${IMAGE_ROOTFS}/../bootfs/extlinux/
    fi
    #populate boot image
    for reg in ${STBOOTFS_FILE_REGEXP}; do
        if (echo $reg | grep -q "/") ; then
            dir=$(dirname $reg)
            cp -r ${IMAGE_ROOTFS}/boot/$reg ${IMAGE_ROOTFS}/../bootfs/$dir
        else
            cp -r ${IMAGE_ROOTFS}/boot/$reg ${IMAGE_ROOTFS}/../bootfs/
        fi
    done
    cd ${IMAGE_ROOTFS}/../bootfs/
    rm zImage uImage
    mv zImage-* zImage
    mv uImage-* uImage

    rm -f ${STBOOTFS_VFAT_NAME}

    # create bootfs filesystem
    mkdosfs -n BOOTFS -i 0xFC32FB2C -S 512 -C ${STBOOTFS_VFAT_NAME} ${STBOOTFS_SIZE}

    cd ${IMGDEPLOYDIR};
    mcopy -i ${STBOOTFS_VFAT_NAME} -s ${IMAGE_ROOTFS}/../bootfs/* ::/

    (cd ${IMGDEPLOYDIR};ln -sf ${IMAGE_NAME}.bootfs.vfat ${IMAGE_LINK_NAME}.bootfs.vfat)
    (cd ${IMGDEPLOYDIR};xz -z -c ${IMAGE_NAME}.bootfs.vfat > ${IMAGE_NAME}.bootfs.vfat.xz )
    (cd ${IMGDEPLOYDIR};cp ${IMAGE_NAME}.bootfs.vfat.xz ${IMAGE_LINK_NAME}.bootfs.vfat.xz)


    # create ext4
#     rm -f ${STBOOTFS_EXT4_NAME}
#     dd if=/dev/zero of=${STBOOTFS_EXT4_NAME} seek=${STBOOTFS_SIZE} count=0 bs=1024

#     mkfs.ext4 -F -i 4096 ${STBOOTFS_EXT4_NAME} -d ${IMAGE_ROOTFS}/../bootfs
#     fsck.ext4 -pvfD ${STBOOTFS_EXT4_NAME} || [ $? -le 3 ]

#     (cd ${IMGDEPLOYDIR};ln -sf ${IMAGE_NAME}.bootfs.vfat ${IMAGE_LINK_NAME}.bootfs.ext4)
#     (cd ${IMGDEPLOYDIR};xz -z -c ${IMAGE_NAME}.bootfs.ext4 > ${IMAGE_NAME}.bootfs.ext4.xz )
#     (cd ${IMGDEPLOYDIR};cp ${IMAGE_NAME}.bootfs.ext4.xz ${IMAGE_LINK_NAME}.bootfs.ext4.xz)
}

