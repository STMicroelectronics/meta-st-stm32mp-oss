SUMMARY = "STM32MP bootfs Image"
LICENSE = "MIT"

inherit core-image

# Set ROOTFS_MAXSIZE to expected ROOTFS_SIZE to use the whole disk partition and leave extra space to user
IMAGE_ROOTFS_MAXSIZE     = "65536"
IMAGE_OVERHEAD_FACTOR    = "1"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# Add specific package for our image:
PACKAGE_INSTALL = " \
    kernel-imagebootfs \
    u-boot-stm32mp-oss-extlinux \
    ${@bb.utils.contains('MACHINE_FEATURES', 'splashscreen', 'u-boot-stm32mp-splash', '', d)} \
    ${@bb.utils.contains('RT_KERNEL', '1', '${RT_IMAGE_INSTALL}', '', d)} \
"

# Add specific autoresize package to bootfs
AUTORESIZE ?= ""
PACKAGE_INSTALL += " \
    ${@bb.utils.contains('COMBINED_FEATURES', 'autoresize', '${AUTORESIZE}', '', d)} \
"


# -----------------------------------------------
# st-image-partitions.inc
# -----------------------------------------------

# Disable flashlayout generation for the partition image as this is supposed
# to be done only for complete image
ENABLE_FLASHLAYOUT_CONFIG = "0"

# Disable image license summary generation for the partition image as this is
# supposed to be done only for complete image
ENABLE_IMAGE_LICENSE_SUMMARY = "0"

# Remove WIC image generation for the partition image
IMAGE_FSTYPES = "ext4 tar.xz"

# Append DISTRO to image name even if we're not using ST distro setting
# Mandatory to ease flashlayout file configuration
IMAGE_BASENAME_append = "${@'' if 'openstlinuxcommon' in OVERRIDES.split(':') else '-${DISTRO}'}"

# Reset image feature
IMAGE_FEATURE = ""

# Reset LINGUAS_INSTALL to avoid getting installed any locale-base package
LINGUAS_INSTALL = ""
IMAGE_LINGUAS = ""

# Reset LDCONFIG to avoid runing ldconfig on image.
LDCONFIGDEPEND = ""

# Remove from IMAGE_PREPROCESS_COMMAND useless buildinfo
IMAGE_PREPROCESS_COMMAND_remove = "buildinfo;"
# Remove from IMAGE_PREPROCESS_COMMAND the prelink_image as it could be run after
# we clean rootfs folder leading to cp error if '/etc/' folder is missing:
#   cp: cannot create regular file
#   ‘/local/YOCTO/build/tmp-glibc/work/stm32mp1-openstlinux_weston-linux-gnueabi/st-image-userfs/1.0-r0/rootfs/etc/prelink.conf’:
#   No such file or directory
IMAGE_PREPROCESS_COMMAND_remove = "prelink_image;"

IMAGE_PREPROCESS_COMMAND_append = "reformat_rootfs;"

# Cleanup rootfs newly created
reformat_rootfs() {
    if [ -d ${IMAGE_ROOTFS}/boot ]; then
        bbnote "Mountpoint /boot found in ${IMAGE_ROOTFS}"
        bbnote ">>> Remove all files and folder except /boot"
        TARGETROOTFS=${IMAGE_ROOTFS}/boot
        while [ "${TARGETROOTFS}" != "${IMAGE_ROOTFS}" ]
        do
            find $(dirname ${TARGETROOTFS})/ -mindepth 1 ! -regex "^${TARGETROOTFS}\(/.*\)?" -delete
            TARGETROOTFS=$(dirname ${TARGETROOTFS})
        done
        bbnote ">>> Move /boot contents to ${IMAGE_ROOTFS}"
        mv ${IMAGE_ROOTFS}/boot/* ${IMAGE_ROOTFS}/
        bbnote ">>> Remove remaining /boot folder"
        # Remove empty boot folder
        TARGETROOTFS=${IMAGE_ROOTFS}/boot
        while [ "${TARGETROOTFS}" != "${IMAGE_ROOTFS}" ]
        do
            bbnote ">>> Delete ${TARGETROOTFS}"
            rm -rf ${TARGETROOTFS}/
            TARGETROOTFS=$(dirname ${TARGETROOTFS})
        done
    else
        bbwarn "/boot folder not available in rootfs folder, no reformat done..."
    fi
}
