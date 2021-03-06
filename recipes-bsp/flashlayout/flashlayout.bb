DESCRIPTION = "Flashlayout associated to board"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://FlashLayout_sdcard_stm32mp157c-dk2-optee.tsv.in \
    file://FlashLayout_sdcard_stm32mp157c-ev1-optee.tsv.in \
    file://FlashLayout_sdcard_stm32mp157c-dk2-raw.tsv.in \
    file://FlashLayout_sdcard_stm32mp157c-ev1-raw.tsv.in \
    "

S = "${WORKDIR}"

inherit deploy

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
ALLOW_EMPTY_${PN} = "1"

do_deploy_stm32mp15-disco-oss() {
    install -d ${DEPLOYDIR}/arm-trusted-firmware ${DEPLOYDIR}/fip
    install -d ${DEPLOYDIR}/flashlayout_st-image-weston/optee

    sed "s/#MACHINE#/${MACHINE}/g" ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-optee.tsv.in > ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-optee.tsv
    sed "s/#MACHINE#/${MACHINE}/g" ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-raw.tsv.in > ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-raw.tsv

    install -m 0644 ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-optee.tsv ${DEPLOYDIR}/flashlayout_st-image-weston/optee/
    install -m 0644 ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-raw.tsv ${DEPLOYDIR}/flashlayout_st-image-weston/optee/
}
do_deploy_stm32mp15-eval-oss() {
    install -d ${DEPLOYDIR}/arm-trusted-firmware ${DEPLOYDIR}/fip
    install -d ${DEPLOYDIR}/flashlayout_st-image-weston/optee

    sed "s/#MACHINE#/${MACHINE}/g" ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-optee.tsv.in > ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-optee.tsv
    sed "s/#MACHINE#/${MACHINE}/g" ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-raw.tsv.in > ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-raw.tsv

    install -m 0644 ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-optee.tsv ${DEPLOYDIR}/flashlayout_st-image-weston/optee/
    install -m 0644 ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-raw.tsv ${DEPLOYDIR}/flashlayout_st-image-weston/optee/
}

do_deploy_stm32mp1-disco-oss() {
    install -d ${DEPLOYDIR}/arm-trusted-firmware ${DEPLOYDIR}/fip
    install -d ${DEPLOYDIR}/flashlayout_st-image-weston/optee

    sed "s/#MACHINE#/${MACHINE}/g" ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-optee.tsv.in > ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-optee.tsv
    sed "s/#MACHINE#/${MACHINE}/g" ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-raw.tsv.in > ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-raw.tsv
    sed "s/#MACHINE#/${MACHINE}/g" ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-optee.tsv.in > ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-optee.tsv
    sed "s/#MACHINE#/${MACHINE}/g" ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-raw.tsv.in > ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-raw.tsv

    install -m 0644 ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-optee.tsv ${DEPLOYDIR}/flashlayout_st-image-weston/optee/
    #install -m 0644 ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-ev1-raw.tsv ${DEPLOYDIR}/flashlayout_st-image-weston/optee/
    install -m 0644 ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-optee.tsv ${DEPLOYDIR}/flashlayout_st-image-weston/optee/
    install -m 0644 ${WORKDIR}/FlashLayout_sdcard_stm32mp157c-dk2-raw.tsv ${DEPLOYDIR}/flashlayout_st-image-weston/optee/
}

do_deploy() {
}
addtask deploy before do_build after do_unpack

