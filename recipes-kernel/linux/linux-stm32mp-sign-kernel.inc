DEPENDS:append = " openssl-native efitools-native sbsigntool-native "

SRC_URI:append = " \
    file://uefi-certificates/KEK.auth \
    file://uefi-certificates/KEK.crt \
    file://uefi-certificates/KEK.key \
    file://uefi-certificates/PK.auth \
    file://uefi-certificates/PK.crt \
    file://uefi-certificates/PK.key \
    file://uefi-certificates/db.key \
    file://uefi-certificates/db.crt \
    file://uefi-certificates/db.auth \
    "

do_install:append() {
    # the public part of certificate must be installed on
    # secure partition
    # Hack: for demonstration, the auth certificate will be installed
    # on boot parttition
    for img in ${KERNEL_IMAGETYPE} ${KERNEL_ALT_IMAGETYPE}; do
        if [ ! "x$img" = "xzImage" ]; then
            continue;
        fi
        if [ -f ${D}/boot/$img-${KERNEL_VERSION} ]; then
            cp ${D}/boot/$img-${KERNEL_VERSION} ${D}/boot/$img
        fi
        KERNEL_SIGN_IMAGE="${D}/boot/$img"
        ${STAGING_BINDIR_NATIVE}/sbsign --key ${WORKDIR}/uefi-certificates/db.key --cert ${WORKDIR}/uefi-certificates/db.crt $KERNEL_SIGN_IMAGE
        mv ${D}/boot/$img ${D}/boot/$img-${KERNEL_VERSION}
    done
    # install auth certificate file
    # Hack section: will be installed on secure partition
    install -d ${D}/boot/uefi-certificates/
    echo "HACK: file present on this directory will be installed on secure partition." > ${D}/boot/uefi-certificates/README.install
    echo "The files are installed on this directory only for demonstration" >> ${D}/boot/uefi-certificates/README.install
    install -m 0644 ${WORKDIR}/uefi-certificates/PK.auth ${D}/boot/uefi-certificates/
    install -m 0644 ${WORKDIR}/uefi-certificates/KEK.auth ${D}/boot/uefi-certificates/
    install -m 0644 ${WORKDIR}/uefi-certificates/db.auth ${D}/boot/uefi-certificates/
}

