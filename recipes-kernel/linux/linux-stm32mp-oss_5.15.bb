# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.15-rc6"
LINUX_VERSION = "5.15"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "318a37a7e0fcbcb33fa19374b481535e6b2e58463a33d2d79827b5a53996e3d2"

# ------------------------------------------------
require linux-stm32mp-oss.inc
