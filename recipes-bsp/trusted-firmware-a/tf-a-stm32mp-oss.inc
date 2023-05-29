SUMMARY = "ARM Trusted Firmware"
DESCRIPTION = "ARM Trusted Firmware provides a reference implementation of \
Secure World software for ARMv8-A, including Exception Level 3 (EL3) software. \
It provides implementations of various ARM interface standards such as the \
Power State Coordination Interface (PSCI), Trusted Board Boot Requirements \
(TBBR) and Secure monitor code."
HOMEPAGE = "http://infocenter.arm.com/help/topic/com.arm.doc.dui0928e/CJHIDGJF.html"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

PACKAGE_ARCH = "${MACHINE_ARCH}"
PROVIDES += "virtual/trusted-firmware-a"

PV = "${TFA_VERSION}-git${SRCPV}"

SRC_URI = "git://github.com/ARM-software/arm-trusted-firmware.git;protocol=https;nobranch=1"
#2.5 SRCREV = "1e13c500a0351ac4b55d09a63f7008e2438550f8"
#2.4 SRCREV = "e2c509a39c6cc4dda8734e6509cdbe6e3603cdfc"

SRC_URI += "file://metadata.bin"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit deploy

DEPENDS += "dtc-native tf-a-tools-native"

# ledge-stm32mp157c-dk2 specific
TF_A_PLATFORM:stm32mp15common = "stm32mp1"

# Extra make settings
EXTRA_OEMAKE = ' CROSS_COMPILE=${TARGET_PREFIX} '
EXTRA_OEMAKE += ' PLAT=${TF_A_PLATFORM} '
EXTRA_OEMAKE:append:armv7a = ' ARCH=aarch32 ARM_ARCH_MAJOR=7'
EXTRA_OEMAKE:append:armv7ve = ' ARCH=aarch32 ARM_ARCH_MAJOR=7'

# Debug support
EXTRA_OEMAKE += 'DEBUG=1'
EXTRA_OEMAKE += "LOG_LEVEL=40"

EXTRA_FWUPDATE = "${@bb.utils.contains('MACHINE_FEATURES', 'fw-update', 'PSA_FWU_SUPPORT=1', '', d)}"


TF_A_ENABLE_METADATA = "0"
#TF_A_ENABLE_METADATA ??= "${@bb.utils.contains('MACHINE_FEATURES', 'fw-update', '1', '0', d)}"
TF_A_METADATA_NAME ?= "metadata"
TF_A_METADATA_SUFFIX ?= "bin"
TF_A_METADATA_BINARY ??= "${TF_A_METADATA_NAME}.${TF_A_METADATA_SUFFIX}"

TF_A_METADATA_TOOL ?= "tools/fwu_gen_metadata/fwumd_tool.py"
TF_A_METADATA_JSON ?= "plat/st/stm32mp1/default_metadata.json"


# no fip
#EXTRA_OEMAKE += "STM32MP_USE_STM32IMAGE=1"

CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

do_configure[noexec] = "1"

do_compile[depends] = "virtual/bootloader:do_deploy virtual/optee-os:do_deploy"

