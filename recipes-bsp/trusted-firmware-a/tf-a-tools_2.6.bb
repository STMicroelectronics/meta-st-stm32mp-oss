SUMMARY = "Cert_create & Fiptool for fip generation for Trusted Firmware-A"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

SRC_URI = "git://github.com/ARM-software/arm-trusted-firmware.git;protocol=https;nobranch=1"
#SRCREV corresponds to v2.6
SRCREV = "a1f02f4f3daae7e21ee58b4c93ec3e46b8f28d15"

DEPENDS_class-nativesdk = "nativesdk-openssl"

S = "${WORKDIR}/git"

do_compile() {
    oe_runmake fiptool
}

do_install() {
    install -d ${D}${bindir}
    # fiptool
    install -m 0755 ${B}/tools/fiptool/fiptool ${D}${bindir}/fiptool
}

FILES_${PN}_class-nativesdk = "${bindir}/fiptool"

RDEPENDS_${PN}_class-nativesdk += "nativesdk-libcrypto"

BBCLASSEXTEND += "native nativesdk"
