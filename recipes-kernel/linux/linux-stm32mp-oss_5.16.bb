# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.16-rc2"
LINUX_VERSION = "5.16"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "90be486cc939f008d40c8217e5e9bbd3dd0e0a998440214573a128295fb0dd80"
# ------------------------------------------------
require linux-stm32mp-oss.inc
