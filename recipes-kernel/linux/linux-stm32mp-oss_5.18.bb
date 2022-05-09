# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.18-rc6"
LINUX_VERSION = "5.18"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "1ee5c56028c0a0cd566d0bf3e0e92b0d1693ae5460755729e3f683573d4e80f8"
# ------------------------------------------------
require linux-stm32mp-oss.inc
