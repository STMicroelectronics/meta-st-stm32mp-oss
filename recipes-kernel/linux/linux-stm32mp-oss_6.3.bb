# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "6.3.4"
LINUX_VERSION = "6.3"

SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v6.x/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"
SRC_URI += "file://0016-rpmsg-virtio-overwrite-dst-add-on-ept-callback-if-se.patch"

SRC_URI[kernel.sha256sum] = "5cc25097ab31f02e507be17acfd032aacace93262596f6d011fe9fb119fb0efd"
# ------------------------------------------------
require linux-stm32mp-oss.inc
