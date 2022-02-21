# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.17-rc5"
LINUX_VERSION = "5.17"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "f58de0f77ae34aefb6fec5c4942251194db6fb6522f91d4554c4ab4dbda8c133"
# ------------------------------------------------
require linux-stm32mp-oss.inc
