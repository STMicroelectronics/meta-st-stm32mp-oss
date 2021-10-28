FILESEXTRAPATHS:prepend:stm32mpcommon := "${THISDIR}/${PN}:"
FILE_ALSA_PATCH = "${@'file://0001-conf-add-card-configs-for-stm32mp1-boards.patch' if (d.getVar('STM32MP_BASE') == '') else ''}"
SRC_URI:append:stm32mpcommon = " ${FILE_ALSA_PATCH} "
