# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.16-rc8"
LINUX_VERSION = "5.16"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "4d64b2c48e3977f18519f71a95d93f0e6ac8e3d86fd69551332dee723c17e3b8"

# ------------------------------------------------
require linux-stm32mp-oss.inc
