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
            # BL2 sdcard
            oe_runmake -C ${S} BUILD_PLAT=${B}/sdcard${soc_suffix} DTB_FILE_NAME=${dt}.dtb STM32MP_SDMMC=1 ${soc_extra_opt} all
            if [ -f ${B}/sdcard${soc_suffix}/tf-a-${dt}.stm32 ]; then
                cp ${B}/sdcard${soc_suffix}/tf-a-${dt}.stm32 ${B}/sdcard${soc_suffix}/tf-a-${dt}-sdcard.stm32
            fi

            # BL2 USB flashing
            oe_runmake -C ${S} BUILD_PLAT=${B}/usb${soc_suffix} DTB_FILE_NAME=${dt}.dtb STM32MP_USB_PROGRAMMER=1 ${soc_extra_opt} all
            if [ -f ${B}/usb${soc_suffix}/tf-a-${dt}.stm32 ]; then
                cp ${B}/usb${soc_suffix}/tf-a-${dt}.stm32 ${B}/usb${soc_suffix}/tf-a-${dt}-usb.stm32
            fi

            # generate fip for trusted
#            oe_runmake -C ${S} AARCH32_SP=sp_min \
#                BUILD_PLAT=${B}/fip-trusted-${dt} \
#                DTB_FILE_NAME=${dt}.dtb \
#                BL33=${DEPLOY_DIR}/images/${MACHINE}/u-boot/u-boot-nodtb-${dt}.bin \
#                BL33_CFG=${DEPLOY_DIR}/images/${MACHINE}/u-boot/u-boot-${dt}.dtb \
#                fip FIP_NAME=fip-${dt}-trusted.bin

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
}

do_deploy() {
    install -d ${DEPLOYDIR}/arm-trusted-firmware/debug
    install -d ${DEPLOYDIR}/fip
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

            # BL2 sdcard
            install -m 644 ${B}/sdcard${soc_suffix}/tf-a-${dt}-sdcard.stm32 ${DEPLOYDIR}/arm-trusted-firmware/
            # BL2 usb
            install -m 644 ${B}/usb${soc_suffix}/tf-a-${dt}-usb.stm32 ${DEPLOYDIR}/arm-trusted-firmware/

#            # FIP trusted
#            install -m 644 ${B}/fip-trusted-${dt}/fip-${dt}-trusted.bin ${DEPLOYDIR}/fip/
#            install -m 644 ${B}/fip-trusted-${dt}/bl32.bin ${DEPLOYDIR}/arm-trusted-firmware/bl32-${dt}-trusted.bin
#            install -m 644 ${B}/fip-trusted-${dt}/bl32/bl32.elf ${DEPLOYDIR}/arm-trusted-firmware/debug/bl32-${dt}-trusted.elf
#            install -m 644 ${B}/fip-trusted-${dt}/fdts/${dt}-fw-config.dtb ${DEPLOYDIR}/arm-trusted-firmware/${dt}-fw-config-trusted.dtb
            # FIP optee
            install -m 644 ${B}/fip-optee-${dt}/fip-${dt}-optee.bin ${DEPLOYDIR}/fip/
            install -m 644 ${B}/fip-optee-${dt}/fdts/${dt}-fw-config.dtb ${DEPLOYDIR}/arm-trusted-firmware/${dt}-fw-config-optee.dtb
        done
    fi
}
addtask deploy before do_build after do_compile

FILES:${PN} = "/boot"

# Configure default mode  (All supported device type)
EXTRA_OEMAKE += 'STM32MP_SDMMC=1'
#EXTRA_OEMAKE += 'STM32MP_EMMC=1'
#EXTRA_OEMAKE += 'STM32MP_QSPI_NOR=1'
#EXTRA_OEMAKE += 'STM32MP_RAW_NAND=1'
#EXTRA_OEMAKE += 'STM32MP_SPI_NAND=1'