do_compile() {
    if [ -n "${TF_A_DEVICETREE}" ]; then
        for dt in ${TF_A_DEVICETREE}; do
            soc_extra_opt=""
            soc_suffix=""
            if [ -n "${STM32MP_SOC_NAME}" ]; then
                for soc in ${STM32MP_SOC_NAME}; do
                    if [ "$(echo ${dt} | grep -c ${soc})" -eq 1 ]; then
                        soc_extra_opt="$(echo ${soc} | awk '{print toupper($0)}')=1"
                        soc_suffix="-${soc}"
                    fi
                done
            fi
            echo "################## SDCARD ###########################"
            # BL2 sdcard with no firmware update
            oe_runmake -C ${S} BUILD_PLAT=${B}/sdcard${soc_suffix} DTB_FILE_NAME=${dt}.dtb STM32MP_SDMMC=1 STM32MP_FORCE_MTD_START_OFFSET=0x00104400 ${soc_extra_opt} all
            if [ -f ${B}/sdcard${soc_suffix}/tf-a-${dt}.stm32 ]; then
                cp ${B}/sdcard${soc_suffix}/tf-a-${dt}.stm32 ${B}/sdcard${soc_suffix}/tf-a-${dt}-sdcard.stm32
            fi
            # BL2 sdcard
            oe_runmake -C ${S} BUILD_PLAT=${B}/sdcardfwup${soc_suffix} DTB_FILE_NAME=${dt}.dtb STM32MP_SDMMC=1 ${EXTRA_FWUPDATE} ${soc_extra_opt} all
            if [ -f ${B}/sdcardfwup${soc_suffix}/tf-a-${dt}.stm32 ]; then
                cp ${B}/sdcardfwup${soc_suffix}/tf-a-${dt}.stm32 ${B}/sdcardfwup${soc_suffix}/tf-a-${dt}-sdcard-fwup.stm32
            fi
            echo "################## EMMC ###########################"
            # BL2 emmc with no firmware update
            oe_runmake -C ${S} BUILD_PLAT=${B}/emmc${soc_suffix} DTB_FILE_NAME=${dt}.dtb STM32MP_EMMC=1  STM32MP_FORCE_MTD_START_OFFSET=0x00180000 ${soc_extra_opt} all
            if [ -f ${B}/emmc${soc_suffix}/tf-a-${dt}.stm32 ]; then
                cp ${B}/emmc${soc_suffix}/tf-a-${dt}.stm32 ${B}/emmc${soc_suffix}/tf-a-${dt}-emmc.stm32
            fi
            # BL2 emmc
            oe_runmake -C ${S} BUILD_PLAT=${B}/emmcfwup${soc_suffix} DTB_FILE_NAME=${dt}.dtb STM32MP_EMMC=1 ${EXTRA_FWUPDATE} ${soc_extra_opt} all
            if [ -f ${B}/emmcfwup${soc_suffix}/tf-a-${dt}.stm32 ]; then
                cp ${B}/emmcfwup${soc_suffix}/tf-a-${dt}.stm32 ${B}/emmcfwup${soc_suffix}/tf-a-${dt}-emmc-fwup.stm32
            fi
            echo "################## NAND ###########################"
            # BL2 nand  with no firmware update
            # NOTE: no fiwrmware updata available for nand
            # STM32MP_FORCE_MTD_START_OFFSET=0x00100000
            oe_runmake -C ${S} BUILD_PLAT=${B}/nand${soc_suffix} DTB_FILE_NAME=${dt}.dtb STM32MP_RAW_NAND=1 STM32MP_FORCE_MTD_START_OFFSET=0x00200000  ${soc_extra_opt} all
            if [ -f ${B}/nand${soc_suffix}/tf-a-${dt}.stm32 ]; then
                cp ${B}/nand${soc_suffix}/tf-a-${dt}.stm32 ${B}/nand${soc_suffix}/tf-a-${dt}-nand.stm32
            fi
            echo "################## NOR ###########################"
            # BL2 nor  with no firmware update
            # NOTE: no fiwrmware updata availabel for nor
            # STM32MP_FORCE_MTD_START_OFFSET=0x00100000
            oe_runmake -C ${S} BUILD_PLAT=${B}/nor${soc_suffix} DTB_FILE_NAME=${dt}.dtb STM32MP_SPI_NOR=1  ${soc_extra_opt} all
            if [ -f ${B}/nor${soc_suffix}/tf-a-${dt}.stm32 ]; then
                cp ${B}/nor${soc_suffix}/tf-a-${dt}.stm32 ${B}/nor${soc_suffix}/tf-a-${dt}-nor.stm32
            fi
            echo "################## SPINAND ###########################"
            # BL2 spinand  with no firmware update
            # NOTE: no fiwrmware updata availabel for spinand
            #oe_runmake -C ${S} BUILD_PLAT=${B}/spinand${soc_suffix} DTB_FILE_NAME=${dt}.dtb STM32MP_SPI_NAND=1 STM32MP_FORCE_MTD_START_OFFSET=0x00200000 ${soc_extra_opt} all
            #if [ -f ${B}/spinand${soc_suffix}/tf-a-${dt}.stm32 ]; then
            #    cp ${B}/spinand${soc_suffix}/tf-a-${dt}.stm32 ${B}/spinand${soc_suffix}/tf-a-${dt}-spinand.stm32
            #fi
            echo "################## USB ###########################"
            # BL2 USB flashing
            oe_runmake -C ${S} BUILD_PLAT=${B}/usb${soc_suffix} DTB_FILE_NAME=${dt}.dtb STM32MP_USB_PROGRAMMER=1 ${soc_extra_opt} all
            if [ -f ${B}/usb${soc_suffix}/tf-a-${dt}.stm32 ]; then
                cp ${B}/usb${soc_suffix}/tf-a-${dt}.stm32 ${B}/usb${soc_suffix}/tf-a-${dt}-usb.stm32
            fi
            echo "################## FIP ###########################"
            # generate fip for optee
            oe_runmake -C ${S} AARCH32_SP=optee \
                BUILD_PLAT=${B}/fip-optee-${dt} \
                DTB_FILE_NAME=${dt}.dtb \
                BL33=${DEPLOY_DIR}/images/${MACHINE}/u-boot/u-boot-nodtb-${dt}.bin \
                BL33_CFG=${DEPLOY_DIR}/images/${MACHINE}/u-boot/u-boot-${dt}.dtb \
                BL32=${DEPLOY_DIR}/images/${MACHINE}/optee/tee-header_v2-${dt}-optee.bin \
                BL32_EXTRA1=${DEPLOY_DIR}/images/${MACHINE}/optee/tee-pager_v2-${dt}-optee.bin \
                BL32_EXTRA2=${DEPLOY_DIR}/images/${MACHINE}/optee/tee-pageable_v2-${dt}-optee.bin \
                fip FIP_NAME=fip-${dt}-optee.bin
        done
    fi
    # scmi management: need to update only u-boot
    if [ -n "${TF_A_DEVICETREE}" ]; then
        for dt in ${TF_A_DEVICETREE}; do
            soc_extra_opt=""
            soc_suffix=""
            if [ -n "${STM32MP_SOC_NAME}" ]; then
                for soc in ${STM32MP_SOC_NAME}; do
                    if [ "$(echo ${dt} | grep -c ${soc})" -eq 1 ]; then
                        soc_extra_opt="$(echo ${soc} | awk '{print toupper($0)}')=1"
                        soc_suffix="-${soc}"
                    fi
                done
            fi
            if [ -e ${DEPLOY_DIR}/images/${MACHINE}/u-boot/u-boot-${dt}-scmi.dtb ]; then
                oe_runmake -C ${S} AARCH32_SP=optee \
                    BUILD_PLAT=${B}/fip-optee-${dt} \
                    DTB_FILE_NAME=${dt}.dtb \
                    BL33=${DEPLOY_DIR}/images/${MACHINE}/u-boot/u-boot-nodtb-${dt}-scmi.bin \
                    BL33_CFG=${DEPLOY_DIR}/images/${MACHINE}/u-boot/u-boot-${dt}-scmi.dtb \
                    BL32=${DEPLOY_DIR}/images/${MACHINE}/optee/tee-header_v2-${dt}-optee.bin \
                    BL32_EXTRA1=${DEPLOY_DIR}/images/${MACHINE}/optee/tee-pager_v2-${dt}-optee.bin \
                    BL32_EXTRA2=${DEPLOY_DIR}/images/${MACHINE}/optee/tee-pageable_v2-${dt}-optee.bin \
                    fip FIP_NAME=fip-${dt}-scmi-optee.bin
            fi
        done
    fi

    if [ "${TF_A_ENABLE_METADATA}" = "1" ]; then
        ${S}/${TF_A_METADATA_TOOL} jsonparse "${S}/${TF_A_METADATA_JSON}" -b "${B}/${TF_A_METADATA_NAME}.${TF_A_METADATA_SUFFIX}"
    fi
}

