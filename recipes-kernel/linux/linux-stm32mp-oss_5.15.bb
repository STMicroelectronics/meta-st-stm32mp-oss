# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.15-rc7"
LINUX_VERSION = "5.15"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "d2baa6a1aed59202f599fb69074d0adf5f2754b63e271a22465f29dd593bb774"

# ------------------------------------------------
require linux-stm32mp-oss.inc
