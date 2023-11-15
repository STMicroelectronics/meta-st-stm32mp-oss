FILESEXTRAPATHS:prepend:stm32mpcommon := "${THISDIR}/${PN}:"

SRC_URI:append:stm32mpcommon = " \
       file://busybox-stm32mp.cfg \
       "

#inherit update-rc.d
DEPENDS:append:stm32mpcommon = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '', 'update-rc.d-native', d)}"