do_deploy() {
    install -d ${DEPLOYDIR}/arm-trusted-firmware/debug
    install -d ${DEPLOYDIR}/fip

    # install bl2 sdcard
    if [ -n "${TF_A_DEVICETREE_SDCARD}" ]; then
        for dt in ${TF_A_DEVICETREE_SDCARD}; do
            soc_extra_opt=""
            soc_suffix=""
            if [ -n "${STM32MP_SOC_NAME}" ]; then
                for soc in ${STM32MP_SOC_NAME}; do
                    if [ "$(echo ${dt} | grep -c ${soc})" -eq 1 ]; then
                        soc_extra_opt="$(echo ${soc} | awk '{print toupper($0)}')=1"
                        soc_suffix="-${soc}"
                    fi
                done
            fi
            install -m 644 ${B}/sdcard${soc_suffix}/tf-a-${dt}-sdcard.stm32 ${DEPLOYDIR}/arm-trusted-firmware/
            if [ -d  ${B}/sdcardfwup${soc_suffix} ]; then
                install -m 644 ${B}/sdcardfwup${soc_suffix}/tf-a-${dt}-sdcard-fwup.stm32 ${DEPLOYDIR}/arm-trusted-firmware/
            fi
        done
    fi
    # install bl2 emmc
    if [ -n "${TF_A_DEVICETREE_EMMC}" ]; then
        for dt in ${TF_A_DEVICETREE_EMMC}; do
            soc_extra_opt=""
            soc_suffix=""
            if [ -n "${STM32MP_SOC_NAME}" ]; then
                for soc in ${STM32MP_SOC_NAME}; do
                    if [ "$(echo ${dt} | grep -c ${soc})" -eq 1 ]; then
                        soc_extra_opt="$(echo ${soc} | awk '{print toupper($0)}')=1"
                        soc_suffix="-${soc}"
                    fi
                done
            fi
            install -m 644 ${B}/emmc${soc_suffix}/tf-a-${dt}-emmc.stm32 ${DEPLOYDIR}/arm-trusted-firmware/
            if [ -d  ${B}/emmcfwup${soc_suffix} ]; then
                install -m 644 ${B}/emmcfwup${soc_suffix}/tf-a-${dt}-emmc-fwup.stm32 ${DEPLOYDIR}/arm-trusted-firmware/
            fi
        done
    fi
    # install bl2 nand
    if [ -n "${TF_A_DEVICETREE_NAND}" ]; then
        for dt in ${TF_A_DEVICETREE_NAND}; do
            soc_extra_opt=""
            soc_suffix=""
            if [ -n "${STM32MP_SOC_NAME}" ]; then
                for soc in ${STM32MP_SOC_NAME}; do
                    if [ "$(echo ${dt} | grep -c ${soc})" -eq 1 ]; then
                        soc_extra_opt="$(echo ${soc} | awk '{print toupper($0)}')=1"
                        soc_suffix="-${soc}"
                    fi
                done
            fi
            install -m 644 ${B}/nand${soc_suffix}/tf-a-${dt}-nand.stm32 ${DEPLOYDIR}/arm-trusted-firmware/
        done
    fi
    # install bl2 nor
    if [ -n "${TF_A_DEVICETREE_NOR}" ]; then
        for dt in ${TF_A_DEVICETREE_NOR}; do
            soc_extra_opt=""
            soc_suffix=""
            if [ -n "${STM32MP_SOC_NAME}" ]; then
                for soc in ${STM32MP_SOC_NAME}; do
                    if [ "$(echo ${dt} | grep -c ${soc})" -eq 1 ]; then
                        soc_extra_opt="$(echo ${soc} | awk '{print toupper($0)}')=1"
                        soc_suffix="-${soc}"
                    fi
                done
            fi
            install -m 644 ${B}/nor${soc_suffix}/tf-a-${dt}-nor.stm32 ${DEPLOYDIR}/arm-trusted-firmware/
        done
    fi
    # install bl2 spinand
#     if [ -n "${TF_A_DEVICETREE_SPINAND}" ]; then
#         for dt in ${TF_A_DEVICETREE_SPINAND}; do
#             soc_extra_opt=""
#             soc_suffix=""
#             if [ -n "${STM32MP_SOC_NAME}" ]; then
#                 for soc in ${STM32MP_SOC_NAME}; do
#                     if [ "$(echo ${dt} | grep -c ${soc})" -eq 1 ]; then
#                         soc_extra_opt="$(echo ${soc} | awk '{print toupper($0)}')=1"
#                         soc_suffix="-${soc}"
#                     fi
#                 done
#             fi
#             install -m 644 ${B}/spinand${soc_suffix}/tf-a-${dt}-spinand.stm32 ${DEPLOYDIR}/arm-trusted-firmware/
#         done
#     fi
    if [ -n "${TF_A_DEVICETREE}" ]; then
        for dt in ${TF_A_DEVICETREE}; do
            soc_extra_opt=""
            soc_suffix=""
            if [ -n "${STM32MP_SOC_NAME}" ]; then
                for soc in ${STM32MP_SOC_NAME}; do
                    if [ "$(echo ${dt} | grep -c ${soc})" -eq 1 ]; then
                        soc_extra_opt="$(echo ${soc} | awk '{print toupper($0)}')=1"
                        soc_suffix="-${soc}"
                    fi
                done
            fi

            # BL2 usb
            install -m 644 ${B}/usb${soc_suffix}/tf-a-${dt}-usb.stm32 ${DEPLOYDIR}/arm-trusted-firmware/

            # FIP optee
            install -m 644 ${B}/fip-optee-${dt}/fip-${dt}-optee.bin ${DEPLOYDIR}/fip/
            install -m 644 ${B}/fip-optee-${dt}/fdts/${dt}-fw-config.dtb ${DEPLOYDIR}/arm-trusted-firmware/${dt}-fw-config-optee.dtb
        done
    fi
    # scmi management
    if [ -n "${TF_A_DEVICETREE}" ]; then
        for dt in ${TF_A_DEVICETREE}; do
            soc_extra_opt=""
            soc_suffix=""
            if [ -n "${STM32MP_SOC_NAME}" ]; then
                for soc in ${STM32MP_SOC_NAME}; do
                    if [ "$(echo ${dt} | grep -c ${soc})" -eq 1 ]; then
                        soc_extra_opt="$(echo ${soc} | awk '{print toupper($0)}')=1"
                        soc_suffix="-${soc}"
                    fi
                done
            fi
            if [ -e ${B}/fip-optee-${dt}/fip-${dt}-scmi-optee.bin ]; then
                # FIP optee
                install -m 644 ${B}/fip-optee-${dt}/fip-${dt}-scmi-optee.bin ${DEPLOYDIR}/fip/
            fi
        done
    fi
    if [ "${TF_A_ENABLE_METADATA}" = "1" ]; then
        install -d "${DEPLOYDIR}/arm-trusted-firmware"
        if [ -f "${B}/${TF_A_METADATA_NAME}.${TF_A_METADATA_SUFFIX}" ]; then
            install -m 644 "${B}/${TF_A_METADATA_NAME}.${TF_A_METADATA_SUFFIX}" "${DEPLOYDIR}/arm-trusted-firmware/${TF_A_METADATA_BIN}"
        fi
    else
        if [ -e "${WORKDIR}/metadata.bin" ]; then
            install -m 644 "${WORKDIR}/metadata.bin" "${DEPLOYDIR}/arm-trusted-firmware/"
        fi
    fi
}
addtask deploy before do_build after do_compile

FILES:${PN} = "/boot"
