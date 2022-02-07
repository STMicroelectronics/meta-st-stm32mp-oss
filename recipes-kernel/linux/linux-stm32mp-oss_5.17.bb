# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.17-rc3"
LINUX_VERSION = "5.17"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "21b7b7d87dab777dc0e27de99bd167797b774be08b594f384839e54fd62cc217"
# ------------------------------------------------
require linux-stm32mp-oss.inc
