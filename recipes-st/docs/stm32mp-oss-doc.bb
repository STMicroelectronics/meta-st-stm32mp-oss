DESCRIPTION = "Provide documentation for flashing"
HOMEPAGE = "www.st.com"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://how_to_flash.txt"

BBCLASSEXTEND = "native"

inherit deploy

do_install() {
    install -d ${D}/home/root
    install -m 0755 ${WORKDIR}/how_to_flash.txt ${D}/home/root/
}
do_deploy() {
    :
}
do_deploy_class-native() {
    install -d ${DEPLOYDIR}/
    install -m 0755 ${WORKDIR}/how_to_flash.txt ${DEPLOYDIR}/
}
addtask deploy before do_build after do_compile
