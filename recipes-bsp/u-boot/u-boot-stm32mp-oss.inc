HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"
PE = "1"

SRC_URI = "git://git.denx.de/u-boot.git;nobranch=1"

SRC_URI_append_stm32mp1common = " \
    file://stm32mp15_disco_defconfig \
    file://stm32mp15_eval_defconfig \
    "

S = "${WORKDIR}/git"
PV = "${UBOOT_VERSION}-git${SRCPV}"

require recipes-bsp/u-boot/u-boot.inc
RPROVIDES_${PN} += "u-boot"

DEPENDS += "bc-native dtc-native"

do_configure_prepend_stm32mp1common() {
    for conf in ${UBOOT_MACHINE};
    do
        if [ -f ${WORKDIR}/$conf ] ;
        then
            cp ${WORKDIR}/$conf ${S}/configs/
        fi
    done
}

# -----------------------------------------------------------------------------
# Append deploy to handle specific device tree binary deployement
#
SPL_BINARY_LEDGE_stm32mp1common = "spl/u-boot-spl.stm32"
do_deploy[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}/u-boot"
do_deploy_append() {
if [ -n "${SPL_BINARY_LEDGE}" ]; then
    # Clean deploydir from any available binary first
    # This allows to only install the devicetree binary ones
    rm -rf ${DEPLOYDIR}

    # Install destination folder
    install -d ${DEPLOYDIR}

    if [ -n "${UBOOT_CONFIG}" ]; then
        unset i j k
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]; then
                    for binary in ${UBOOT_BINARIES}; do
                        binarysuffix=$(echo ${binary} | cut -d'.' -f2)
                        k=$(expr $k + 1);
                        if [ $k -eq $i ]; then
                            if [ -f ${B}/${config}/${binary} ];
                            then
                                install -m 644 ${B}/${config}/${binary} ${DEPLOYDIR}/u-boot-${type}.${binarysuffix}
                                install -m 644 ${B}/${config}/u-boot-nodtb.bin ${DEPLOYDIR}/u-boot-nodtb-${type}.bin
                            fi
                            # As soon as SPL binary exists, install it
                            # This allow to mix u-boot configuration, with and without SPL
                            if [ -f ${B}/${config}/${SPL_BINARY_LEDGE} ]; then
                                install -d ${DEPLOYDIR}/spl
                                install -m 644 ${B}/${config}/${SPL_BINARY_LEDGE} ${DEPLOYDIR}/${SPL_BINARY_LEDGE}-${type}
                            fi
                        fi
                    done
                    # export u-boot-no-dtb
                    #install -m 644 ${B}/${config}/u-boot-nodtb.bin ${DEPLOYDIR}/u-boot-nodtb.bin
                    # export devicetree
                    install -m 644 ${B}/${config}/u-boot.dtb ${DEPLOYDIR}/u-boot-${type}.dtb
                    #install -m 644 ${B}/${config}/u-boot-nodtb.bin ${DEPLOYDIR}/u-boot-nodtb.bin
                    unset k
                fi
            done
            unset j
        done
        unset i
    else
            bbfatal "Wrong u-boot-ledge configuration: please make sure to use UBOOT_CONFIG through BOOTSCHEME_LABELS config"
    fi
fi
}
