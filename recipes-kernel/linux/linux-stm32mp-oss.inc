SUMMARY = "Linux STM32MP Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
include linux-stm32mp.inc
include linux-stm32mp-sign-kernel.inc

#SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-5.15.tar.xz;name=kernel"

SRC_URI += " \
    file://0001-ARM-STM32MP15-support-of-Preemptible-Kernel.patch \
    "

PV = "${LINUX_VERSION}"

S = "${WORKDIR}/linux-${ST_KERNEL_VERSION}"

# -------------------------------------------------------------
# Defconfig
#
KERNEL_DEFCONFIG        = "defconfig"
KERNEL_CONFIG_FRAGMENTS = "${@bb.utils.contains('KERNEL_DEFCONFIG', 'defconfig', '${WORKDIR}/fragments/5.x/fragment-01-multiv7_cleanup.config', '', d)}"
KERNEL_CONFIG_FRAGMENTS += "${@bb.utils.contains('KERNEL_DEFCONFIG', 'defconfig', '${WORKDIR}/fragments/5.x/fragment-02-multiv7_addons.config', '', d)}"
KERNEL_CONFIG_FRAGMENTS += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${WORKDIR}/fragments/5.x/fragment-03-systemd.config', '', d)} "
KERNEL_CONFIG_FRAGMENTS += "${@bb.utils.contains('COMBINED_FEATURES', 'optee', '${WORKDIR}/fragments/5.x/fragment-04-optee.config', '', d)}"
KERNEL_CONFIG_FRAGMENTS += "${WORKDIR}/fragments/5.x/fragment-05-modules.config"
KERNEL_CONFIG_FRAGMENTS += "${@oe.utils.ifelse(d.getVar('KERNEL_SIGN_ENABLE') == '1', '${WORKDIR}/fragments/5.x/fragment-06-signature.config','')} "
KERNEL_CONFIG_FRAGMENTS += "${WORKDIR}/fragments/5.x/fragment-09-efi.config"

SRC_URI += "file://5.x/fragment-01-multiv7_cleanup.config;subdir=fragments"
SRC_URI += "file://5.x/fragment-02-multiv7_addons.config;subdir=fragments"
SRC_URI += "file://5.x/fragment-03-systemd.config;subdir=fragments"
SRC_URI += "file://5.x/fragment-04-optee.config;subdir=fragments"
SRC_URI += "file://5.x/fragment-05-modules.config;subdir=fragments"
SRC_URI += "file://5.x/fragment-06-signature.config;subdir=fragments"
SRC_URI += "file://5.x/fragment-09-efi.config;subdir=fragments"

# -------------------------------------------------------------
# Kernel Args
#
KERNEL_EXTRA_ARGS += "LOADADDR=${ST_KERNEL_LOADADDR}"

deltask shared_workdir
addtask shared_workdir after do_compile_kernelmodules before do_install
do_shared_workdir[depends] +="virtual/kernel:do_compile_kernelmodules"

# -------------------------------------------------------------
# RT Management
include ${@bb.utils.contains('RT_KERNEL', '1', 'linux-stm32mp-oss-rt.inc', '', d)}
# -------------------------------------------------------------
# modprobe action
#
do_install_append() {
    install -d ${D}/${sysconfdir}/modprobe.d/
    echo "softdep snd-soc-cs42l51 pre: snd-soc-cs42l51-i2c" > ${D}/${sysconfdir}/modprobe.d/stm32mp1-snd.conf
}
FILES_${KERNEL_PACKAGE_NAME}-modules += "${sysconfdir}/modprobe.d"
