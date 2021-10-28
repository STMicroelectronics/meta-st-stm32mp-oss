SUMMARY = "Universal Boot Loader Splash Screen for stm32mp embedded devices"
#TODO Need to review the exact license we want to have for the specific BMP we provide.
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ac3e0fd89b582e9fc11d534a27636636"

SRC_URI = "${@bb.utils.contains('MACHINE_FEATURES', 'splashscreen', 'file://${UBOOT_SPLASH_SRC}', '', d)}"
SRC_URI += "file://ST_logo_2020_yellow_V_rgb_480x800_8bit.bmp"
SRC_URI += "file://LICENSE"

S = "${WORKDIR}"

UBOOT_SPLASH_SRC = "ST_logo_2020_blue_V_rgb_352x480_8bit.bmp"
UBOOT_SPLASH_IMAGE ?= "splash"

inherit deploy

do_compile[noexec] = "1"

do_install() {
    install -d ${D}/boot
    if [ -e "${S}/${UBOOT_SPLASH_SRC}" ]; then
        install -m 644 ${S}/${UBOOT_SPLASH_SRC} ${D}/boot/${UBOOT_SPLASH_IMAGE}.bmp
    fi
    if [ -e "${S}/ST_logo_2020_yellow_V_rgb_480x800_8bit.bmp" ]; then
        install -m 644 ${S}/ST_logo_2020_yellow_V_rgb_480x800_8bit.bmp ${D}/boot/${UBOOT_SPLASH_IMAGE}_yellow.bmp
    fi
}

ALLOW_EMPTY:${PN} = "1"
FILES:${PN} = "/boot"
