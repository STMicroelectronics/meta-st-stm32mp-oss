# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.17-rc1"
LINUX_VERSION = "5.17"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "9269b46a87c16941871f81057c8b82c913869eb86037b92c0bd4d5b9aa69dece"
# ------------------------------------------------
require linux-stm32mp-oss.inc
