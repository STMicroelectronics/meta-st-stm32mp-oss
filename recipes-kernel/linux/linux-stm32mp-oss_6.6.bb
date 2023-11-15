# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "6.6"
LINUX_VERSION = "6.6"

SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v6.x/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"
SRC_URI += "file://0016-rpmsg-virtio-overwrite-dst-add-on-ept-callback-if-se.patch"

SRC_URI[kernel.sha256sum] = "3c88ff4648122e81832b7067be6456ef28e119b64649a29e84f7f04691e489f9"
# ------------------------------------------------
require linux-stm32mp-oss.inc
