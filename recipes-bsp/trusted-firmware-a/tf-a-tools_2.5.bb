SUMMARY = "Cert_create & Fiptool for fip generation for Trusted Firmware-A"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

SRC_URI = "git://github.com/ARM-software/arm-trusted-firmware.git;protocol=https;nobranch=1"
#SRCREV corresponds to v2.5
SRCREV = "1e13c500a0351ac4b55d09a63f7008e2438550f8"

DEPENDS:class-nativesdk = "nativesdk-openssl"

S = "${WORKDIR}/git"

do_compile() {
    oe_runmake fiptool
}

do_install() {
    install -d ${D}${bindir}
    # fiptool
    install -m 0755 ${B}/tools/fiptool/fiptool ${D}${bindir}/fiptool
}

FILES:${PN}:class-nativesdk = "${bindir}/fiptool"

RDEPENDS:${PN}:class-nativesdk += "nativesdk-libcrypto"

BBCLASSEXTEND += "native nativesdk"
