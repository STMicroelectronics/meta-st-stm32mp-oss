# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.17-rc6"
LINUX_VERSION = "5.17"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "275126af98e8c8b8334605413bf604aa924848515d811b63d272df838adf1525"
# ------------------------------------------------
require linux-stm32mp-oss.inc
