# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "6.0"
LINUX_VERSION = "6.0"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "f63602d33f9b98ab8abd1f668fe1debbb1eea28984e7218a0e051ac1d25aac37"
# ------------------------------------------------
require linux-stm32mp-oss.inc
