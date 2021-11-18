# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.16-rc1"
LINUX_VERSION = "5.16"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "f15cb8ed94671ac2e094380b429bafdab04f44bc3f29cca0b050ee2370a21d2c"
# ------------------------------------------------
require linux-stm32mp-oss.inc
