# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "6.0-rc2"
LINUX_VERSION = "6.0"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "529d24f9bef65074ff1b6e8d703981b3dbc0c328b3ba3112abca55b4da27dd81"
# ------------------------------------------------
require linux-stm32mp-oss.inc
