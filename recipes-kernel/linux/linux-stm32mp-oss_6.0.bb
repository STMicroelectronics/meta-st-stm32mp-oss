# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "6.0"
LINUX_VERSION = "6.0"

SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v6.x/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "3e7557f0de28c0e8cd2c858c6ff3726aeb778db91b9da14bfc79e6df4169f8bd"
# ------------------------------------------------
require linux-stm32mp-oss.inc
