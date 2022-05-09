SUMMARY = "OPTEE Client"
HOMEPAGE = "https://github.com/OP-TEE/optee_client"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=69663ab153298557a59c67a60a743e5b"

inherit python3native systemd cmake

PROVIDES += "optee-client"

SRC_URI = "git://github.com/OP-TEE/optee_client.git;protocol=https;branch=master \
           file://tee-supplicant.service \
    "

S = "${WORKDIR}/git"

PV = "${OPTEE_VERSION}-git${SRCPV}"

SYSTEMD_SERVICE:${PN} = "tee-supplicant.service"


EXTRA_OECMAKE = " \
    -DCFG_TEE_FS_PARENT_PATH='/data/tee' \
    -DCFG_WERROR=OFF \
    -DCFG_TEE_CLIENT_LOG_LEVEL=2 \
    -DCFG_TEE_CLIENT_LOG_FILE='/data/tee/teec.log' \
    -DBUILD_SHARED_LIBS=ON \
    "

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        sed -i -e s:/etc:${sysconfdir}:g \
           -e s:/usr/bin:${bindir}:g \
              ${WORKDIR}/tee-supplicant.service

        install -D -p -m0644 ${WORKDIR}/tee-supplicant.service ${D}${systemd_system_unitdir}/tee-supplicant.service
    fi
}
RPROVIDES:${PN} += "optee-client"
