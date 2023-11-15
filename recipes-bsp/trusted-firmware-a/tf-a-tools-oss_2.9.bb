tkaSUMMARY = "Cert_create & Fiptool for fip generation for Trusted Firmware-A"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

#SRC_URI = "git://github.com/ARM-software/arm-trusted-firmware.git;protocol=https;nobranch=1"
SRC_URI = "git://git.trustedfirmware.org/TF-A/trusted-firmware-a.git;protocol=https;nobranch=1"
#SRCREV corresponds to v2.9
SRCREV = "d3e71ead6ea5bc3555ac90a446efec84ef6c6122"

DEPENDS += "dtc-native openssl"

S = "${WORKDIR}/git"

HOSTCC:class-native = "${BUILD_CC}"
HOSTCC:class-nativesdk = "${CC}"
EXTRA_OEMAKE += "HOSTCC='${HOSTCC}' OPENSSL_DIR='${STAGING_EXECPREFIXDIR}'"
EXTRA_OEMAKE += "certtool fiptool"

do_configure[noexec] = "1"


do_install() {
    install -d ${D}${bindir}
    install -m 0755 \
            ${B}/tools/fiptool/fiptool \
            ${B}/tools/cert_create/cert_create \
            ${D}${bindir}/
}

BBCLASSEXTEND += "native nativesdk"
